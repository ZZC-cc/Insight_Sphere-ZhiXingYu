package com.zzc.init.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.zzc.init.admin.product.model.entity.Product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductTagUtils {
    // 获取全部标签名称
    public List<String> getAllTags(List<Product> products) {
        Set<String> tagSet = new HashSet<>();

        // 遍历所有帖子
        for (Product post : products) {
            // 解析标签字符串
            JSONArray jsonArray = JSONUtil.parseArray(post.getTags());
            List<String> tags = jsonArray.toList(String.class);
            // 添加标签文字到Set中
            tagSet.addAll(tags);
        }

        // 将Set转换为List返回
        return new ArrayList<>(tagSet);
    }
}
