/**
 * Copyright © stathry@126.com All Rights Reserved
 */
package org.stathry.commons.convert;

import org.junit.Test;
import org.stathry.commons.utils.DataConvertUtils;


/**
 * @author Demon
 * @date 2016年12月12日
 */
public class DataConvertUtilsTest {

//	@Test
	public void test() throws Exception {

		Bean2 b2 = new Bean2();
		b2.setAge("1");
		
		Bean1 b1 = new Bean1();
		DataConvertUtils.setProperty(b2.getClass().getDeclaredField("age"), b2, b1);
		System.out.println(b1.getAge());
	}
	
	@Test
	public void test2() throws Exception {
		
		Bean2 b2 = new Bean2();
		b2.setAge("1");
		b2.setMoney(88888.123456789d);
		
		Bean1 b1 = new Bean1();
		DataConvertUtils.convertBean(b2, b1);
		System.out.println(b1.getAge());
		System.out.println(b1.getMoney());
	}

}
