package com.flycode;

import com.flycode.ThreadPool.config.DynamicThreadPoolConfig;
import com.flycode.ThreadPool.entity.ThreadPoolConfigEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RTopic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

@SpringBootTest
@ActiveProfiles("local")
@Slf4j
public class ApiTest {

    @Resource
    private RTopic threadPoolConfigListener;


    @Test
    public void test() throws InterruptedException {
        ThreadPoolConfigEntity threadPoolConfig = new ThreadPoolConfigEntity();
        threadPoolConfig.setApplicationName("thread-pool-test");
        threadPoolConfig.setThreadPoolName("threadPool01");
        threadPoolConfig.setMaximumPoolSize(1000);
        threadPoolConfig.setCorePoolSize(200);
        long isPublished = threadPoolConfigListener.publish(threadPoolConfig);
        log.info("isPublished:{}", isPublished);
        new CountDownLatch(1).await();
    }
}
