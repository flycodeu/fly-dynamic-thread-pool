package com.flycode.ThreadPool.entity.vo;


/**
 * Redis的key集合
 *
 * @author flycode
 */

public enum RegisterEnum {
    THREAD_POOL_LIST_KEY("THREAD_POOL_LIST_KEY", "线程池组key"),
    THREAD_POOL_KEY("THREAD_POOL_KEY", "指定线程池的key"),
    THREAD_POOL_REDIS_TOPIC("THREAD_POOL_REDIS_TOPIC","redis 的topic key")
    ;
    private String key;
    private String value;

    RegisterEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
