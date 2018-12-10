package org.stathry.commons.utils;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DataFormatUtilsTest {

    @Test
    public void testFormatDate() throws ParseException {
        DataFormatter formatter = new DataFormatter();
        Date date1 = DateUtils.parseDate("2018-12-04 16:32", "yyyy-MM-dd HH:mm");
        Assert.assertEquals("2018-12-04 16:32:00", formatter.format(date1, null));
        Assert.assertEquals("2018-12-04", formatter.format(date1, new DataFormatter.DataFormat("yyyy-MM-dd")));

        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.parseDate("2018-12-04 16:32", "yyyy-MM-dd HH:mm"));
        Assert.assertEquals("2018-12-04 16:32:00", formatter.format(cal, null));
        Assert.assertEquals("2018-12-04", formatter.format(cal, new DataFormatter.DataFormat("yyyy-MM-dd")));

        Assert.assertEquals("2018-12-04", formatter.format("2018-12-04 16:22", 
                new DataFormatter.DataFormat("yyyy-MM-dd HH:mm", "yyyy-MM-dd"), 
                DataFormatter.TYPE_DATE));
    }

    @Test
    public void testFormatInt() {
        DataFormatter formatter = new DataFormatter();
        Assert.assertEquals("6", formatter.format(6));
        Assert.assertEquals("6", formatter.format("6"));
        Assert.assertEquals("-6", formatter.format(-6));
        Assert.assertEquals("1234567890123456", formatter.format(1234567890123456L));

        Assert.assertEquals("60", formatter.format(6, new DataFormatter.DataFormat(10)));
        Assert.assertEquals("60", formatter.format(6000, new DataFormatter.DataFormat(0.01)));
        Assert.assertEquals("0", formatter.format(6, new DataFormatter.DataFormat(0.01)));
        Assert.assertEquals("80", formatter.format("8", new DataFormatter.DataFormat(10), DataFormatter.TYPE_INT));
        Assert.assertEquals("8", formatter.format("800", new DataFormatter.DataFormat(0.01), DataFormatter.TYPE_INT));
        Assert.assertEquals("0", formatter.format("8", new DataFormatter.DataFormat(0.01), DataFormatter.TYPE_INT));
    }

    @Test
    public void testFormatFloat() {
        DataFormatter formatter = new DataFormatter();
        System.out.println(formatter.format(new Date()));

        Assert.assertEquals("500.89", formatter.format(500.885));
        Assert.assertEquals("500.89", formatter.format(500.885f));
        Assert.assertEquals("12345678901234.46", formatter.format(12345678901234.456));

        Assert.assertEquals("6.60", formatter.format(6.6));
        Assert.assertEquals("-6.60", formatter.format(-6.6));

        Assert.assertEquals("1.66", formatter.format(1.656));
        Assert.assertEquals("1.65", formatter.format(1.654));
        Assert.assertEquals("-1.66", formatter.format(-1.656));
        Assert.assertEquals("-1.65", formatter.format(-1.654));


        Assert.assertEquals("6.60", formatter.format("6.6", null, DataFormatter.TYPE_FLOAT));
        Assert.assertEquals("-6.60", formatter.format("-6.6", null, DataFormatter.TYPE_FLOAT));

        Assert.assertEquals("1.66", formatter.format("1.656", null, DataFormatter.TYPE_FLOAT));
        Assert.assertEquals("-1.66", formatter.format("-1.656", null, DataFormatter.TYPE_FLOAT));

        Assert.assertEquals("2", formatter.format(1.656, new DataFormatter.DataFormat(0, RoundingMode.HALF_UP)));
        Assert.assertEquals("-1.7", formatter.format("-1.656", new DataFormatter.DataFormat(1, RoundingMode.HALF_UP), DataFormatter.TYPE_FLOAT));
    }

}
