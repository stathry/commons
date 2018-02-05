package org.stathry.commons.utils;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * TODO
 * 
 * @date 2018年1月30日
 */
public class DateUtilsTest {

	private static final String FMT = "yyyy-MM-dd HH:mm:ss";

	@Test
	public void testParse1() throws ParseException {
		Date date = DateUtils.parseDate("2018-01-30", "yyyy-MM-dd");
		String dates = DateFormatUtils.format(date, FMT);
		System.out.println(dates);
		Assert.assertEquals("2018-01-30 00:00:00", dates);

		Date date2 = DateUtils.parseDate("2018-01-30 10:36:00", FMT);
		String dates2 = DateFormatUtils.format(date2, FMT);
		System.out.println(dates2);
		Assert.assertEquals("2018-01-30 10:36:00", dates2);

		Date date3 = DateUtils.parseDate("2018-02-30", "yyyy-MM-dd");
		String dates3 = DateFormatUtils.format(date3, "yyyy-MM-dd");
		System.out.println(dates3);
		Assert.assertEquals("2018-03-02", dates3);
	}

	public void testParseDateStrictly1() throws ParseException {
		Date date2 = DateUtils.parseDateStrictly("2018-01-30 10:36:00", FMT);
		String dates2 = DateFormatUtils.format(date2, FMT);
		System.out.println(dates2);
		Assert.assertEquals("2018-01-30 10:36:00", dates2);
	}

	@Test(expected = ParseException.class)
	public void testParseDateStrictly2() throws ParseException {
		Date date3 = DateUtils.parseDateStrictly("2018-02-30", "yyyy-MM-dd");
		String dates3 = DateFormatUtils.format(date3, "yyyy-MM-dd");
		System.out.println(dates3);
		Assert.assertEquals("2018-03-02", dates3);
	}

	@Test
	public void testFormat() throws ParseException {
		String dates = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
		System.out.println(dates);
		Assert.assertEquals("2018-01-30", dates);
	}

}
