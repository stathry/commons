package org.stathry.commons.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @date 2018年1月30日
 */
public class DatetimeUtilsTest {

    private static final String FMT = "yyyy-MM-dd HH:mm:ss";
    private static final String FMT_MON = "yyyy-MM";
    private static final String FMT_YEAR = "yyyy";

    @Test
    public void testInterval() throws ParseException {
        Assert.assertEquals(-1, yearInterval("2019", "2018"));
        Assert.assertEquals(0, yearInterval("2018", "2018"));
        Assert.assertEquals(1, yearInterval("2017", "2018"));

        Assert.assertEquals(-2, monInterval("2019-01", "2018-11"));
        Assert.assertEquals(-1, monInterval("2018-12", "2018-11"));
        Assert.assertEquals(0, monInterval("2018-11", "2018-11"));
        Assert.assertEquals(1, monInterval("2018-10", "2018-11"));
        Assert.assertEquals(2, monInterval("2018-09", "2018-11"));

        Assert.assertEquals(-1, timeInterval("2018-11-06", "2018-11-05", "yyyy-MM-dd", TimeUnit.DAYS));
        Assert.assertEquals(0, timeInterval("2018-11-05", "2018-11-05", "yyyy-MM-dd", TimeUnit.DAYS));
        Assert.assertEquals(1, timeInterval("2018-11-04", "2018-11-05", "yyyy-MM-dd", TimeUnit.DAYS));

        Assert.assertEquals(-1, timeInterval("2018-11-05 11", "2018-11-05 10", "yyyy-MM-dd HH", TimeUnit.HOURS));
        Assert.assertEquals(0, timeInterval("2018-11-05 10", "2018-11-05 10", "yyyy-MM-dd HH", TimeUnit.HOURS));
        Assert.assertEquals(1, timeInterval("2018-11-05 09", "2018-11-05 10", "yyyy-MM-dd HH", TimeUnit.HOURS));

        Assert.assertEquals(-1, timeInterval("2018-11-05 10:09", "2018-11-05 10:08", "yyyy-MM-dd HH:mm", TimeUnit.MINUTES));
        Assert.assertEquals(0, timeInterval("2018-11-05 10:08", "2018-11-05 10:08", "yyyy-MM-dd HH:mm", TimeUnit.MINUTES));
        Assert.assertEquals(1, timeInterval("2018-11-05 10:07", "2018-11-05 10:08", "yyyy-MM-dd HH:mm", TimeUnit.MINUTES));

        Assert.assertEquals(-1, timeInterval("2018-11-05 10:08:09", "2018-11-05 10:08:08", "yyyy-MM-dd HH:mm:ss", TimeUnit.SECONDS));
        Assert.assertEquals(0, timeInterval("2018-11-05 10:08:08", "2018-11-05 10:08:08", "yyyy-MM-dd HH:mm:ss", TimeUnit.SECONDS));
        Assert.assertEquals(1, timeInterval("2018-11-05 10:08:07", "2018-11-05 10:08:08", "yyyy-MM-dd HH:mm:ss", TimeUnit.SECONDS));
    }

    public static long monInterval(String startTime, String endTime) throws ParseException {
        return DatetimeUtils.intervalOfMonths(DatetimeUtils.parse(startTime, FMT_MON), DatetimeUtils.parse(endTime, FMT_MON));
    }

    public static long yearInterval(String startTime, String endTime) throws ParseException {
        return DatetimeUtils.intervalOfYears(DatetimeUtils.parse(startTime, FMT_YEAR), DatetimeUtils.parse(endTime, FMT_YEAR));
    }

    public static long timeInterval(String startTime, String endTime, String pattern, TimeUnit unit) throws ParseException {
        return DatetimeUtils.intervalOfDatetime(DatetimeUtils.parse(startTime, pattern), DatetimeUtils.parse(endTime, pattern), unit);
    }

