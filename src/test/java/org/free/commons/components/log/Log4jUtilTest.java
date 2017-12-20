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

	private static final Logger LOGGER0 = Logger.getLogger(Log4jUtilTest.class);
	private static final Logger LOGGER1 = Log4jUtil.getLogger(Log4jUtilTest.class, "ZMXY");
	private static final Logger LOGGER2 = Log4jUtil.getLogger(Log4jUtilTest.class, "GOOGLE");
	
	@Test
	public void test0() {
		LOGGER0.info("TEST0");
	}

	@Test
	public void test1() {
		LOGGER1.info("TEST1");
	}

	@Test
	public void test2() {
		LOGGER2.info("TEST2");
	}

	@Test
	public void test3() {
		Logger LOGGER3 = Log4jUtil.getLogger(Log4jUtilTest.class, "MS");
		LOGGER3.info("TEST3");
	}
	
	@Test
	public void test4() {
		Log4jUtil.getLoggerByOrg("MS").info("test41");
		Log4jUtil.getLoggerByOrg("MS").info("test42");
	}

}
