package com.zzc.init.admin.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzc.init.admin.order.model.entity.Order;
import com.zzc.init.admin.product.model.dto.ProductAddRequest;
import com.zzc.init.admin.product.model.dto.ProductUpdateRequest;
import com.zzc.init.admin.product.model.entity.Product;
import com.zzc.init.admin.product.model.vo.ProductVO;
import com.zzc.init.admin.product.service.ProductService;
import com.zzc.init.admin.user.service.impl.UserServiceImpl;
import com.zzc.init.mapper.OrderMapper;
import com.zzc.init.mapper.ProductMapper;
import com.zzc.init.utils.ProductTagUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 帖子服务实现
 */
@Service
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 添加教程
     */
    @Override
    public boolean addProduct(ProductAddRequest addRequest) {
        // 1. 验证教程
        this.validProduct(addRequest);
        // 2. 添加教程
        Product product = new Product();
        BeanUtils.copyProperties(addRequest, product);
        return this.save(product);
    }


    /**
     * 获取所有教程VO列表（用户）
     */
    @Override
    public List<ProductVO> getAllProducts(HttpServletRequest request) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("createTime");
        List<Product> products = this.list(queryWrapper);
        List<ProductVO> productVOS = new ArrayList<>();
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("userId", userService.getLoginUser(request).getUser_id());
        List<Order> orders = orderMapper.selectList(orderQueryWrapper);


        for (Product product : products) {
            ProductVO productVO = ProductVO.objToVo(product);
            productVO.setUserVO(userService.getUserVO(product.getUserId()));
            if (orders.size() != 0) {
                productVO.setIsBuy(1);
            } else {
                productVO.setIsBuy(0);
            }
            productVOS.add(productVO);
        }
        return productVOS;
    }

    /**
     * 更新教程
     */
    @Override
    public boolean updateProduct(ProductUpdateRequest updateRequest) {
        // 1. 验证教程
        this.validProduct(updateRequest);
        // 2. 更新教程
        Product product = new Product();
        BeanUtils.copyProperties(updateRequest, product);
        return this.updateById(product);
    }

    /**
     * 切换教程上下架状态
     */
    @Override
    public String changeShelvesStatus(Long id) {
        Product product = this.getById(id);
        if (product.getIsShelves() == 1) {
            product.setIsShelves(0);
            this.updateById(product);
            return "下架成功";
        } else {
            product.setIsShelves(1);
            this.updateById(product);
            return "上架成功";
        }
    }

    /**
     * 多类型搜索
     */
    @Override
    public List<ProductVO> searchProductBySearchText(String searchText) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", searchText)
                .or()
                .like("tags", searchText)
                .or()
                .like("description", searchText)
                .orderByDesc("createTime");
        List<Product> products = this.list(queryWrapper);
        List<ProductVO> productVOS = new ArrayList<>();
        for (Product product : products) {
            ProductVO productVO = ProductVO.objToVo(product);
            productVO.setUserVO(userService.getUserVO(product.getUserId()));
            productVOS.add(productVO);
        }
        return productVOS;
    }


    /**
     * 获取全部标签名称
     */
    @Override
    public List<String> getAllTags() {
        List<Product> list = this.list();
        ProductTagUtils productTagUtils = new ProductTagUtils();
        return productTagUtils.getAllTags(list);
    }


    /**
     * 通过id获取VO
     */
    @Override
    public ProductVO getProductById(Long id, HttpServletRequest request) {
        Product product = this.getById(id);
        ProductVO productVO = ProductVO.objToVo(product);
        productVO.setUserVO(userService.getUserVO(product.getUserId()));
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("userId", userService.getLoginUser(request).getUser_id());
        List<Order> orders = orderMapper.selectList(orderQueryWrapper);
        if (orders.size() != 0) {
            productVO.setIsBuy(1);
        } else {
            productVO.setIsBuy(0);
        }

        return productVO;
    }

    /**
     * 通过标签名称获取教程列表
     */
    @Override
    public List<ProductVO> getProductsByTags(String tags) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("tags", tags)
                .orderByDesc("createTime");
        List<Product> products = this.list(queryWrapper);
        List<ProductVO> productVOS = new ArrayList<>();
        for (Product product : products) {
            ProductVO productVO = ProductVO.objToVo(product);
            productVO.setUserVO(userService.getUserVO(product.getUserId()));
            productVOS.add(productVO);
        }
        return productVOS;
    }

    private <T> void validProduct(T request) {
        if (request instanceof ProductAddRequest) {
            ProductAddRequest product = (ProductAddRequest) request;
            // 1. 验证教程标题
            if (StringUtils.isEmpty(product.getTitle())) {
                throw new RuntimeException("教程标题不能为空");
            }
            // 2. 验证教程描述
            if (StringUtils.isEmpty(product.getDescription())) {
                throw new RuntimeException("教程描述不能为空");
            }
            // 3. 验证教程价格
            if (StringUtils.isEmpty(product.getPrice().toString())) {
                throw new RuntimeException("教程价格不能为空");
            }
            if (product.getPrice() <= 0) {
                throw new RuntimeException("教程价格必须大于 0");
            }
            // 4. 验证教程库存
            if (product.getStock() <= 0) {
                throw new RuntimeException("教程库存必须大于 0");
            }
            // 5. 验证教程图片
            if (StringUtils.isEmpty(product.getImages())) {
                throw new RuntimeException("教程图片不能为空");
            }
            // 6. 验证教程标签
            if (StringUtils.isEmpty(product.getTags())) {
                throw new RuntimeException("教程标签不能为空");
            }
            // 7. 验证教程描述
            if (StringUtils.isEmpty(product.getDescription())) {
                throw new RuntimeException("教程描述不能为空");
            }
        } else if (request instanceof ProductUpdateRequest) {
            ProductUpdateRequest product = (ProductUpdateRequest) request;
            // 1. 验证教程标题
            if (StringUtils.isEmpty(product.getTitle())) {
                throw new RuntimeException("教程标题不能为空");
            }
            // 2. 验证教程描述
            if (StringUtils.isEmpty(product.getDescription())) {
                throw new RuntimeException("教程描述不能为空");
            }
            // 3. 验证教程价格
            if (StringUtils.isEmpty(product.getPrice().toString())) {
                throw new RuntimeException("教程价格不能为空");
            }
            if (product.getPrice() <= 0) {
                throw new RuntimeException("教程价格必须大于 0");
            }
            // 4. 验证教程库存
            if (product.getStock() <= 0) {
                throw new RuntimeException("教程库存必须大于 0");
            }
            // 5. 验证教程图片
            if (StringUtils.isEmpty(product.getImages())) {
                throw new RuntimeException("教程图片不能为空");
            }
            // 6. 验证教程标签
            if (StringUtils.isEmpty(product.getTags())) {
                throw new RuntimeException("教程标签不能为空");
            }
            // 7. 验证教程描述
            if (StringUtils.isEmpty(product.getDescription())) {
                throw new RuntimeException("教程描述不能为空");
            }
            // 8. 验证教程id
            if (product.getId() == null || product.getId() <= 0) {
                throw new RuntimeException("教程id不能为空");
            }
        } else {
            throw new IllegalArgumentException("Invalid request type");
        }

    }

}