    @Test
    public void testParse() throws ParseException {
        Date date = DatetimeUtils.parse("2018-01-30", "yyyy-MM-dd");
        Assert.assertEquals("2018-01-30 00:00:00", DateFormatUtils.format(date, FMT));

        Date date2 = DatetimeUtils.parse("2018-01-30 10:36:00", FMT);
        Assert.assertEquals("2018-01-30 10:36:00", DateFormatUtils.format(date2, FMT));

        Date date21 = DatetimeUtils.parse("2018-10-30 10:36:00", null);
        Assert.assertEquals("2018-10-30 10:36:00", DateFormatUtils.format(date21, FMT));

        Date date3 = null;
        try {
            date3 = DatetimeUtils.parse("2018-02-30", "yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertNull(date3);
    }

    @Test
    public void testFormat() throws ParseException {
        Assert.assertEquals("2018-11-05", DatetimeUtils.format(DatetimeUtils.parse("2018-11-05", "yyyy-MM-dd"), "yyyy-MM-dd"));
        Assert.assertEquals("2018-10-30 10:36:00", DatetimeUtils.format(DatetimeUtils.parse("2018-10-30 10:36:00", null), null));
    }

    @Test
    public void testParseQuietly() {
        Assert.assertNull(DatetimeUtils.parseQuietly("2018-02-30 10:36:00", FMT));
        Assert.assertNull(DatetimeUtils.parseQuietly("2018/10/30 10:36:00", FMT));

        Assert.assertNotNull(DatetimeUtils.parseQuietly("2018-10-30", "yyyy-MM-dd"));
        Assert.assertNotNull(DatetimeUtils.parseQuietly("2018-10-30 10:36:00", FMT));
        Assert.assertEquals("2018-10-30 10:36:00", DatetimeUtils.format(DatetimeUtils.parseQuietly("2018-10-30 10:36:00", FMT)));
    }

    @Test
    public void testNextClearDate() {
        Date now = DatetimeUtils.parseQuietly("2018-11-05 11:58:36", null);
        Assert.assertEquals("2019-01-01 00:00:00", DatetimeUtils.format(DatetimeUtils.nextClearDate(now, Calendar.YEAR, 1), null));
        Assert.assertEquals("2018-12-01 00:00:00", DatetimeUtils.format(DatetimeUtils.nextClearDate(now, Calendar.MONTH, 1), null));
        Assert.assertEquals("2018-11-06 00:00:00", DatetimeUtils.format(DatetimeUtils.nextClearDate(now, Calendar.DAY_OF_MONTH, 1), null));
        Assert.assertEquals("2018-11-05 12:00:00", DatetimeUtils.format(DatetimeUtils.nextClearDate(now, Calendar.HOUR_OF_DAY, 1), null));
        Assert.assertEquals("2018-11-05 11:59:00", DatetimeUtils.format(DatetimeUtils.nextClearDate(now, Calendar.MINUTE, 1), null));
        Assert.assertEquals("2018-11-05 11:58:37", DatetimeUtils.format(DatetimeUtils.nextClearDate(now, Calendar.SECOND, 1), null));
//        Assert.assertEquals("2018-11-05 11:58:37", DatetimeUtils.format(DatetimeUtils.nextClearDate(now, Calendar.MILLISECOND, 1), null));
    }

    @Test
    public void testCompareTime() {
        Date date = DatetimeUtils.parseQuietly("2018-11-05 11:58:36", null);

        Assert.assertEquals(-1, DatetimeUtils.compareTime(date, "12:00:00", "HH:mm:ss"));
        Assert.assertEquals(0, DatetimeUtils.compareTime(date, "11:58:36", "HH:mm:ss"));
        Assert.assertEquals(1, DatetimeUtils.compareTime(date, "00:00:00", "HH:mm:ss"));
    }

    @Test
    public void testIsInTime() {
        Date date = DatetimeUtils.parseQuietly("2018-11-05 11:58:36", null);
        Date date1 = DatetimeUtils.parseQuietly("2018-11-05 23:00:36", null);
        Date date2 = DatetimeUtils.parseQuietly("2018-11-05 00:00:00", null);
        Date date3 = DatetimeUtils.parseQuietly("2018-11-05 06:00:00", null);
        Date date4 = DatetimeUtils.parseQuietly("2018-11-05 00:01:01", null);

        Assert.assertFalse(DatetimeUtils.isInTime(date, "00:00:00", "06:00:00", "HH:mm:ss"));
        Assert.assertFalse(DatetimeUtils.isInTime(date, "23:00:00", "23:59:59", "HH:mm:ss"));

        Assert.assertFalse(DatetimeUtils.isInTime(date1, "00:00:00", "06:00:00", "HH:mm:ss"));
        Assert.assertTrue(DatetimeUtils.isInTime(date1, "23:00:00", "23:59:59", "HH:mm:ss"));

        Assert.assertTrue(DatetimeUtils.isInTime(date2, "00:00:00", "06:00:00", "HH:mm:ss"));
        Assert.assertFalse(DatetimeUtils.isInTime(date2, "23:00:00", "23:59:59", "HH:mm:ss"));

        Assert.assertTrue(DatetimeUtils.isInTime(date3, "00:00:00", "06:00:00", "HH:mm:ss"));
        Assert.assertFalse(DatetimeUtils.isInTime(date3, "23:00:00", "23:59:59", "HH:mm:ss"));

        Assert.assertTrue(DatetimeUtils.isInTime(date4, "00:00:00", "06:00:00", "HH:mm:ss"));
        Assert.assertFalse(DatetimeUtils.isInTime(date4, "23:00:00", "23:59:59", "HH:mm:ss"));
    }

    @Test
    public void testMonthList() {
        List<String> list1 = DatetimeUtils.getMonthList("yyyy-MM", 5, true);
        List<String> list2 = DatetimeUtils.getMonthList("yyyy-MM", 5, false);
        System.out.println(list1);
        System.out.println(list2);
        org.springframework.util.Assert.notEmpty(list1, "error");
        org.springframework.util.Assert.notEmpty(list2, "error");
    }

}
