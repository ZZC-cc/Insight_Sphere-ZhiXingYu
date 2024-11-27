package com.zzc.init.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 搜索请求
 */
@Data
public class SearchRequest implements Serializable {

    private String searchText;

    private static final long serialVersionUID = 1L;
}