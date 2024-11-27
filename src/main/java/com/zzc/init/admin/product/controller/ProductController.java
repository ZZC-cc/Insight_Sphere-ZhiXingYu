package com.zzc.init.admin.product.controller;

import com.zzc.init.admin.product.model.dto.ProductAddRequest;
import com.zzc.init.admin.product.model.dto.ProductUpdateRequest;
import com.zzc.init.admin.product.model.vo.ProductVO;
import com.zzc.init.admin.product.service.ProductService;
import com.zzc.init.annotation.AuthCheck;
import com.zzc.init.common.BaseResponse;
import com.zzc.init.common.DeleteRequest;
import com.zzc.init.common.ErrorCode;
import com.zzc.init.common.ResultUtils;
import com.zzc.init.constant.UserConstant;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 教程接口
 */
@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {

    @Resource
    private ProductService productService;

    /**
     * 添加教程
     */
    @PostMapping("/add")
    @Operation(summary = "添加教程")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> addProduct(@RequestBody ProductAddRequest addRequest) {
        boolean b = productService.addProduct(addRequest);
        if (!b) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "添加教程失败");
        }
        return ResultUtils.success("添加教程成功");
    }

    /**
     * 更新教程
     */
    @PostMapping("/update")
    @Operation(summary = "更新教程")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> updateProduct(@RequestBody ProductUpdateRequest updateRequest) {
        boolean b = productService.updateProduct(updateRequest);
        if (!b) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "更新教程失败");
        }
        return ResultUtils.success("更新教程成功");
    }

    /**
     * 删除教程
     */
    @PostMapping("/delete")
    @Operation(summary = "删除教程")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> deleteProduct(@RequestBody DeleteRequest deleteRequest) {
        boolean b = productService.removeById(deleteRequest.getId());
        if (!b) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "删除教程失败");
        }
        return ResultUtils.success("删除教程成功");
    }

    /**
     * 获取所有教程VO列表
     */
    @PostMapping("/get/all/vo")
    @Operation(summary = "获取所有教程VO列表")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<ProductVO>> getAllProductVO(HttpServletRequest request) {
        List<ProductVO> productVOS = productService.getAllProducts(request);
        return ResultUtils.success(productVOS);
    }

    /**
     * 切换教程上下架状态
     */
    @GetMapping("/change/shelves")
    @Operation(summary = "切换教程上下架状态")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> changeShelves(@RequestParam Long id) {
        String res = productService.changeShelvesStatus(id);
        if (res.isEmpty()) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "操作失败");
        }
        return ResultUtils.success(res);
    }

    /**
     * 多类型搜索
     */
    @GetMapping("/search")
    @Operation(summary = "多类型搜索")
    public BaseResponse<List<ProductVO>> searchProductBySearchText(@RequestParam(value = "searchText", required = false) String searchText) {
        List<ProductVO> productVOS = productService.searchProductBySearchText(searchText);
        return ResultUtils.success(productVOS);
    }

    /**
     * 获取全部标签名称
     */
    @GetMapping("/get/all/tags")
    @Operation(summary = "获取全部标签名称")
    public BaseResponse<List<String>> getAllTags() {
        List<String> tags = productService.getAllTags();
        return ResultUtils.success(tags);
    }

    /**
     * 通过标签名称获取教程列表
     */
    @GetMapping("/get/products/by/tags")
    @Operation(summary = "通过标签名称获取教程列表")
    public BaseResponse<List<ProductVO>> getProductsByTags(@RequestParam(value = "tags", required = false) String tags) {
        List<ProductVO> productVOS = productService.getProductsByTags(tags);
        return ResultUtils.success(productVOS);
    }

    /**
     * 获取教程详情
     */
    @GetMapping("/get/vo")
    @Operation(summary = "获取教程详情")
    public BaseResponse<ProductVO> getProductById(@RequestParam Long id, HttpServletRequest request) {
        ProductVO productVO = productService.getProductById(id, request);
        return ResultUtils.success(productVO);
    }

}
