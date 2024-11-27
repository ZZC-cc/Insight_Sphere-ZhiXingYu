package com.zzc.init.admin.comment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzc.init.admin.comment.model.dto.CommentAddRequest;
import com.zzc.init.admin.comment.model.entity.Comment;
import com.zzc.init.admin.comment.model.vo.CommentVO;
import com.zzc.init.admin.comment.service.CommentService;
import com.zzc.init.admin.post.service.PostService;
import com.zzc.init.admin.user.service.UserService;
import com.zzc.init.mapper.CommentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论服务实现
 */
@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    @Override
    public boolean addComment(CommentAddRequest addRequest) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(addRequest, comment);
        return save(comment);
    }

    @Override
    public List<CommentVO> getAllCommentsTree() {
        List<Comment> comments = list(); // 查询数据库中的所有评论
        Map<Long, CommentVO> commentMap = new HashMap<>();
        List<CommentVO> commentVOList = new ArrayList<>();

        for (Comment comment : comments) {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(comment, commentVO);
            commentVOList.add(commentVO);
            commentVO.setUser(userService.getUserVO(comment.getUser_id()));
            commentMap.put(comment.getId(), commentVO);
        }

        for (Comment comment : comments) {
            Long parentId = comment.getParent_id();
            if (parentId != null && commentMap.containsKey(parentId)) {
                CommentVO parentComment = commentMap.get(parentId);
                if (parentComment.getChild() == null) {
                    parentComment.setChild(new ArrayList<>());
                }
                parentComment.getChild().add(commentMap.get(comment.getId()));
                commentVOList.remove(commentMap.get(comment.getId()));
            }
        }

        return commentVOList;
    }

    /**
     * 根据文章id获取评论
     *
     * @param postId 文章id
     * @return 文章对应的评论列表
     */
    @Override
    public List<CommentVO> getCommentsByPostId(Long postId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId);
        List<Comment> comments = list(queryWrapper);
        List<CommentVO> commentVOList = new ArrayList<>();
        Map<Long, CommentVO> commentMap = new HashMap<>();
        // 将评论转换为视图对象并存入Map中
        for (Comment comment : comments) {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(comment, commentVO);
            commentVO.setUser(userService.getUserVO(comment.getUser_id()));
            commentMap.put(comment.getId(), commentVO);
        }
        // 将子评论添加到对应的父评论的child中
        for (Comment comment : comments) {
            Long parentId = comment.getParent_id();
            if (parentId != null && commentMap.containsKey(parentId)) {
                CommentVO parentComment = commentMap.get(parentId);
                if (parentComment.getChild() == null) {
                    parentComment.setChild(new ArrayList<>());
                }
                parentComment.getChild().add(commentMap.get(comment.getId()));
            } else {
                // 如果没有父评论，则将其作为顶层评论加入到评论列表中
                commentVOList.add(commentMap.get(comment.getId()));
            }
        }
        return commentVOList;
    }

    @Override
    public List<CommentVO> getAllCommentsList() {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("createTime");
        List<Comment> comments = this.list(queryWrapper); // 查询数据库中的所有评论
        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment comment : comments) {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(comment, commentVO);
            commentVO.setUser(userService.getUserVO(comment.getUser_id()));
            commentVO.setPost_name(postService.getById(comment.getPost_id()).getTitle());
            commentVOList.add(commentVO);
        }
        return commentVOList;
    }

    @Override
    public List<CommentVO> getGetCommentsListByNumber(int number) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("createTime")
                .last("limit " + number);
        List<Comment> comments = this.list(queryWrapper);
        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment comment : comments) {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(comment, commentVO);
            commentVO.setUser(userService.getUserVO(comment.getUser_id()));
            commentVO.setPost_name(postService.getById(comment.getPost_id()).getTitle());
            commentVOList.add(commentVO);
        }
        return commentVOList;
    }

    /**
     * 多类型搜索
     *
     * @param searchText 搜索文本
     */
    @Override
    public List<CommentVO> getCommentBySearchText(String searchText) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("content", searchText)
                .or()
                .like("user_name", searchText);
        List<Comment> comments = this.list(queryWrapper); // 查询数据库中的所有评论
        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment comment : comments) {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(comment, commentVO);
            commentVO.setUser(userService.getUserVO(comment.getUser_id()));
            commentVO.setPost_name(postService.getById(comment.getPost_id()).getTitle());
            commentVOList.add(commentVO);
        }
        return commentVOList;
    }

    /**
     * 根据文章id删除全部评论
     */
    @Override
    public boolean deleteCommentsByPostId(Long postId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId);
        try {
            return this.remove(queryWrapper);
        } catch (Exception e) {
            log.error("无评论", e);
        }
        return true;
    }
}
