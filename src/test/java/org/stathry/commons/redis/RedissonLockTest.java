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
/*        Config conf = new Config();
        conf.setTransportMode(TransportMode.EPOLL);
        conf.useClusterServers().addNodeAddress("redis://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(conf);
        System.out.println(redisson);*/
    }

    @Test
    public void testInit2() {
/*        Config conf = new Config();
        conf. useSingleServer().setAddress("127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(conf);
        Assert.assertNotNull(redisson);
        System.out.println(redisson);*/
    }

}
