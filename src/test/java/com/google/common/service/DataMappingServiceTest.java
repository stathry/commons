package com.google.common.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stathry.commons.model.dto.DataMap;
import org.stathry.commons.model.dto.DataRange;
import org.stathry.commons.service.DataMappingService;

/**
 * DataMappingServiceTest
 * Created by dongdaiming on 2018-12-06 14:48
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring-context.xml" })
public class DataMappingServiceTest {

    @Autowired
    private DataMappingService dataMappingService;

    @Test
    public void testMappingConfig() {
        DataMap dataMap = dataMappingService.dataMap("ZZC_LOAN");
        System.out.println(dataMap);
        Assert.assertNotNull(dataMap);
    }

    @Test
    public void testAllIdRange() {
        DataRange<Long> range = dataMappingService.keyRange(null, "routing_option", "id");
        System.out.println(range);
        Assert.assertNotNull(range);
    }

    @Test
    public void testByDateIdRange() {
        DataRange<Long> range = dataMappingService.keyRange(null, "data_data1", "id", "fdate1", "2018-12-06 18:17:49", "2018-12-06 18:17:52");
        System.out.println(range);
        Assert.assertNotNull(range);
    }

}
