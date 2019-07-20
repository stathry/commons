package org.testframework.testng;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

/**
 * TestNGRunOrderTest
 * Created by dongdaiming on 2018-12-26 17:00
 */
public class TestNGRunOrderTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestNGRunOrderTest.class);

    private static Map<String, Date> data = new LinkedHashMap<>();
    private static LongAdder counter = new LongAdder();

    private static void putData(String mainKey) {
        counter.add(1);
        data.put(mainKey + "-" + counter.intValue(), new Date());
    }

    @BeforeClass
    public void globalInit() {
        LOGGER.info("run globalInit...");
        putData("globalInit");
    }

    @BeforeMethod
    public void methodInit() {
        LOGGER.info("run methodInit...");
        putData("methodInit");
    }

    @Test
    public void testMethod1() {
        LOGGER.info("run testMethod1...");
        putData("testMethod1");
    }

    @Test
    public void testMethod2() {
        LOGGER.info("run testMethod2...");
        putData("testMethod2");
    }

    @AfterMethod
    public void methodDestroy() {
        LOGGER.info("run methodDestroy...");
        putData("methodDestroy");
    }

    @AfterClass
    public static void globalDestroy() {
        LOGGER.info("run globalDestroy...");
        putData("globalDestroy");
        System.out.println(JSON.toJSONStringWithDateFormat(data, "yyyy-MM-dd HH:mm:ss.SSS"));
    }
}
