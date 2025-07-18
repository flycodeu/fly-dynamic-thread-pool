package com.flycode.ThreadPool.listener;

import com.flycode.ThreadPool.entity.ThreadPoolConfigEntity;
import com.flycode.ThreadPool.service.IRegisterService;
import com.flycode.ThreadPool.service.service.DynamicThreadPoolService;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * 动态线程池监听窗口
 *
 * @author flycode
 */
public class ThreadPoolListener implements MessageListener<ThreadPoolConfigEntity> {
    private final Logger logger = LoggerFactory.getLogger(ThreadPoolListener.class);

    private final DynamicThreadPoolService dynamicThreadPoolService;

    private final IRegisterService redisRegisterService;

    public ThreadPoolListener(DynamicThreadPoolService dynamicThreadPoolService, IRegisterService redisRegisterService) {
        this.dynamicThreadPoolService = dynamicThreadPoolService;
        this.redisRegisterService = redisRegisterService;
    }

    @Override
    public void onMessage(CharSequence charSequence, ThreadPoolConfigEntity threadPoolConfigEntity) {
        String threadPoolName = threadPoolConfigEntity.getThreadPoolName();
        logger.info("动态线程池监听: threadPoolName={}", threadPoolName);
        dynamicThreadPoolService.updateThreadPool(threadPoolConfigEntity);

        // 所有线程池注册到注册中心
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.listAllThreadPool();
        redisRegisterService.reportThreadPoolList(threadPoolConfigEntities);

        // 将指定线程池注册到注册中心
        ThreadPoolConfigEntity threadPool = dynamicThreadPoolService.getThreadPoolByThreadPoolName(threadPoolName);
        redisRegisterService.reportThreadPool(threadPool);
    }
}
