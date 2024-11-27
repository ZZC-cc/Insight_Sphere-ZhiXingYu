package com.zzc.init.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.zzc.init.admin.post.model.entity.Post;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PostTagUtils {
    // 获取全部标签名称
    public List<String> getAllTags(List<Post> posts) {
        Set<String> tagSet = new HashSet<>();

        // 遍历所有帖子
        for (Post post : posts) {
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
