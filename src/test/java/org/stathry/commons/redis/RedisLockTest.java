package org.stathry.commons.redis;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stathry.commons.dao.RedisManager;
import org.stathry.commons.lock.RedisLock;
import org.stathry.commons.pojo.Actor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 * Created by dongdaiming on 2018-07-23 19:23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
public class RedisLockTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test1() {
        RedisLock redisLock = new RedisLock(stringRedisTemplate, "k1");
        int tn = 8;
        ExecutorService exec = Executors.newFixedThreadPool(tn);
        redisLock.lock();

    }

}
