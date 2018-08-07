/*
 * Copyright © stathry@126.com All Rights Reserved
 */
package org.stathry.commons.utils.codec;

import static org.junit.Assert.*;

import org.junit.Test;
import org.stathry.commons.utils.codec.Base64Utils;

/**
 * @author stathry@126.com
 * @date 2017年5月21日
 */
public class Base64UtilsTest {

	@Test
	public void test() {
		String s = Base64Utils.encode("abc");
		System.out.println(s);
		assertNotNull(s);
		assertNotEquals("", s);
		assertEquals("YWJj", s);
	}
	
	@Test
	public void test2() {
		String s = Base64Utils.encode("abc");
        System.out.println(s);
		String s1 = Base64Utils.decode(s);
		System.out.println(s1);
		assertNotNull(s1);
		assertNotEquals("", s1);
	}

}
