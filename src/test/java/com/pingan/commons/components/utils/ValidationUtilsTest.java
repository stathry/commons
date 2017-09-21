/*
 * Copyright © PING AN INSURANCE (GROUP) COMPANY OF CHINA ，LTD. All Rights Reserved
 */
package com.pingan.commons.components.utils;

import static org.junit.Assert.*;

import org.free.commons.components.pojo.dto.ValidateResult;
import org.free.commons.components.utils.ValidationUtils;
import org.junit.Test;

import com.pingan.commons.components.convert.Bean3;

/**
 * @author dongdaiming911@pingan.com
 * @date 2017年5月21日
 */
public class ValidationUtilsTest {
	
	@Test
	public void test1() {
		Bean3 b = new Bean3();
		b.setBirth("2017-05-21 18:38:00");
		ValidateResult r = ValidationUtils.validate(b);
		System.out.println(r);
		assertNotNull(r);
		assertTrue(r.isSuccess());
	}

	@Test
	public void test2() {
		Bean3 b = new Bean3();
		ValidateResult r = ValidationUtils.validate(b);
		System.out.println(r);
		assertNotNull(r);
		assertTrue(r.isSuccess());
	}
	
	@Test
	public void test3() {
		Bean3 b = new Bean3();
		b.setBirth("2017-05-41 18:38:00");
		ValidateResult r = ValidationUtils.validate(b);
		System.out.println(r);
		assertNotNull(r);
		assertFalse(r.isSuccess());
	}
	
	@Test
	public void test4() {
		Bean3 b = new Bean3();
		b.setBirth("2017");
		ValidateResult r = ValidationUtils.validate(b);
		System.out.println(r);
		assertNotNull(r);
		assertFalse(r.isSuccess());
		System.out.println("getFailedItems:" + r.getFailedItems());
	}
	
	@Test
	public void test5() {
		Bean3 b = new Bean3();
		b.setBirth("2017-05-41 18:38:00");
		ValidateResult r = ValidationUtils.validate(b, true);
		System.out.println(r);
		assertNotNull(r);
		assertFalse(r.isSuccess());
		System.out.println("getFailedDetailItems:" + r.getFailedDetailItems());
	}
	
}
