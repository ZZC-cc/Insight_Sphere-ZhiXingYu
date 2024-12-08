package com.zzc.init.job.once;

import com.zzc.init.admin.ai.service.impl.AIChatMessageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 全量同步帖子到 es
 */
// todo 取消注释开启任务
@Component
@Slf4j
public class FullSyncMessageToRedis implements CommandLineRunner {

    @Resource
    private AIChatMessageServiceImpl aiChatMessageService;

    @Override
    public void run(String... args) {
        aiChatMessageService.syncAllContextsToRedis();
    }
}
