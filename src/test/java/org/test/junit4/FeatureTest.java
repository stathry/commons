package org.test.junit4;

import org.junit.Ignore;
import org.junit.Test;

/**
 * FeatureTest
 * Created by dongdaiming on 2018-12-27 09:13
 */
public class FeatureTest {

    @Test(expected = NullPointerException.class)
    public void testExpectedException() {
        int n = 1 / ((Integer)null);
    }

    @Ignore
    @Test
    public void testIgnoreTest() {
        System.out.println("testIgnoreTest");
    }

    @Test(timeout = 15_000)
    public void testNotTimeout() throws InterruptedException {
        Thread.sleep(10_000);
        System.out.println("testNotTimeout");
    }

    @Test(timeout = 5_000)
    public void testTimeout() throws InterruptedException {
        Thread.sleep(10_000);
        System.out.println("testTimeout");
    }
}
