package org.apache.commons.lang3;

import org.junit.Test;

/**
 * TODO
 * Created by dongdaiming on 2018-07-13 12:23
 */
public class ArrayTest {

    @Test
    public void testContains() {
        System.out.println(ArrayUtils.contains("a,b,c".split(","), "a"));
        System.out.println(ArrayUtils.contains("a,b,c".split(","), "x"));
        System.out.println(ArrayUtils.contains("a,b,c".split(","), ""));
    }
}
