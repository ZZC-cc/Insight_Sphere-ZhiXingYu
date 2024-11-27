package com.zzc.init.admin.post.controller;

import com.alibaba.excel.util.StringUtils;
import com.zzc.init.admin.ChatGPT.service.ChatGPTService;
import com.zzc.init.admin.comment.service.CommentService;
import com.zzc.init.admin.post.model.dto.*;
import com.zzc.init.admin.post.model.vo.PostVO;
import com.zzc.init.admin.post.service.PostService;
import com.zzc.init.annotation.AuthCheck;
import com.zzc.init.common.BaseResponse;
import com.zzc.init.common.ErrorCode;
import com.zzc.init.common.ResultUtils;
import com.zzc.init.constant.UserConstant;
import com.zzc.init.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子接口
 */
@RestController
@RequestMapping("/post")
@Slf4j
public class PostController {

    @Resource
    private PostService postService;

    @Resource
    private CommentService commentService;

    @Resource
    private ChatGPTService chatGPTService;


    @PostMapping("/add")
    @Operation(summary = "创建帖子")
    public BaseResponse<String> addPost(@RequestBody PostAddRequest postAddRequest, HttpServletRequest request) {
        if (postAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = postService.addPost(postAddRequest, request);
        if (b) {
            return ResultUtils.success("添加帖子成功");
        } else {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }
    }


    @Operation(summary = "删除帖子")
    @PostMapping("/delete")
    public BaseResponse<String> deletePost(@RequestBody PostDeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getPost_id() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = postService.deletePost(deleteRequest, request);
        //删除评论
        commentService.deleteCommentsByPostId(deleteRequest.getPost_id());
        if (b) {
            return ResultUtils.success("删除帖子成功");
        } else {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }
    }

    @Operation(summary = "更新帖子")
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> updatePost(@RequestBody PostUpdateRequest postUpdateRequest) {
        if (postUpdateRequest == null || postUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = postService.updatePost(postUpdateRequest);
        if (b) {
            return ResultUtils.success("更新帖子成功");
        } else {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }
    }


    @PostMapping("/search/es")
    @Operation(summary = "搜索帖子（ES）")
    public BaseResponse<List<PostVO>> searchPostFromEs(@RequestBody PostQueryRequest queryRequest, HttpServletRequest request) {
        List<PostVO> posts = postService.searchFromEs(queryRequest, request);

        return ResultUtils.success(posts);
    }

    @PostMapping("/list/sorted")
    @Operation(summary = "按字段排序帖子")
    public BaseResponse<List<PostVO>> listSortedPosts(@RequestBody PostQueryRequest queryRequest) {

        List<PostVO> posts = postService.searchSortedPostsFromEs(queryRequest);
        return ResultUtils.success(posts);
    }


    @PostMapping("/incrementView/{postId}")
    @Operation(summary = "文章浏览量+1")
    public BaseResponse<String> incrementView(@PathVariable Long postId) {
        postService.incrementViewsNum(postId);
        return ResultUtils.success("浏览量+1");
    }

    @PostMapping("/generateContent")
    @Operation(summary = "生成文章内容")
    public BaseResponse<String> generateContent(@RequestBody PostGenerateRequest request) {
        String prompt = request.getPrompt();
        if (StringUtils.isBlank(prompt)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "提示词不能为空");
        }

        // 调用 ChatGPTService 生成内容
        String generatedContent = chatGPTService.generateContent(prompt);
        return ResultUtils.success(generatedContent);
    }

    @PostMapping("/generateSummary")
    @Operation(summary = "生成文章总结")
    public BaseResponse<String> generateSummary(@RequestParam Long postId, HttpServletRequest request) {
        String content = postService.getPostById(postId, request).getContent();
        String summary = chatGPTService.summarizePost(content);
        return ResultUtils.success(summary);
    }


    @GetMapping("/list/by/tag")
    @Operation(summary = "根据标签获取文章")
    public BaseResponse<List<PostVO>> listPostsByTag(@RequestParam(value = "tag", required = false) String tag, HttpServletRequest request) {
        List<PostVO> posts = postService.getPostsByTag(tag, request);
        return ResultUtils.success(posts);
    }

    @PostMapping("/thumb")
    @Operation(summary = "文章点赞/取消点赞")
    public BaseResponse<String> thumbPost(@RequestParam Long postId, HttpServletRequest request) {
        if (postId == null || postId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = postService.thumbPost(postId, request);
        return result ? ResultUtils.success("点赞成功") : ResultUtils.error(ErrorCode.OPERATION_ERROR, "点赞失败");
    }

    @PostMapping("/favour")
    @Operation(summary = "文章收藏/取消收藏")
    public BaseResponse<String> favourPost(@RequestParam Long postId, HttpServletRequest request) {
        if (postId == null || postId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = postService.favourPost(postId, request);
        return result ? ResultUtils.success("收藏成功") : ResultUtils.error(ErrorCode.OPERATION_ERROR, "收藏失败");
    }

    @Operation(summary = "获取帖子列表")
    @PostMapping("/list/all")
    public BaseResponse<List<PostVO>> getAllPosts() {
//        List<PostVO> postVOS = postService.searchAllPostsFromEs();
        List<PostVO> postVOS = postService.searchAllPostsFromMySQL();
        return ResultUtils.success(postVOS);
    }

    @Operation(summary = "获取总条数")
    @GetMapping("/get/total")
    public BaseResponse<Integer> getTotal() {
        Integer total = postService.list().size();
        return ResultUtils.success(total);
    }


    @Operation(summary = "获取当前用户创建的资源列表")
    @PostMapping("/my/list/vo")
    public BaseResponse<List<PostVO>> listMyPostVO(HttpServletRequest request) {
        List<PostVO> myPostsVO = postService.getMyPostsVO(request);
        return ResultUtils.success(myPostsVO);
    }

    @Operation(summary = "分页获取文章列表")
    @GetMapping("/list/page")
    public BaseResponse<List<PostVO>> listPagedPosts(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<PostVO> posts = postService.getPagedPosts(page, size);
        return ResultUtils.success(posts);
    }


    @Operation(summary = "多类型搜索")
    @GetMapping("/search")
    public BaseResponse<List<PostVO>> searchPostBySearchText(@RequestParam(value = "searchText", required = false) String searchText) {
        List<PostVO> posts = postService.getPostsBySearchText(searchText);
        return ResultUtils.success(posts);
    }

    @Operation(summary = "通过id获取文章")
    @GetMapping("/get/vo")
    public BaseResponse<PostVO> getPostVOById(long id, HttpServletRequest request) {
        return ResultUtils.success(postService.getPostById(id, request));
    }

    @Operation(summary = "获取全部标签名称")
    @GetMapping("/get/all/tags")
    public BaseResponse<List<String>> getAllTags() {
        List<String> tags = postService.getAllTags();
        return ResultUtils.success(tags);
    }
}
