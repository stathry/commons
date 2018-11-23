package com.google.common.math;

import com.google.common.primitives.Primitives;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * TODO
 *
 * @date 2018年1月31日
 */
public class IntTest {

    @Test
    public void testIntMath() {
        int n1 = (int) Math.pow(2, 10);
        int n2 = IntMath.pow(2, 10);
        System.out.println(n1);
        assertEquals(1024, n1);
        assertEquals(1024, n2);
        long n3 = (long) Math.pow(10, 8);
        long n4 = IntMath.pow(10, 8);
        System.out.println(n3);
        assertEquals(10000_0000, n3);
        assertEquals(10000_0000, n4);
    }

    @Test
    public void testPrimitives1() {
        Set<?> pTypes = Primitives.allPrimitiveTypes();
        System.out.println(pTypes);
        Assert.assertTrue(pTypes.contains(int.class));

        Set<?> wTypes = Primitives.allWrapperTypes();
        System.out.println(wTypes);
        Assert.assertTrue(wTypes.contains(Integer.class));

    }

    @Test
    public void testPrimitives2() {
        Assert.assertEquals(Integer.class, Primitives.wrap(int.class));
        Assert.assertEquals(int.class, Primitives.unwrap(Integer.class));
    }
}
