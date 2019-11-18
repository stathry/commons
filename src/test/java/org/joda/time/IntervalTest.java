package org.joda.time;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.util.Date;

/**
 * IntervalTest
 *
 * @author dongdaiming(董代明)
 * @date 2019-08-03
 */
public class IntervalTest {

    @Test
    public void testInterval() {
        Date now = new Date();
        Date startDate = DateUtils.addYears(now, -2);
        startDate = DateUtils.addMonths(startDate, -3);
        startDate = DateUtils.addDays(startDate, -8);
        startDate = DateUtils.addHours(startDate, -8);
        startDate = DateUtils.addMinutes(startDate, -6);
        startDate = DateUtils.addSeconds(startDate, -8);
        Interval interval = new Interval(startDate.getTime(), now.getTime());
        Period period = interval.toPeriod();

        System.out.printf(
                "%d years, %d months, %d days, %d hours, %d minutes, %d seconds%n",
                period.getYears(), period.getMonths(), period.getDays(),
                period.getHours(), period.getMinutes(), period.getSeconds());
    }
}
