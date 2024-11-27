package com.zzc.init.admin.website.controller;

import com.zzc.init.admin.website.model.vo.WebsiteVO;
import com.zzc.init.admin.website.service.WebSiteService;
import com.zzc.init.common.BaseResponse;
import com.zzc.init.common.ErrorCode;
import com.zzc.init.common.ResultUtils;
import com.zzc.init.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/website")
@Slf4j
public class WebSiteController {
    @Autowired
    private WebSiteService websiteService;

    @GetMapping("/details")
    public BaseResponse<WebsiteVO> getWebsiteDetails() {
        WebsiteVO websiteDetails = websiteService.getWebsiteDetails();
        return ResultUtils.success(websiteDetails);
    }


    @PutMapping("/update")
    public BaseResponse<String> updateWebsiteDetails(@RequestBody WebsiteVO websiteVO) {
        boolean result = websiteService.updateWebsiteDetails(websiteVO);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新失败");
        }
        return ResultUtils.success("更新成功");
    }

}
