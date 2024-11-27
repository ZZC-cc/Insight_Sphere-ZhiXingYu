package com.zzc.init.admin.home.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zzc.init.admin.home.model.entity.Home;
import com.zzc.init.admin.home.model.vo.HomeVO;

/**
 * 网站数据服务
 */
public interface HomeService extends IService<Home> {


    int addVisitCount();

    HomeVO getHomeVO();
}
