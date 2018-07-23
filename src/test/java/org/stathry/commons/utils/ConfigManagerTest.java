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
		List<String> list = ConfigManager.getObject("log4j.orgCodes", List.class);
		System.out.println(list.size());
		System.out.println(list.get(0));
	}

	@Test
	public void test2() {
		List<Integer> list = ConfigManager.getObject("ints", List.class);
		System.out.println(list.size());
		System.out.println(list.get(1));
	}
	
	@Test
	public void test3() {
		String v = ConfigManager.get("k8");
		System.out.println(v);
	}
	@Test
	public void test4() {
		Map<String, Map<String,String>> m = ConfigManager.getObject("k6", Map.class);
		Assert.assertNotNull(m);
		Assert.assertEquals("BEAN1", m.get("ORG1").get("SERVICE1"));
		
		List<Integer> l = ConfigManager.getObject("k9", List.class);
		Assert.assertTrue(22 == l.get(1));
	}
	

}
