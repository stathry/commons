package org.free.commons.components.utils;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * TODO
 * 
 * @author dongdaiming
 * @date 2018年1月18日
 */
public class ConfigManagerTest {

	@Test
	public void test1() {
		String[] a = ConfigManager.getConfig().getStringArray("log4j.orgCodes");
		System.out.println(a.length);
		System.out.println(a[1]);
	}

	@Test
	public void test2() {
		int[] a = ConfigManager.getIntArray("ints");
		System.out.println(a.length);
		System.out.println(a[1]);
		System.out.println(a[1]);
	}
	
	@Test
	public void test3() {
		Object v = ConfigManager.getConfig().getProperty("k8");
		System.out.println(v);
	}
	

}
