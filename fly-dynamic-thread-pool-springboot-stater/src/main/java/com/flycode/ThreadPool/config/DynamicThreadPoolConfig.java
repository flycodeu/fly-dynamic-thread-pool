package com.flycode.ThreadPool.config;

import com.alibaba.fastjson.JSON;
import com.flycode.ThreadPool.entity.DynamicThreadPoolRedisEntity;
import com.flycode.ThreadPool.service.DynamicThreadPoolService;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @author flycode
 */
@Configuration
public class DynamicThreadPoolConfig {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean("dynamicThreadPoolService")
    public DynamicThreadPoolService dynamicThreadPoolService(ApplicationContext applicationContext, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        String applicationName = applicationContext.getEnvironment().getProperty("spring.application.name");
        if (applicationName == null) {
            logger.warn("applicationName is null");
            applicationName = "default";
        }
        logger.info("当前应用:{}, threadPoolInfo:{}", applicationName, JSON.toJSONString(threadPoolExecutorMap));
        return new DynamicThreadPoolService(applicationName, threadPoolExecutorMap);
    }


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
}
