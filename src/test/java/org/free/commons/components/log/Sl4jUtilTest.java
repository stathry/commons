package org.free.commons.components.log;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerAdapter;

/**
 * TODO
 * 
 * @author dongdaiming
 * @date 2017年12月20日
 */
public class Sl4jUtilTest {

	@Test
	public void test0() {
		LoggerFactory.getLogger(Sl4jUtilTest.class).info("test0");
	}
	@Test
	public void test1() {
		Slf4jUtil.getLoggerByOrg("ZMXY").info("test1");
	}

	@Test
	public void test2() {
		Slf4jUtil.getLoggerByOrg("GOOGLE").info("test2");
	}

	@Test(expected = IllegalArgumentException.class)
	public void test3() {
		Slf4jUtil.getLoggerByOrg("MS").info("test3");
	}

}
