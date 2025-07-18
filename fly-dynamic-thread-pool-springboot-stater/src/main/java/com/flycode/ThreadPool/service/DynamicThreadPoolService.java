package com.flycode.ThreadPool.service;

import com.flycode.ThreadPool.entity.ThreadPoolConfigEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 动态线程池接口实现类
 *
 * @author flycode
 */
public class DynamicThreadPoolService implements IDynamicThreadPoolService {
    private final Logger logger = LoggerFactory.getLogger(DynamicThreadPoolService.class);
    /**
     * 应用名称
     */
    private final String applicationName;

    /**
     * 当前所有的线程池,map格式
     */
    private final Map<String, ThreadPoolExecutor> threadPoolExecutorMap;

    public DynamicThreadPoolService(String applicationName, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        this.applicationName = applicationName;
        this.threadPoolExecutorMap = threadPoolExecutorMap;
    }

    @Override
    public List<ThreadPoolConfigEntity> listAllThreadPool() {
        List<ThreadPoolConfigEntity> listAllThreadPool = new ArrayList<>();
        Set<String> threadPoolNames = threadPoolExecutorMap.keySet();
        for (String threadPoolName : threadPoolNames) {
            ThreadPoolConfigEntity threadPoolByThreadPoolName = getThreadPoolByThreadPoolName(threadPoolName);
            listAllThreadPool.add(threadPoolByThreadPoolName);
        }
        return listAllThreadPool;
    }

    @Override
    public ThreadPoolConfigEntity getThreadPoolByThreadPoolName(String threadPoolName) {
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolName);
        if (threadPoolExecutor != null) {
            ThreadPoolConfigEntity threadPoolConfigEntity = new ThreadPoolConfigEntity();
            threadPoolConfigEntity.setApplicationName(applicationName);
            threadPoolConfigEntity.setThreadPoolName(threadPoolName);
            threadPoolConfigEntity.setCorePoolSize(threadPoolExecutor.getCorePoolSize());
            threadPoolConfigEntity.setMaximumPoolSize(threadPoolExecutor.getMaximumPoolSize());
            threadPoolConfigEntity.setActiveCount(threadPoolExecutor.getActiveCount());
            threadPoolConfigEntity.setPoolSize(threadPoolExecutor.getPoolSize());
            threadPoolConfigEntity.setQueueType(threadPoolExecutor.getQueue().getClass().getSimpleName());
            threadPoolConfigEntity.setQueueSize(threadPoolExecutor.getQueue().size());
            threadPoolConfigEntity.setRemainCapacity(threadPoolExecutor.getQueue().remainingCapacity());
            return threadPoolConfigEntity;
        }
        return null;
    }

    @Override
    public void updateThreadPool(ThreadPoolConfigEntity threadPoolConfigEntity) {
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolConfigEntity.getThreadPoolName());
        if (threadPoolExecutor == null) {
            return;
        }

        int newMaximumPoolSize = threadPoolConfigEntity.getMaximumPoolSize();
        int newCorePoolSize = threadPoolConfigEntity.getCorePoolSize();
        if (newMaximumPoolSize < newCorePoolSize) {
            logger.error("核心线程数: " + newCorePoolSize + "超过了最大线程数: " + newMaximumPoolSize);
            return;
        }
        threadPoolExecutor.setMaximumPoolSize(newMaximumPoolSize);
        threadPoolExecutor.setCorePoolSize(newCorePoolSize);
    }
}
