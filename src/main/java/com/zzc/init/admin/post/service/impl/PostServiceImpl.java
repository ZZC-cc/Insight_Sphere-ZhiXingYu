package com.zzc.init.admin.post.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzc.init.admin.post.model.dto.*;
import com.zzc.init.admin.post.model.entity.Post;
import com.zzc.init.admin.post.model.entity.PostFavorite;
import com.zzc.init.admin.post.model.entity.PostLike;
import com.zzc.init.admin.post.model.vo.PostVO;
import com.zzc.init.admin.post.service.PostEsDao;
import com.zzc.init.admin.post.service.PostService;
import com.zzc.init.admin.user.model.entity.User;
import com.zzc.init.admin.user.model.vo.UserVO;
import com.zzc.init.admin.user.service.UserService;
import com.zzc.init.common.ErrorCode;
import com.zzc.init.constant.UserConstant;
import com.zzc.init.exception.BusinessException;
import com.zzc.init.exception.ThrowUtils;
import com.zzc.init.mapper.PostFavoriteMapper;
import com.zzc.init.mapper.PostLikeMapper;
import com.zzc.init.mapper.PostMapper;
import com.zzc.init.utils.PostTagUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 帖子服务实现
 */
@Service
@Slf4j
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Resource
    private UserService userService;

    @Resource
    private PostLikeMapper postLikeMapper;

    @Resource
    private PostFavoriteMapper postFavoriteMapper;

    @Resource
    private ElasticsearchRestTemplate esTemplate;

    @Resource
    private PostEsDao postEsDao;


    /**
     * 添加帖子
     */
    @Override
    public boolean addPost(PostAddRequest postAddRequest, HttpServletRequest request) {
        Post post = new Post();
        BeanUtils.copyProperties(postAddRequest, post);
        List<String> tags = postAddRequest.getTagList();
        if (tags != null) {
            post.setTags(JSONUtil.toJsonStr(tags));
        }
        validPost(post, true);
        User loginUser = new User();
        BeanUtils.copyProperties(userService.getLoginUser(request), loginUser);
        post.setUserId(loginUser.getUser_id());
        post.setFavourNum(0);
        post.setThumbNum(0);
        boolean result = this.save(post);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }

    /**
     * 删除帖子
     */
    @Override
    public boolean deletePost(PostDeleteRequest deleteRequest, HttpServletRequest request) {
        User user = new User();
        BeanUtil.copyProperties(userService.getLoginUser(request), user);
        long id = deleteRequest.getPost_id();
        // 判断是否存在
        Post oldPost = this.getById(id);
        ThrowUtils.throwIf(oldPost == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldPost.getUserId().equals(user.getUser_id()) && !user.getRole().equals(UserConstant.ADMIN_ROLE)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = this.removeById(id);
        return true;
    }

    /**
     * 增量同步帖子到 ES
     */
    public void syncIncrementalPostsToEs() {
        // 查询近 5 分钟内的数据
        Date fiveMinutesAgoDate = new Date(new Date().getTime() - 5 * 60 * 1000L);
        List<Post> postList = baseMapper.listPostWithDelete(fiveMinutesAgoDate);
        if (CollUtil.isEmpty(postList)) {
            log.info("增量同步文章：过去5分钟没有文章");
            return;
        }
        List<PostEsDTO> postEsDTOList = postList.stream()
                .map(PostEsDTO::objToDto)
                .collect(Collectors.toList());
        final int pageSize = 500;
        int total = postEsDTOList.size();
        log.info("IncSyncPostToEs start, total {}", total);
        for (int i = 0; i < total; i += pageSize) {
            int end = Math.min(i + pageSize, total);
            log.info("sync from {} to {}", i, end);
            postEsDao.saveAll(postEsDTOList.subList(i, end));
        }
        log.info("增量同步文章到ES, 总数 {}", total);
    }

    @Override
    public List<PostVO> getPagedPosts(int page, int size) {
        // 设置分页条件
        Page<Post> postPage = new Page<>(page, size);
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isDelete", 0); // 仅查询未删除的文章
        queryWrapper.orderByDesc("createTime");

        // 查询分页数据
        IPage<Post> postIPage = this.page(postPage, queryWrapper);
        List<Post> postList = postIPage.getRecords();

        // 转换为 VO
        List<PostVO> postVOS = postList.stream()
                .map(PostVO::objToVo)
                .collect(Collectors.toList());

        // 填充用户信息
        postVOS.forEach(postVO -> postVO.setUser(userService.getUserByUserId(postVO.getUserId())));
        return postVOS;
    }


    @Override
    public List<PostVO> searchAllPostsFromMySQL() {
        List<Post> postList = this.list();
        List<PostVO> postVOList = new ArrayList<>();
        for (Post post : postList) {
            PostVO postVO = PostVO.objToVo(post);
            postVOList.add(postVO);
            postVO.setUser(userService.getUserVO(post.getUserId()));
            // 查询是否点赞
            QueryWrapper<PostLike> thumbQuery = new QueryWrapper<>();
            thumbQuery.eq("postId", postVO.getId()).eq("userId", postVO.getUserId());
            postVO.setIsThumbed(postLikeMapper.selectCount(thumbQuery) > 0);

            // 查询是否收藏
            QueryWrapper<PostFavorite> favourQuery = new QueryWrapper<>();
            favourQuery.eq("postId", postVO.getId()).eq("userId", postVO.getUserId());
            postVO.setIsFavoured(postFavoriteMapper.selectCount(favourQuery) > 0);
        }

        return postVOList;
    }

    @Override
    public List<PostVO> searchAllPostsFromEs() {
        syncIncrementalPostsToEs();
        // 构建查询，排除已删除的数据
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchAllQuery())
                        .filter(QueryBuilders.termQuery("isDelete", "0"))) // 仅查询未删除的数据
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC));


        // 执行搜索
        SearchHits<PostEsDTO> searchHits = esTemplate.search(queryBuilder.build(), PostEsDTO.class);

        // 解析结果
        List<PostVO> result = new ArrayList<>();
        for (SearchHit<PostEsDTO> hit : searchHits) {
            PostEsDTO esDTO = hit.getContent();

            // 转换为 PostVO
            PostVO postVO = PostVO.objToVo(PostEsDTO.dtoToObj(esDTO));
            postVO.setUser(userService.getUserVO(esDTO.getUserId()));
            // 查询是否点赞
            QueryWrapper<PostLike> thumbQuery = new QueryWrapper<>();
            thumbQuery.eq("postId", postVO.getId()).eq("userId", esDTO.getUserId());
            postVO.setIsThumbed(postLikeMapper.selectCount(thumbQuery) > 0);

            // 查询是否收藏
            QueryWrapper<PostFavorite> favourQuery = new QueryWrapper<>();
            favourQuery.eq("postId", postVO.getId()).eq("userId", esDTO.getUserId());
            postVO.setIsFavoured(postFavoriteMapper.selectCount(favourQuery) > 0);
            // 添加到结果列表
            result.add(postVO);
        }

        return result;
    }

    @Override
    public List<PostVO> searchSortedPostsFromEs(PostQueryRequest queryRequest) {
        // 设置分页条件
        int currentPage = queryRequest.getCurrent() > 0 ? queryRequest.getCurrent() - 1 : 0; // ES 的分页是从 0 开始的
        int pageSize = queryRequest.getPageSize();
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        // 增量同步 ES 数据
        syncIncrementalPostsToEs();

        // 设置排序规则
        SortOrder order = "asc".equalsIgnoreCase(queryRequest.getSortOrder()) ? SortOrder.ASC : SortOrder.DESC;

        // 构建查询
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchAllQuery())
                        .filter(QueryBuilders.termQuery("isDelete", "0"))) // 仅查询未删除的数据
                .withSort(SortBuilders.fieldSort(queryRequest.getSortField()).order(order)) // 添加排序字段
                .withPageable(pageable); // 添加分页条件

        // 执行搜索
        SearchHits<PostEsDTO> searchHits = esTemplate.search(queryBuilder.build(), PostEsDTO.class);

        // 解析结果
        List<PostVO> result = new ArrayList<>();
        for (SearchHit<PostEsDTO> hit : searchHits) {
            PostEsDTO esDTO = hit.getContent();
            PostVO postVO = PostVO.objToVo(PostEsDTO.dtoToObj(esDTO));
            postVO.setUser(userService.getUserVO(esDTO.getUserId()));

            // 查询是否点赞
            QueryWrapper<PostLike> thumbQuery = new QueryWrapper<>();
            thumbQuery.eq("postId", postVO.getId()).eq("userId", esDTO.getUserId());
            postVO.setIsThumbed(postLikeMapper.selectCount(thumbQuery) > 0);

            // 查询是否收藏
            QueryWrapper<PostFavorite> favourQuery = new QueryWrapper<>();
            favourQuery.eq("postId", postVO.getId()).eq("userId", esDTO.getUserId());
            postVO.setIsFavoured(postFavoriteMapper.selectCount(favourQuery) > 0);

            result.add(postVO);
        }
        return result;
    }


    /**
     * 搜索帖子(ES)
     *
     * @param queryRequest
     * @param request
     * @return
     */
    @Override
    public List<PostVO> searchFromEs(PostQueryRequest queryRequest, HttpServletRequest request) {
        String searchText = queryRequest.getSearchText();

        // 构建查询
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(searchText, "title", "content", "tags"))
                .withHighlightFields(
                        new HighlightBuilder.Field("title")
                                .preTags("<em style='background-color:yellow'>")
                                .postTags("</em>"),
                        new HighlightBuilder.Field("content")
                                .preTags("<em style='background-color:yellow'>")
                                .postTags("</em>")
                );

        // 执行搜索
        SearchHits<PostEsDTO> searchHits = esTemplate.search(queryBuilder.build(), PostEsDTO.class);

        // 解析结果
        List<PostVO> result = new ArrayList<>();
        for (SearchHit<PostEsDTO> hit : searchHits) {
            PostEsDTO esDTO = hit.getContent();

            // 解析高亮字段
            Map<String, List<String>> highlightFields = hit.getHighlightFields();
            if (highlightFields.containsKey("title")) {
                esDTO.setTitle(highlightFields.get("title").get(0)); // 替换高亮标题
            }
            if (highlightFields.containsKey("content")) {
                esDTO.setContent(highlightFields.get("content").get(0)); // 替换高亮内容
            }

            // 转换为 PostVO
            PostVO postVO = PostVO.objToVo(PostEsDTO.dtoToObj(esDTO));

            // 获取用户信息
            postVO.setUser(userService.getUserByUserId(postVO.getUserId()));
            // 查询是否点赞
            QueryWrapper<PostLike> thumbQuery = new QueryWrapper<>();
            thumbQuery.eq("postId", postVO.getId()).eq("userId", esDTO.getUserId());
            postVO.setIsThumbed(postLikeMapper.selectCount(thumbQuery) > 0);

            // 查询是否收藏
            QueryWrapper<PostFavorite> favourQuery = new QueryWrapper<>();
            favourQuery.eq("postId", postVO.getId()).eq("userId", esDTO.getUserId());
            postVO.setIsFavoured(postFavoriteMapper.selectCount(favourQuery) > 0);


            // 添加到结果列表
            result.add(postVO);
        }

        return result;
    }

    @Override
    public void incrementViewsNum(Long postId) {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", postId);
        Post post = getOne(queryWrapper);
        post.setViewsNum(post.getViewsNum() + 1);
        updateById(post);
    }


    @Override
    public List<PostVO> getPostsByTag(String tag, HttpServletRequest request) {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(tag)) {
            queryWrapper.like("tags", tag);
        }
        queryWrapper.orderByDesc("createTime");
        List<Post> posts = this.list(queryWrapper);
        if (posts.isEmpty()) {
            return Collections.emptyList();
        }

        // 获取当前登录用户
        UserVO loginUser = userService.getLoginUser(request);
        Long userId = loginUser != null ? loginUser.getUser_id() : null;

        List<PostVO> postVOS = posts.stream()
                .map(PostVO::objToVo)
                .collect(Collectors.toList());

        // 填充用户信息和点赞/收藏状态
        postVOS.forEach(postVO -> {
            // 设置创建用户信息
            postVO.setUser(userService.getUserByUserId(postVO.getUserId()));

            if (userId != null) {
                // 查询是否点赞
                QueryWrapper<PostLike> thumbQuery = new QueryWrapper<>();
                thumbQuery.eq("postId", postVO.getId()).eq("userId", userId);
                postVO.setIsThumbed(postLikeMapper.selectCount(thumbQuery) > 0);

                // 查询是否收藏
                QueryWrapper<PostFavorite> favourQuery = new QueryWrapper<>();
                favourQuery.eq("postId", postVO.getId()).eq("userId", userId);
                postVO.setIsFavoured(postFavoriteMapper.selectCount(favourQuery) > 0);
            } else {
                postVO.setIsThumbed(false);
                postVO.setIsFavoured(false);
            }
        });

        return postVOS;
    }


    @Override
    public boolean thumbPost(Long postId, HttpServletRequest request) {
        UserVO loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getUser_id();

        // 检查是否已点赞
        QueryWrapper<PostLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId).eq("postId", postId);
        PostLike existingThumb = postLikeMapper.selectOne(queryWrapper);

        if (existingThumb != null) {
            // 已点赞，执行取消点赞
            postLikeMapper.delete(queryWrapper);
            // 更新文章点赞数
            this.update().setSql("thumbNum = thumbNum - 1").eq("id", postId).update();
            return true;
        } else {
            // 未点赞，执行点赞
            PostLike postLike = new PostLike();
            postLike.setUserId(userId);
            postLike.setPostId(postId);
            postLikeMapper.insert(postLike);
            // 更新文章点赞数
            this.update().setSql("thumbNum = thumbNum + 1").eq("id", postId).update();
            return true;
        }
    }

    @Override
    public boolean favourPost(Long postId, HttpServletRequest request) {
        UserVO loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getUser_id();

        // 检查是否已收藏
        QueryWrapper<PostFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId).eq("postId", postId);
        PostFavorite existingFavour = postFavoriteMapper.selectOne(queryWrapper);

        if (existingFavour != null) {
            // 已收藏，执行取消收藏
            postFavoriteMapper.delete(queryWrapper);
            // 更新文章收藏数
            this.update().setSql("favourNum = favourNum - 1").eq("id", postId).update();
            return true;
        } else {
            // 未收藏，执行收藏
            PostFavorite newFavour = new PostFavorite();
            newFavour.setUserId(userId);
            newFavour.setPostId(postId);
            postFavoriteMapper.insert(newFavour);
            // 更新文章收藏数
            this.update().setSql("favourNum = favourNum + 1").eq("id", postId).update();
            return true;
        }
    }


    @Override
    public void validPost(Post post, boolean add) {
        if (post == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = post.getTitle();
        String content = post.getContent();
        String tags = post.getTags();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 100000) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
    }

    /**
     * 多类型搜索
     *
     * @param searchText 搜索文本
     */
    @Override
    public List<PostVO> getPostsBySearchText(String searchText) {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("createTime");
        if (StringUtils.isBlank(searchText)) {

            List<Post> posts = this.list(queryWrapper);
            List<PostVO> postVOS = posts.stream().map(post1 -> PostVO.objToVo(post1)).collect(java.util.stream.Collectors.toList());
            postVOS.forEach(postVO -> {
                postVO.setUser(userService.getUserByUserId(postVO.getUserId()));
            });
            return postVOS;
        }
        queryWrapper.like("title", searchText)
                .or()
                .like("content", searchText)
                .or()
                .like("tags", searchText);
        List<Post> posts = this.list(queryWrapper);
        List<PostVO> postVOS = posts.stream().map(post1 -> PostVO.objToVo(post1)).collect(java.util.stream.Collectors.toList());
        postVOS.forEach(postVO -> {
            postVO.setUser(userService.getUserByUserId(postVO.getUserId()));
        });
        return postVOS;
    }

    /**
     * 获取当前用户创建的资源列表
     */
    @Override
    public List<PostVO> getMyPostsVO(HttpServletRequest request) {
        UserVO loginUser = userService.getLoginUser(request);

        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getUser_id()).orderByDesc("updateTime").orderByDesc("createTime");
        List<Post> posts = this.list(queryWrapper);
        List<PostVO> postVOS = new ArrayList<>();
        for (Post post : posts) {
            PostVO postVO = PostVO.objToVo(post);
            postVO.setUser(userService.getUserByUserId(post.getUserId()));
            // 查询是否点赞
            QueryWrapper<PostLike> thumbQuery = new QueryWrapper<>();
            thumbQuery.eq("postId", postVO.getId()).eq("userId", loginUser.getUser_id());
            postVO.setIsThumbed(postLikeMapper.selectCount(thumbQuery) > 0);

            // 查询是否收藏
            QueryWrapper<PostFavorite> favourQuery = new QueryWrapper<>();
            favourQuery.eq("postId", postVO.getId()).eq("userId", loginUser.getUser_id());
            postVO.setIsFavoured(postFavoriteMapper.selectCount(favourQuery) > 0);
            postVOS.add(postVO);
        }
        return postVOS;
    }

    /**
     * 获取指定数量的VO
     */
    @Override
    public List<PostVO> getPostsVOByNumber(int number) {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.last("limit " + number).orderByDesc("viewsNum").orderByDesc("thumbNum").orderByDesc("favourNum").orderByDesc("createTime");
        List<Post> posts = this.list(queryWrapper);
        List<PostVO> postVOS = posts.stream().map(post1 -> PostVO.objToVo(post1)).collect(java.util.stream.Collectors.toList());
        postVOS.forEach(postVO -> {
            postVO.setUser(userService.getUserByUserId(postVO.getUserId()));
        });
        return postVOS;
    }

    /**
     * 编辑帖子（用户）
     */
    @Override
    public boolean editPost(@RequestBody PostEditRequest postEditRequest, HttpServletRequest request) {
        if (postEditRequest == null || postEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Post post = new Post();
        BeanUtils.copyProperties(postEditRequest, post);
        List<String> tags = postEditRequest.getTags();
        if (tags != null) {
            post.setTags(JSONUtil.toJsonStr(tags));
        }
        // 参数校验
        this.validPost(post, false);
        User loginUser = new User();
        BeanUtils.copyProperties(userService.getLoginUser(request), loginUser);
        long id = postEditRequest.getId();
        // 判断是否存在
        Post oldPost = this.getById(id);
        ThrowUtils.throwIf(oldPost == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldPost.getUserId().equals(loginUser.getUser_id()) && !loginUser.getRole().equals(UserConstant.ADMIN_ROLE)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = this.updateById(post);
        return result;
    }

    /**
     * 更新（仅管理员）
     */
    @Override
    public boolean updatePost(@RequestBody PostUpdateRequest postUpdateRequest) {
        Post post = new Post();
        BeanUtils.copyProperties(postUpdateRequest, post);
        List<String> tags = postUpdateRequest.getTagList();
        if (tags != null) {
            post.setTags(JSONUtil.toJsonStr(tags));
        }
        // 参数校验
        this.validPost(post, false);
        long id = postUpdateRequest.getId();
        // 判断是否存在
        Post oldPost = this.getById(id);
        ThrowUtils.throwIf(oldPost == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = this.updateById(post);
        return result;
    }

    /**
     * 通过id获取文章详情
     */
    @Override
    public PostVO getPostById(Long id, HttpServletRequest request) {
        // 查询文章
        Post post = this.getById(id);
        if (post == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "资源不存在");
        }

        // 将 Post 转换为 PostVO
        PostVO postVO = PostVO.objToVo(post);

        // 设置用户信息
        postVO.setUser(userService.getUserByUserId(post.getUserId()));

        // 获取当前登录用户
        UserVO currentUser = userService.getLoginUser(request); // 假设有此方法，获取当前登录用户
        if (currentUser != null) {
            Long userId = currentUser.getUser_id();
            // 查询是否点赞
            QueryWrapper<PostLike> thumbQuery = new QueryWrapper<>();
            thumbQuery.eq("postId", postVO.getId()).eq("userId", userId);
            postVO.setIsThumbed(postLikeMapper.selectCount(thumbQuery) > 0);

            // 查询是否收藏
            QueryWrapper<PostFavorite> favourQuery = new QueryWrapper<>();
            favourQuery.eq("postId", postVO.getId()).eq("userId", userId);
            postVO.setIsFavoured(postFavoriteMapper.selectCount(favourQuery) > 0);
        } else {
            // 如果未登录，默认未点赞和未收藏
            postVO.setIsThumbed(false);
            postVO.setIsFavoured(false);
        }

        return postVO;
    }


    /**
     * 获取全部标签名称
     */
    @Override
    public List<String> getAllTags() {
        List<Post> posts = this.list();
        PostTagUtils postTagUtils = new PostTagUtils();
        return postTagUtils.getAllTags(posts);
    }

    /**
     * 获取用户收藏的所有帖子
     */
    @Override
    public List<PostVO> getFavouredPosts(HttpServletRequest request) {
        UserVO user = userService.getLoginUser(request);
        QueryWrapper<PostFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", user.getUser_id()).orderByDesc("createTime");
        List<PostFavorite> postFavorites = postFavoriteMapper.selectList(queryWrapper);
        List<Long> postIds = postFavorites.stream().map(PostFavorite::getPostId).collect(Collectors.toList());
        if (postIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<Post> posts = this.listByIds(postIds);
        List<PostVO> postVOS = new ArrayList<>();
        for (Post post : posts) {
            PostVO postVO = PostVO.objToVo(post);
            postVO.setUser(userService.getUserByUserId(post.getUserId()));
            // 查询是否点赞
            QueryWrapper<PostLike> thumbQuery = new QueryWrapper<>();
            thumbQuery.eq("postId", postVO.getId()).eq("userId", user.getUser_id());
            postVO.setIsThumbed(postLikeMapper.selectCount(thumbQuery) > 0);

            // 查询是否收藏
            QueryWrapper<PostFavorite> favourQuery = new QueryWrapper<>();
            favourQuery.eq("postId", postVO.getId()).eq("userId", user.getUser_id());
            postVO.setIsFavoured(postFavoriteMapper.selectCount(favourQuery) > 0);
            postVOS.add(postVO);
        }
        return postVOS;
    }

}




