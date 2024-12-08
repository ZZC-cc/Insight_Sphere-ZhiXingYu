package com.zzc.init.admin.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzc.init.admin.product.model.dto.ProductAddRequest;
import com.zzc.init.admin.product.model.dto.ProductUpdateRequest;
import com.zzc.init.admin.product.model.entity.Product;
import com.zzc.init.admin.product.model.vo.ProductVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 教程服务
 */
public interface ProductService extends IService<Product> {


    boolean addProduct(ProductAddRequest addRequest);

    List<ProductVO> getAllProducts(HttpServletRequest request);

    boolean updateProduct(ProductUpdateRequest updateRequest);

    String changeShelvesStatus(Long id);

    List<ProductVO> searchProductBySearchText(String searchText);


    List<String> getAllTags();

    ProductVO getProductById(Long id, HttpServletRequest request);

    List<ProductVO> getProductsByTags(String tags);

    List<ProductVO> getVoByNumber(int number);
}
