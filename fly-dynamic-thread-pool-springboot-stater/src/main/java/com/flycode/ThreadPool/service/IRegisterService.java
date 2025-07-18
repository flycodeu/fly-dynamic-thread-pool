package com.flycode.ThreadPool.service;

import com.flycode.ThreadPool.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * 注册中心接口
 *
 * @author flycode
 */
public interface IRegisterService {

    /**
     * 将线程池列表写入注册中心
     * @param threadPoolConfigList
     */
    void reportThreadPoolList(List<ThreadPoolConfigEntity> threadPoolConfigList);

    /**
     * 将指定线程池写入注册中心
     * @param threadPoolConfigEntity
     */
    void reportThreadPool(ThreadPoolConfigEntity threadPoolConfigEntity);
}
