package com.flycode.ThreadPool.job;

import com.alibaba.fastjson.JSON;
import com.flycode.ThreadPool.entity.ThreadPoolConfigEntity;
import com.flycode.ThreadPool.service.service.DynamicThreadPoolService;
import com.flycode.ThreadPool.service.service.RedisRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public class ThreadPoolJob {

    private final Logger logger = LoggerFactory.getLogger(ThreadPoolJob.class);

    private final DynamicThreadPoolService dynamicThreadPoolService;

    private final RedisRegisterService redisRegisterService;

    public ThreadPoolJob(DynamicThreadPoolService dynamicThreadPoolService, RedisRegisterService redisRegisterService) {
        this.dynamicThreadPoolService = dynamicThreadPoolService;
        this.redisRegisterService = redisRegisterService;
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void report(){
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.listAllThreadPool();
        redisRegisterService.reportThreadPoolList(threadPoolConfigEntities);
        logger.info("动态线程池配置{}", JSON.toJSONString(threadPoolConfigEntities));

        threadPoolConfigEntities.forEach(threadPoolConfigEntity -> {
            dynamicThreadPoolService.getThreadPoolByThreadPoolName(threadPoolConfigEntity.getThreadPoolName());
            redisRegisterService.reportThreadPool(threadPoolConfigEntity);
            logger.info("线程池配置,线程名{},其余信息{}",threadPoolConfigEntity.getThreadPoolName(),JSON.toJSONString(threadPoolConfigEntity));
        });
    }
}
