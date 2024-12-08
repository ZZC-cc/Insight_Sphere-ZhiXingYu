package com.zzc.init.admin.home.controller;

import com.zzc.init.admin.home.model.vo.HomeVO;
import com.zzc.init.admin.home.service.HomeService;
import com.zzc.init.common.BaseResponse;
import com.zzc.init.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 首页接口
 */
@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

    @Resource
    HomeService homeService;

    /**
     * 获取首页数据
     */
    @GetMapping("/get/vo")
    public BaseResponse<HomeVO> getHome() {
        HomeVO homeVO = homeService.getHomeVO();
        return ResultUtils.success(homeVO);
    }

}
