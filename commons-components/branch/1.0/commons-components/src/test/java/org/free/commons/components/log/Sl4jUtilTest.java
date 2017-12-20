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
		Slf4jUtil.getLogger(Sl4jUtilTest.class).info("test0");
		Slf4jUtil.getLogger(Sl4jUtilTest.class).info("test0");
	}

	@Test
	public void test1() {
		Slf4jUtil.getLoggerByOrg("MS").info("MS test1 p1 {}.", 1);
		Slf4jUtil.getLoggerByOrg("MS").info("MS test1 p1 {}.", 2);
		Slf4jUtil.getLoggerByOrg("ZMXY").info("ZMXY test1 p1 {}.", 1);
		Slf4jUtil.getLoggerByOrg("ZMXY").info("ZMXY test1 p1 {}.", 2);
	}
	@Test
	public void test2() {
		Slf4jUtil.getLoggerByOrg(Sl4jUtilTest.class, "MS").info("MS test2 p1 {}.", 1);
		Slf4jUtil.getLoggerByOrg(Sl4jUtilTest.class, "MS").info("MS test2 p1 {}.", 2);
		Slf4jUtil.getLoggerByOrg(Sl4jUtilTest.class.getName(), "ZMXY").info("ZMXY test2 p1 {}.", 1);
		Slf4jUtil.getLoggerByOrg(Sl4jUtilTest.class.getName(), "ZMXY").info("ZMXY test2 p1 {}.", 2);
	}

}
