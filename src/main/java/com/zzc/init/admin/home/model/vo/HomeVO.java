package com.zzc.init.admin.home.model.vo;

import com.zzc.init.admin.comment.model.vo.CommentVO;
import com.zzc.init.admin.notice.model.entity.Notice;
import com.zzc.init.admin.order.model.vo.OrderVO;
import com.zzc.init.admin.post.model.vo.PostVO;
import com.zzc.init.admin.product.model.vo.ProductVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 首页
 */
@Data
public class HomeVO implements Serializable {
    /**
     * 访问量
     */
    Integer visitCount;

    /**
     * 评论量
     */
    Integer commentCount;

    /**
     * 文章量
     */
    Integer postCount;

    /**
     * 用户量
     */
    Integer userCount;

    /**
     * 订单量
     */
    Integer orderCount;

    /**
     * 商品量
     */
    Integer productCount;


    /**
     * 订单金额
     */
    Double orderTotalPrice;

    /**
     * 最近的文章
     */
    List<PostVO> postList;

    /**
     * 最近的评论
     */
    List<CommentVO> commentList;

    /**
     * 最近的订单
     */
    List<OrderVO> orderList;

    /**
     * 最近的商品
     */
    List<ProductVO> productList;

    Notice notice;


}
