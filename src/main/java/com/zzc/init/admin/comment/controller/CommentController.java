package com.zzc.init.admin.comment.controller;

import com.zzc.init.admin.comment.model.dto.CommentAddRequest;
import com.zzc.init.admin.comment.model.dto.CommentDeleteRequest;
import com.zzc.init.admin.comment.model.vo.CommentVO;
import com.zzc.init.admin.comment.service.CommentService;
import com.zzc.init.common.BaseResponse;
import com.zzc.init.common.ErrorCode;
import com.zzc.init.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评论接口
 */
@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add")
    public BaseResponse<String> addComment(@RequestBody CommentAddRequest addRequest) {
        if (commentService.addComment(addRequest)) {
            return ResultUtils.success("评论成功");
        } else {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }
    }

    @GetMapping("/all")
    public BaseResponse<List<CommentVO>> getAllComments() {
        List<CommentVO> commentVOList = commentService.getAllCommentsTree();
        return ResultUtils.success(commentVOList);
    }

    @GetMapping("/list")
    public BaseResponse<List<CommentVO>> getAllCommentsList() {
        List<CommentVO> commentVOList = commentService.getAllCommentsList();
        return ResultUtils.success(commentVOList);
    }

    /**
     * 删除评论
     */
    @PostMapping("/delete")
    public BaseResponse<String> deleteComment(@RequestBody CommentDeleteRequest deleteRequest) {
        boolean b = commentService.removeById(deleteRequest.getId());
        if (b) {
            return ResultUtils.success("删除成功");
        } else {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }
    }

    /**
     * 根据文章id获取评论
     */
    @GetMapping("/getByPostId/{postId}")
    public BaseResponse<List<CommentVO>> getCommentsByPostId(@PathVariable("postId") Long postId) {
        List<CommentVO> commentVOList = commentService.getCommentsByPostId(postId);
        return ResultUtils.success(commentVOList);
    }

    @GetMapping("/search")
    public BaseResponse<List<CommentVO>> getCommentBySearchText(@RequestParam(value = "searchText", required = true) String searchText) {
        List<CommentVO> commentVOList = commentService.getCommentBySearchText(searchText);
        return ResultUtils.success(commentVOList);
    }
}
