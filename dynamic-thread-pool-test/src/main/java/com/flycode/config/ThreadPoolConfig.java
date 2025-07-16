package com.flycode.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

/**
 * @author flycode
 */
@Slf4j
@EnableAsync
@Configuration
@EnableConfigurationProperties({ThreadPoolConfigProperties.class})
public class ThreadPoolConfig {

    @Bean("threadPool01")
    public ThreadPoolExecutor threadPool01(ThreadPoolConfigProperties threadPoolConfigProperties) {
        RejectedExecutionHandler rejectedExecutionHandler;
        switch (threadPoolConfigProperties.getPolicy()) {
            case "AbortPolicy":
                rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
                break;
            case "DiscardPolicy":
                rejectedExecutionHandler = new ThreadPoolExecutor.DiscardPolicy();
                break;
            case "DiscardOldestPolicy":
                rejectedExecutionHandler = new ThreadPoolExecutor.DiscardOldestPolicy();
                break;
            default:
                rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
        }

        // 创建线程池
        return new ThreadPoolExecutor(
                threadPoolConfigProperties.getCorePoolSize(),
                threadPoolConfigProperties.getMaximumPoolSize(),
                threadPoolConfigProperties.getKeepAliveTime(),
                TimeUnit.MINUTES,
                new LinkedBlockingDeque<>(threadPoolConfigProperties.getBlockQueueSize()),
                Executors.defaultThreadFactory(),
                rejectedExecutionHandler
        );
    }


    @Bean("threadPool02")
    public ThreadPoolExecutor threadPool02(ThreadPoolConfigProperties threadPoolConfigProperties) {
        RejectedExecutionHandler rejectedExecutionHandler;
        switch (threadPoolConfigProperties.getPolicy()) {
            case "AbortPolicy":
                rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
                break;
            case "DiscardPolicy":
                rejectedExecutionHandler = new ThreadPoolExecutor.DiscardPolicy();
                break;
            case "DiscardOldestPolicy":
                rejectedExecutionHandler = new ThreadPoolExecutor.DiscardOldestPolicy();
                break;
            default:
                rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
        }

        // 创建线程池
        return new ThreadPoolExecutor(
                threadPoolConfigProperties.getCorePoolSize(),
                threadPoolConfigProperties.getMaximumPoolSize(),
                threadPoolConfigProperties.getKeepAliveTime(),
                TimeUnit.MINUTES,
                new LinkedBlockingDeque<>(threadPoolConfigProperties.getBlockQueueSize()),
                Executors.defaultThreadFactory(),
                rejectedExecutionHandler
        );
    }
}
