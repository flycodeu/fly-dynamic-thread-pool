package com.flycode.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author flycode
 * 线程池配置
 */
@Component
@Data
@ConfigurationProperties(prefix = "thread.pool.executor.config", ignoreUnknownFields = true)
public class ThreadPoolConfigProperties {
    /**
     * 核心线程数
     */
    private int corePoolSize = 20;
    /**
     * 最大线程数
     */
    private int maximumPoolSize = 100;
    /**
     * 线程存活时间
     */
    private long keepAliveTime = 10L;
    /**
     * 阻塞队列长度
     */
    private int blockQueueSize = 5000;
    /**
     * 丢弃任务方式
     */
    private String policy = "AbortPolicy";
}
