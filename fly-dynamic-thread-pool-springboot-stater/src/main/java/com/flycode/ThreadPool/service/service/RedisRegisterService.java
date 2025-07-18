package com.flycode.ThreadPool.service.service;

import com.flycode.ThreadPool.entity.ThreadPoolConfigEntity;
import com.flycode.ThreadPool.entity.vo.RegisterEnum;
import com.flycode.ThreadPool.service.IRegisterService;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;

import java.util.List;

/**
 * Redis注册中心
 *
 * @author flycode
 */
public class RedisRegisterService implements IRegisterService {
    private final RedissonClient redissonClient;

    public RedisRegisterService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void reportThreadPoolList(List<ThreadPoolConfigEntity> threadPoolConfigList) {
        RList<ThreadPoolConfigEntity> threadPoolList = redissonClient.getList(RegisterEnum.THREAD_POOL_LIST_KEY.getKey());
        threadPoolList.delete();
        threadPoolList.addAll(threadPoolConfigList);
    }

    @Override
    public void reportThreadPool(ThreadPoolConfigEntity threadPoolConfigEntity) {
        RBucket<ThreadPoolConfigEntity> threadPoolBucket = redissonClient.getBucket(RegisterEnum.THREAD_POOL_KEY.getKey() + "_" + threadPoolConfigEntity.getThreadPoolName());
        threadPoolBucket.set(threadPoolConfigEntity);
    }
}
