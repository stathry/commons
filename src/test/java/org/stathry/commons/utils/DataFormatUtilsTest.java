package org.stathry.commons.utils;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DataFormatUtilsTest {

    @Test
    public void testNewFormatDate() throws ParseException {
        DataFormatter dataFormatUtils = new DataFormatter();
        Assert.assertEquals("2018-12-04 16:32:00", dataFormatUtils.format2(DateUtils.parseDate("2018-12-04 16:32", "yyyy-MM-dd HH:mm")));
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.parseDate("2018-12-04 16:32", "yyyy-MM-dd HH:mm"));
        Assert.assertEquals("2018-12-04 16:32:00", dataFormatUtils.format2(cal));

//        Assert.assertEquals("2018-12-04 16:32:00", dataFormatUtils.format2("2018-12-04 16:32:00", DataFormatter.TYPE_DATE));
    }

    @Test
    public void testNewFormatInt() {
        DataFormatter dataFormatUtils = new DataFormatter();
        Assert.assertEquals("6", dataFormatUtils.format2(6));
        Assert.assertEquals("-6", dataFormatUtils.format2(-6));

        Assert.assertEquals("6", dataFormatUtils.format2(6, DataFormatter.TYPE_INT));
        Assert.assertEquals("-6", dataFormatUtils.format2(-6, DataFormatter.TYPE_INT));
    }

//    testNewFormatFloat:		12052/11395/12506
//    testStaticFormatFloat:	21355/21356/23317
    @Test
    public void testNewFormatFloat() {
        DataFormatter dataFormatUtils = new DataFormatter();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100_0000; i++) {
            dataFormatUtils.format2(new Date());
            Assert.assertEquals("6.00", dataFormatUtils.format(6, DataFormatter.TYPE_FLOAT));
            Assert.assertEquals("-6.00", dataFormatUtils.format(-6, DataFormatter.TYPE_FLOAT));

            Assert.assertEquals("500.89", dataFormatUtils.format(500.885));
            Assert.assertEquals("500.89", dataFormatUtils.format(500.885f));

            Assert.assertEquals("6.60", dataFormatUtils.format2(6.6));
            Assert.assertEquals("-6.60", dataFormatUtils.format2(-6.6));

            Assert.assertEquals("1.66", dataFormatUtils.format2(1.656));
            Assert.assertEquals("1.65", dataFormatUtils.format2(1.654));
            Assert.assertEquals("-1.66", dataFormatUtils.format2(-1.656));
            Assert.assertEquals("-1.65", dataFormatUtils.format2(-1.654));

            Assert.assertEquals("6.60", dataFormatUtils.format2("6.6", DataFormatter.TYPE_FLOAT));
            Assert.assertEquals("-6.60", dataFormatUtils.format2("-6.6", DataFormatter.TYPE_FLOAT));

            Assert.assertEquals("1.66", dataFormatUtils.format2("1.656", DataFormatter.TYPE_FLOAT));
            Assert.assertEquals("-1.66", dataFormatUtils.format2("-1.656", DataFormatter.TYPE_FLOAT));

            Assert.assertEquals("1234567890123456", dataFormatUtils.format2(1234567890123456L));

            Assert.assertEquals("12345678901234.46", dataFormatUtils.format2(12345678901234.456));
        }
        System.out.println("testNewFormatFloat:" + (System.currentTimeMillis() - start));
    }


    @Test
    public void testStaticFormatDate() throws ParseException {
        Assert.assertEquals("2018-12-04 16:32:00", DataFormatter.format(DateUtils.parseDate("2018-12-04 16:32", "yyyy-MM-dd HH:mm")));
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.parseDate("2018-12-04 16:32", "yyyy-MM-dd HH:mm"));
        Assert.assertEquals("2018-12-04 16:32:00", DataFormatter.format(cal));

        Assert.assertEquals("2018-12-04", DataFormatter.formatDate("2018-12-04 16:32", "yyyy-MM-dd HH:mm", "yyyy-MM-dd"));
    }

    @Test
    public void testStaticFormatInt() {
        Assert.assertEquals("6", DataFormatter.format(6));
        Assert.assertEquals("-6", DataFormatter.format(-6));

        Assert.assertEquals("6", DataFormatter.format(6, DataFormatter.TYPE_INT));
        Assert.assertEquals("-6", DataFormatter.format(-6, DataFormatter.TYPE_INT));
    }

    @Test
    public void testStaticFormatFloat() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100_0000; i++) {
            DataFormatter.format(new Date());
            Assert.assertEquals("6.00", DataFormatter.format(6, DataFormatter.TYPE_FLOAT));
            Assert.assertEquals("-6.00", DataFormatter.format(-6, DataFormatter.TYPE_FLOAT));

            Assert.assertEquals("500.89", DataFormatter.format(500.885));
            Assert.assertEquals("500.89", DataFormatter.format(500.885f));

            Assert.assertEquals("6.60", DataFormatter.format(6.6));
            Assert.assertEquals("-6.60", DataFormatter.format(-6.6));

            Assert.assertEquals("1.66", DataFormatter.format(1.656));
            Assert.assertEquals("1.65", DataFormatter.format(1.654));
            Assert.assertEquals("-1.66", DataFormatter.format(-1.656));
            Assert.assertEquals("-1.65", DataFormatter.format(-1.654));

            Assert.assertEquals("6.60", DataFormatter.format("6.6", DataFormatter.TYPE_FLOAT));
            Assert.assertEquals("-6.60", DataFormatter.format("-6.6", DataFormatter.TYPE_FLOAT));

            Assert.assertEquals("1.66", DataFormatter.format("1.656", DataFormatter.TYPE_FLOAT));
            Assert.assertEquals("-1.66", DataFormatter.format("-1.656", DataFormatter.TYPE_FLOAT));

            Assert.assertEquals("1234567890123456", DataFormatter.format(1234567890123456L));

            Assert.assertEquals("12345678901234.46", DataFormatter.format(12345678901234.456));
        }
        System.out.println("testStaticFormatFloat:" + (System.currentTimeMillis() - start));
    }

}
