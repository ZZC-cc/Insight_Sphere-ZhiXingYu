package com.zzc.init.admin.website.model.vo;

import com.zzc.init.admin.website.model.entity.Feature;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class WebsiteVO {
    private Long id;
    private String site_name;
    private String site_description;
    private String logo_url;
    private String footer_info;
    private Integer visit_count;
    private String cover_url;
    private String contact_address;
    private String contact_email;
    private String contact_phone;
    private String contact_name;

    @ApiModelProperty("网站特点描述")
    private List<Feature> features;

//    private List<NavMenuVO> navMenus;
}
