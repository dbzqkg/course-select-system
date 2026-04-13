package com.lzh.server;

import org.junit.jupiter.api.Test;
import org.redisson.RedissonLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedissonTest {
    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void testRedissonLock() {
        RLock myLock = redissonClient.getLock("myLock");

    }
}
