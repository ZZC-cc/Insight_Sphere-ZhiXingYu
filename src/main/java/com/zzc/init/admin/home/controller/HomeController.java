package com.zzc.init.admin.home.controller;

import com.zzc.init.admin.home.model.vo.HomeVO;
import com.zzc.init.admin.home.service.HomeService;
import com.zzc.init.common.BaseResponse;
import com.zzc.init.common.ErrorCode;
import com.zzc.init.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页接口
 */
@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

    @Autowired
    HomeService homeService;

    /**
     * 添加访问量
     */
    @GetMapping("/add")
    public BaseResponse<String> addView() {
        int num = homeService.addVisitCount();
        if (num > 0) {
            return ResultUtils.success("第" + num + "次访问");
        } else {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "访问量添加失败");
        }
    }

    /**
     * 获取首页数据
     */
    @GetMapping("/get/vo")
    public BaseResponse<HomeVO> getHome() {
        HomeVO homeVO = homeService.getHomeVO();
        return ResultUtils.success(homeVO);
    }

}
