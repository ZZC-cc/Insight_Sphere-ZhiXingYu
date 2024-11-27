package com.zzc.init.admin.website.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzc.init.admin.website.model.entity.Website;
import com.zzc.init.admin.website.model.vo.WebsiteVO;

public interface WebSiteService extends IService<Website> {
    WebsiteVO getWebsiteDetails();

    boolean updateWebsiteDetails(WebsiteVO websiteVO);

}

