package org.stathry.commons.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 城市数据服务
 * Created by dongdaiming on 2018-10-25 11:21
 */
@Service
public class SystemCacheCityDAO implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemCacheCityDAO.class);

    private static final long CITY_CACHE_MS = TimeUnit.MINUTES.toMillis(30);

    private volatile long lastUpdateTime = System.currentTimeMillis();

    private Set<String> citySet = new HashSet<>(600);

    public Set<String> queryCitySet() {
        ifReloadCities(false);
        return citySet;
    }

    private void ifReloadCities(boolean forceReload) {
        if (forceReload || (System.currentTimeMillis() - lastUpdateTime > CITY_CACHE_MS)) {
            synchronized (this) {
                if (forceReload || (System.currentTimeMillis() - lastUpdateTime > CITY_CACHE_MS)) {
                    // MOCK, TODO MODIFY
                    List<String> list = new ArrayList<>();
//                    Assert.notEmpty(list, "not found city list in table 'dict_prelevel_city'.");
                    LOGGER.info("list cities, size {}.", list.size());
                    if (list == null || list.isEmpty()) {
                        return;
                    }
                    citySet.clear();
                    String city;
                    for (int i = 0, size = list.size(); i < size; i++) {
                        city = list.get(i).trim();
                        citySet.add(city);
                    }
                    lastUpdateTime = System.currentTimeMillis();
                    LOGGER.info("init cached cities, size {}.", list.size());
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ifReloadCities(true);
    }
}
