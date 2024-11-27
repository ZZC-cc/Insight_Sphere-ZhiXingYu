package com.zzc.init.admin.comment.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zzc.init.admin.comment.model.dto.CommentAddRequest;
import com.zzc.init.admin.comment.model.entity.Comment;
import com.zzc.init.admin.comment.model.vo.CommentVO;

import java.util.List;

/**
 * 评论服务
 */
public interface CommentService extends IService<Comment> {

    boolean addComment(CommentAddRequest addRequest);

    List<CommentVO> getAllCommentsTree();

    List<CommentVO> getCommentsByPostId(Long postId);

    List<CommentVO> getAllCommentsList();

    List<CommentVO> getGetCommentsListByNumber(int number);

    List<CommentVO> getCommentBySearchText(String searchText);

    boolean deleteCommentsByPostId(Long postId);
}
