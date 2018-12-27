package org.apache.commons.lang3;

import org.junit.Test;

import java.util.Arrays;

/**
 * TODO
 * Created by dongdaiming on 2018-07-13 12:23
 */
public class ArrayTest {

    @Test
    public void testArrayCopy() {
        String[] a = "case2,employee_info2,employee_info2".split(",");
        String[] a1 = Arrays.copyOfRange(a, 1, a.length);
        System.out.println(Arrays.toString(a1));
    }

    @Test
    public void testContains() {
        System.out.println(ArrayUtils.contains("a,b,c".split(","), "a"));
        System.out.println(ArrayUtils.contains("a,b,c".split(","), "x"));
        System.out.println(ArrayUtils.contains("a,b,c".split(","), ""));
    }
}
