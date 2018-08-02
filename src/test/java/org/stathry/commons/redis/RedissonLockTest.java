package org.stathry.commons.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * TvODO
 * Created by dongdaiming on 2018-07-30 10:21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
public class RedissonLockTest {

    @Test
    public void testInit1() {
/*        Config config = new Config();
        config.setTransportMode(TransportMode.EPOLL);
        config.useClusterServers().addNodeAddress("redis://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);
        System.out.println(redisson);*/
    }

    @Test
    public void testInit2() {
/*        Config config = new Config();
        config. useSingleServer().setAddress("127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);
        Assert.assertNotNull(redisson);
        System.out.println(redisson);*/
    }

}
