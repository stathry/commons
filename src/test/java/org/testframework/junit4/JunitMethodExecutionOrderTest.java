package org.testframework.junit4;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JunitMethodExecutionOrderTest
 *
 * @author dongdaiming(董代明)
 * @date 2019-06-13
 */
public class JunitMethodExecutionOrderTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JunitMethodExecutionOrderTest.class);

    @Test
    public void testMethod1() {
        LOGGER.info("run testMethod1");
    }

    @Test
    public void testMethod2() {
        LOGGER.info("run testMethod2");
    }

    @BeforeClass
    public static void beforeClassMethod() {
        LOGGER.info("run beforeClassMethod");
    }

    @Before
    public void beforeMethod() {
        LOGGER.info("run beforeMethod");
    }

    @After
    public void afterMethod() {
        LOGGER.info("run afterMethod");
    }

    @AfterClass
    public static void afterClassMethod() {
        LOGGER.info("run afterClassMethod");
    }
}
