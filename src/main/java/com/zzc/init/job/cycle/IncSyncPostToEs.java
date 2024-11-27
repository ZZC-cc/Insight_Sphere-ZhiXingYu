package com.zzc.init.job.cycle;


import com.zzc.init.admin.post.service.impl.PostServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 增量同步帖子到 es
 */
// todo 取消注释开启任务
@Component
@Slf4j
public class IncSyncPostToEs {

    @Resource
    private PostServiceImpl postService;


    /**
     * 每分钟执行一次
     */
    @Scheduled(fixedRate = 60 * 1000)
    public void run() {
        postService.syncIncrementalPostsToEs();
    }
}
