package com.zzc.init.admin.post.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zzc.init.admin.post.model.dto.*;
import com.zzc.init.admin.post.model.entity.Post;
import com.zzc.init.admin.post.model.vo.PostVO;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子服务
 */
public interface PostService extends IService<Post> {

    boolean addPost(PostAddRequest postAddRequest, HttpServletRequest request);

    boolean deletePost(PostDeleteRequest deleteRequest, HttpServletRequest request);

    /**
     * 校验
     *
     * @param post
     * @param add
     */
    void validPost(Post post, boolean add);

    List<PostVO> getPostsBySearchText(String searchText);

    List<PostVO> getMyPostsVO(HttpServletRequest request);

    List<PostVO> getPostsVOByNumber(int number);

    boolean editPost(@RequestBody PostEditRequest postEditRequest, HttpServletRequest request);

    boolean updatePost(@RequestBody PostUpdateRequest postUpdateRequest);

    PostVO getPostById(Long id, HttpServletRequest request);

    List<String> getAllTags();


    List<PostVO> getPostsByTag(String tag, HttpServletRequest request);

    boolean thumbPost(Long postId, HttpServletRequest request);

    boolean favourPost(Long postId, HttpServletRequest request);

    List<PostVO> searchFromEs(PostQueryRequest queryRequest, HttpServletRequest request);

    void incrementViewsNum(Long postId);

    List<PostVO> getPagedPosts(int page, int size);

    List<PostVO> searchAllPostsFromMySQL();

    List<PostVO> searchAllPostsFromEs();

    List<PostVO> searchSortedPostsFromEs(PostQueryRequest queryRequest);


    List<PostVO> getFavouredPosts(HttpServletRequest request);
}
