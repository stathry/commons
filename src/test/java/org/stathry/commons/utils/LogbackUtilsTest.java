package org.stathry.commons.utils;

import ch.qos.logback.classic.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stathry.commons.log.LogbackUtils;

/**
 * TODO
 *
 * @date 2018年1月27日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-context.xml")
public class LogbackUtilsTest {

    @Test
    public void testMain() {
        org.slf4j.Logger logr = LoggerFactory.getLogger(LogbackUtilsTest.class);
        logr.info("mainlog frame {}.", "slf4j");
        logr.debug("mainlog frame {}.", "logback");
    }

    @Test
    public void testOrgLog1() {
        Logger log = LogbackUtils.getLogger(LogbackUtilsTest.class, "GOOGLE");
        log.debug("1hello logback.");
        log.info("1hello");
        log.info("1hello, {}, year {}.", "ddm", 2018);
        Logger log2 = LogbackUtils.getLogger("LogbackUtilsTest2", "GOOGLE");
        log2.info("11hello logback.");
        log2.info("12hello logback.");
    }

    @Test
    public void testOrgLog2() {
        Logger log = LogbackUtils.getLogger(LogbackUtilsTest.class, "ZMXY");
        log.debug("2hello logback.");
        log.info("2hello");
        log.info("2hello, {}, year {}.", "ddm", 2018);
        Logger log2 = LogbackUtils.getLogger("LogbackUtilsTest", "ZMXY");
        log2.info("21hello logback.");
        log2.info("22hello logback.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOrgLog3() {
        Logger log = LogbackUtils.getLogger(LogbackUtilsTest.class, "FB");
        log.debug("hello logback.");
        log.info("hello");
        log.info("hello, {}, year {}.", "ddm", 2018);
    }

}
