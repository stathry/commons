package org.stathry.commons.utils;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.stathry.commons.utils.ConfigManager;

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
	@Test
	public void test4() {
		Map<String, Map<String,String>> m = ConfigManager.get("k6", Map.class);
		Assert.assertNotNull(m);
		Assert.assertEquals("BEAN1", m.get("ORG1").get("SERVICE1"));
		
		List<Integer> l = ConfigManager.get("k9", List.class);
		Assert.assertTrue(22 == l.get(1));
	}
	

}
