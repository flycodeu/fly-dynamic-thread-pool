package com.flycode.ThreadPool.config;

import com.alibaba.fastjson.JSON;
import com.flycode.ThreadPool.entity.DynamicThreadPoolRedisEntity;
import com.flycode.ThreadPool.entity.ThreadPoolConfigEntity;
import com.flycode.ThreadPool.entity.vo.RegisterEnum;
import com.flycode.ThreadPool.job.ThreadPoolJob;
import com.flycode.ThreadPool.listener.ThreadPoolListener;
import com.flycode.ThreadPool.service.service.DynamicThreadPoolService;
import com.flycode.ThreadPool.service.service.RedisRegisterService;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @author flycode
 */
@Configuration
@EnableScheduling
public class DynamicThreadPoolConfig {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    String applicationName;

    /**
     * 动态线程池配置类
     *
     * @param applicationContext
     * @param threadPoolExecutorMap
     * @return
     */
    @Bean("dynamicThreadPoolService")
    public DynamicThreadPoolService dynamicThreadPoolService(ApplicationContext applicationContext, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        applicationName = applicationContext.getEnvironment().getProperty("spring.application.name");
        if (applicationName == null) {
            logger.warn("applicationName is null");
            applicationName = "default";
        }
        logger.info("当前应用:{}, threadPoolInfo:{}", applicationName, JSON.toJSONString(threadPoolExecutorMap));
        return new DynamicThreadPoolService(applicationName, threadPoolExecutorMap);
    }

    /**
     * 注册redis
     *
     * @param dynamicThreadPoolRedisEntity
     * @return
     */
    @Bean("redisClient")
    public RedissonClient redissonClient(DynamicThreadPoolRedisEntity dynamicThreadPoolRedisEntity) {
        Config config = new Config();
        config.setCodec(JsonJacksonCodec.INSTANCE);
        config.useSingleServer()
                .setAddress(String.format("redis://%s:%s", dynamicThreadPoolRedisEntity.getHost(), dynamicThreadPoolRedisEntity.getPort()))
                .setDatabase(dynamicThreadPoolRedisEntity.getDatabase())
                .setKeepAlive(dynamicThreadPoolRedisEntity.isKeepAlive())
                .setRetryAttempts(dynamicThreadPoolRedisEntity.getRetryAttempts())
                .setPingConnectionInterval(dynamicThreadPoolRedisEntity.getPingInterval())
                .setConnectTimeout(dynamicThreadPoolRedisEntity.getConnectTimeout())
                .setPassword(dynamicThreadPoolRedisEntity.getPassword())
                .setConnectionMinimumIdleSize(dynamicThreadPoolRedisEntity.getMinIdleSize())
        ;

        RedissonClient redissonClient = Redisson.create(config);
        logger.info("动态线程池注册中心Redis Host: {},端口：{}启动成功", dynamicThreadPoolRedisEntity.getHost(), dynamicThreadPoolRedisEntity.getPort());
        return redissonClient;
    }


    /**
     * 注册中心配置
     *
     * @param redissonClient
     * @return
     */
    @Bean("redisRegister")
    public RedisRegisterService redisRegisterService(RedissonClient redissonClient) {
        return new RedisRegisterService(redissonClient);
    }

    /**
     * 线程池监听
     *
     * @param dynamicThreadPoolService
     * @param redisRegisterService
     * @return
     */
    @Bean
    public ThreadPoolListener threadPoolListener(DynamicThreadPoolService dynamicThreadPoolService, RedisRegisterService redisRegisterService) {
        return new ThreadPoolListener(dynamicThreadPoolService, redisRegisterService);
    }

    /**
     * 动态线程池任务
     *
     * @param dynamicThreadPoolService
     * @param redisRegisterService
     * @return
     */
    @Bean
    public ThreadPoolJob threadPoolJob(DynamicThreadPoolService dynamicThreadPoolService, RedisRegisterService redisRegisterService) {
        return new ThreadPoolJob(dynamicThreadPoolService, redisRegisterService);
    }

    /**
     * Redis 发布订阅功能
     * @param redissonClient
     * @param threadPoolListener
     * @return
     */
    @Bean
    public RTopic threadPoolConfigListener(RedissonClient redissonClient, ThreadPoolListener threadPoolListener) {
        String key = RegisterEnum.THREAD_POOL_REDIS_TOPIC.getKey() + "_" + applicationName;
        RTopic topic = redissonClient.getTopic(key);
        topic.addListener(ThreadPoolConfigEntity.class, threadPoolListener);
        return topic;
    }


}
