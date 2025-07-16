package com.flycode.ThreadPool.config;

import com.alibaba.fastjson.JSON;
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
    public String dynamicThreadPoolService(ApplicationContext applicationContext, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        String applicationName = applicationContext.getEnvironment().getProperty("spring.application.name");
        if (applicationName == null) {
            logger.warn("applicationName is null");
            applicationName = "default";
        }
        String jsonString = JSON.toJSONString(threadPoolExecutorMap);
        logger.info(jsonString);
        return new String();
    }
}
