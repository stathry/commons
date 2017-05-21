/*
 * Copyright © PING AN INSURANCE (GROUP) COMPANY OF CHINA ，LTD. All Rights Reserved
 */
package com.pingan.commons.components.utils.codec;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author dongdaiming911@pingan.com
 * @date 2017年5月21日
 */
public class Base64UtilsTest {

	@Test
	public void test() {
		String s = Base64Utils.encode("abc");
		System.out.println(s);
		assertNotNull(s);
		assertNotEquals("", s);
	}
	
	@Test
	public void test2() {
		String s = Base64Utils.encode("abc");
		String s1 = Base64Utils.decode(s);
		System.out.println(s1);
		assertNotNull(s1);
		assertNotEquals("", s1);
	}

}
