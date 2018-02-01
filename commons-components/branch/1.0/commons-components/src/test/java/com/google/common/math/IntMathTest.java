package com.google.common.math;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * TODO
 * @date 2018年1月31日
 */
public class IntMathTest {

	@Test
	public void test() {
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

}
