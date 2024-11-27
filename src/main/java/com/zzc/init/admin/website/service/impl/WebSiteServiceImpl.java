package com.zzc.init.admin.website.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzc.init.admin.website.model.entity.Feature;
import com.zzc.init.admin.website.model.entity.Website;
import com.zzc.init.admin.website.model.vo.WebsiteVO;
import com.zzc.init.admin.website.service.WebSiteService;
import com.zzc.init.mapper.WebsiteMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务实现
 */
@Service
@Slf4j
public class WebSiteServiceImpl extends ServiceImpl<WebsiteMapper, Website> implements WebSiteService {

    @Override
    public WebsiteVO getWebsiteDetails() {
        QueryWrapper<Website> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", 1);
        Website website = getOne(queryWrapper);
        WebsiteVO websiteVO = new WebsiteVO();
        BeanUtils.copyProperties(website, websiteVO);

        // 解析 JSON
        if (website.getFeatures() != null) {
            List<Feature> features = JSONUtil.toList(website.getFeatures(), Feature.class);
            websiteVO.setFeatures(features);
        }
        return websiteVO;
    }

    @Override
    public boolean updateWebsiteDetails(WebsiteVO websiteVO) {
        Website website = new Website();
        BeanUtils.copyProperties(websiteVO, website);

        // 序列化 JSON
        if (websiteVO.getFeatures() != null) {
            String featuresJson = JSONUtil.toJsonStr(websiteVO.getFeatures());
            website.setFeatures(featuresJson);
        }
        this.updateById(website);
        return updateById(website);
    }

}
