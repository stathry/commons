package org.stathry.commons.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;

/**
 * 分布式id工具类
 *
 * @Author Wang Lin(王霖)
 * @Date 2017/9/2
 * @Time 14:25
 */
public class DistributeIdUtils {

    private static final int USER_ID_BITS = 8;
    private static final int USER_ID_MASK = (1 << USER_ID_BITS) - 1;
    private static final int TIME_BIT = 42;
    private static final ZoneOffset ZONE_OFFSET = OffsetDateTime.now().getOffset();
    private static final long OVER_TIME_MONTH = 3L;

    private static final long EPOCH;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, Calendar.JANUARY, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        EPOCH = calendar.getTimeInMillis() / 1000;
    }

    /**
     * 根据idg生成的id和用户id生成订单id
     * 规则: idg id+用户id后8位
     *
     * @param idgId
     * @param userId
     * @return
     */
    public static long generateOrderId(long idgId, long userId) {
        return idgId | (userId & USER_ID_MASK);
    }

    /**
     * 从订单id里获取当前时间
     *
     * @param orderId
     * @return
     */
    public static LocalDateTime getTime(long orderId) {
        long seconds = (orderId >>> (64 - TIME_BIT)) + EPOCH;
        return LocalDateTime.ofEpochSecond(seconds, 0, ZONE_OFFSET);
    }

    /**
     * 获取某个时间点的起始订单id
     *
     * @param dateTime
     * @return
     */
    public static long getMinOrderId(LocalDateTime dateTime) {
        long seconds = OffsetDateTime.of(dateTime, ZONE_OFFSET).toEpochSecond() - EPOCH;
        return seconds << (64 - TIME_BIT);
    }

    /**
     * 获取es index 秒级
     *
     * @param orderId
     * @param index
     * @return
     */
    public static String buildIndexWithSecondId(long orderId, String index) {
        LocalDateTime time = getTime(orderId);
        return String.format(new StringBuilder(index).append("-").append(time.getYear()).append("%02d").toString(), time.getMonthValue());
    }

    /**
     * 获取es index 毫秒级
     *
     * @param id
     * @param index
     * @return
     */
    public static String buildIndexWithMillsId(long id, String index) {
        LocalDateTime time = getMillsTime(id);
        return String.format(new StringBuilder(index).append("-").append(time.getYear()).append("%02d").toString(), time.getMonthValue());
    }

    /**
     * 从订单id里获取当前时间
     *
     * @param orderId
     * @return
     */
    public static LocalDateTime getMillsTime(long orderId) {
        long timeMills = (orderId >>> (64 - TIME_BIT)) + EPOCH * 1000;
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMills), ZoneId.systemDefault());
    }


    /**
     * 时间是否超过三个月
     *
     * @param orderId
     * @return
     */
    public static boolean overTime(Long orderId) {
        LocalDateTime createTime = getTime(orderId);
        if (LocalDateTime.now().minusMonths(OVER_TIME_MONTH).isAfter(createTime)) {
            return true;
        }
        return false;
    }

    /**
     * 生成收款单id.
     * <p>
     * 规则：C{orderId}-{index}
     *
     * @param orderId
     * @param index
     * @return
     */
    public static String generatePayCollectionId(long orderId, int index) {
        return "C" + orderId + "-" + index;
    }

    /**
     * 生成退款单id.
     * <p>
     * 规则：R{orderId}-{index}
     *
     * @param orderId
     * @param index
     * @return
     */
    public static String generatePayRefundId(long orderId, int index) {
        return "R" + orderId + "-" + index;
    }
}
