package com.zzc.init.admin.home.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzc.init.admin.ai.service.AIPlatformService;
import com.zzc.init.admin.comment.service.CommentService;
import com.zzc.init.admin.home.model.entity.Home;
import com.zzc.init.admin.home.model.vo.HomeVO;
import com.zzc.init.admin.home.service.HomeService;
import com.zzc.init.admin.order.service.OrderService;
import com.zzc.init.admin.post.service.PostService;
import com.zzc.init.admin.product.service.ProductService;
import com.zzc.init.admin.user.service.UserService;
import com.zzc.init.mapper.HomeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 网站数据服务实现
 */
@Service
@Slf4j
public class HomeServiceImpl extends ServiceImpl<HomeMapper, Home> implements HomeService {

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    @Resource
    private CommentService commentService;

    @Resource
    private ProductService productService;

    @Resource
    private OrderService orderService;


    @Resource
    private AIPlatformService aiPlatformService;

    /**
     * 获取HomeVO
     */
    @Override
    public HomeVO getHomeVO() {
        HomeVO homeVO = new HomeVO();
        homeVO.setCommentCount(commentService.list().size());
        homeVO.setPostCount(postService.list().size());
        homeVO.setUserCount(userService.list().size());
        homeVO.setOrderCount(orderService.list().size());
        homeVO.setProductCount(productService.list().size());
        homeVO.setPostList(postService.getPostsVOByNumber(6));
        homeVO.setCommentList(commentService.getGetCommentsListByNumber(6));
//        homeVO.setOrderList(orderService.getOrderVOByNumber(5));
        homeVO.setProductList(productService.getVoByNumber(6));
        homeVO.setAITop10(aiPlatformService.listPlatforms());
//        homeVO.setNotice(noticeService.news());
        return homeVO;
    }

}
