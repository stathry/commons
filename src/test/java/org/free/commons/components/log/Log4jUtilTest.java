package org.free.commons.components.log;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * TODO
 * 
 * @author dongdaiming
 * @date 2017年12月20日
 */
public class Log4jUtilTest {

	@Test
	public void test0() {
		Logger.getLogger(Log4jUtilTest.class).info("test0");
	}
	@Test
	public void test1() {
		Log4jUtil.getLoggerByOrg("ZMXY").info("test1");
	}

	@Test
	public void test2() {
		Log4jUtil.getLoggerByOrg("GOOGLE").info("test2");
	}

	@Test(expected = IllegalArgumentException.class)
	public void test3() {
		Log4jUtil.getLoggerByOrg("MS").info("test3");
	}

}
