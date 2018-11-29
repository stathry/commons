package org.stathry.commons.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stathry.commons.bean.RedisManager;
import org.stathry.commons.utils.Snowflake;

/**
 * 订单号生成
 * Created by dongdaiming on 2018-11-07 10:17
 */
@Service
public class OrderService implements InitializingBean {

    @Autowired
    private RedisManager redisManager;

    private Snowflake snowflake;

    public String order() {
        return String.valueOf(snowflake.nextId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        int dataCenterId = Math.abs(redisManager.increment("app.inc.order").intValue()) % 31;
//        snowflake = new Snowflake(dataCenterId, 1);
    }
}
