package org.apache.commons.lang3;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * TODO
 * Created by dongdaiming on 2018-11-06 15:12
 */
public class DurationFormatUtilsTest {

    @Test
    public void testFormatTime() {
        Assert.assertEquals("06:00:06.060", DurationFormatUtils.formatDuration(6 * 60 * 60 * 1000 + 6000 + 60, "HH:mm:ss.SSS"));
        Assert.assertEquals("00:00:01.500", DurationFormatUtils.formatDuration(1500, "HH:mm:ss.SSS"));

        Assert.assertEquals("01:06", DurationFormatUtils.formatDuration(TimeUnit.MINUTES.toMillis(66), "HH:mm"));
        Assert.assertEquals("66 01:06", DurationFormatUtils.formatDuration(
                TimeUnit.DAYS.toMillis(66) + TimeUnit.MINUTES.toMillis(66), "dd HH:mm"));
        System.out.println(DurationFormatUtils.formatDuration(TimeUnit.DAYS.toMillis(66) + TimeUnit.MINUTES.toMillis(66), "dd HH:mm"));
    }
}
