package com.flycode.ThreadPool.service;

import com.flycode.ThreadPool.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * 动态线程池接口,提供三个接口
 *
 * @author flycode
 */
public interface IDynamicThreadPoolService {

    /**
     * 展示所有的动态线程池信息
     *
     * @return
     */
    List<ThreadPoolConfigEntity> listAllThreadPool();

    /**
     * 根据线程池名称查找线程
     *
     * @param threadPoolName
     * @return
     */
    ThreadPoolConfigEntity getThreadPoolByThreadPoolName(String threadPoolName);

    /**
     * 根据线程池名称等信息修改指定线程池
     * @param threadPoolConfigEntity
     * @return
     */
    void updateThreadPool(ThreadPoolConfigEntity threadPoolConfigEntity);
}
