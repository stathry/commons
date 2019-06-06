package org.teststandard.testng;


import org.apache.commons.lang3.time.DateFormatUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;

/**
 * FeatureTest
 * Created by dongdaiming on 2018-12-27 09:13
 */
public class TestNGFeatureTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void testExpectedException() {
        int n = 1 / ((Integer)null);
    }

    @Test(enabled = false)
    public void testIgnoreTest() {
        System.out.println("testIgnoreTest");
    }

    @Test(timeOut = 5_000)
    public void testNotTimeout() throws InterruptedException {
        Thread.sleep(3_000);
        System.out.println("testNotTimeout");
    }

    @Test(timeOut = 1_000, enabled = false)
    public void testTimeout() throws InterruptedException {
        Thread.sleep(3_000);
        System.out.println("testTimeout");
    }

    @Test(enabled = false, threadPoolSize = 4, invocationCount = 20)
    public void testByThreadPool() {
        System.out.println("testByThreadPool, tid-" + Thread.currentThread().getId());
    }

    @Test(enabled = false)
    public void testDepend1() {
        System.out.println("running " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Assert.fail();
    }

    @Test
    public void testDepend2() {
        System.out.println("running " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test(enabled = false, dependsOnMethods = "testDepend1")
    public void testDepend3() {
        System.out.println("running " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test(dependsOnMethods = "testDepend2")
    public void testDepend4() {
        System.out.println("running " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test(priority = 2)
    public void testPriority2() {
        System.out.println("running " + Thread.currentThread().getStackTrace()[1].getMethodName()
                + ", at " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
    }

    @Test(priority = 3)
    public void testPriority3() {
        System.out.println("running " + Thread.currentThread().getStackTrace()[1].getMethodName()
                + ", at " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
    }

    @Test(priority = 1)
    public void testPriority1() {
        System.out.println("running " + Thread.currentThread().getStackTrace()[1].getMethodName()
                + ", at " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
    }
}
