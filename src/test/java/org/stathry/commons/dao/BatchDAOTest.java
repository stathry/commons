package org.stathry.commons.dao;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stathry.commons.mapper.impl.BatchDAOSkeleton;
import xyz.downgoon.snowflake.Snowflake;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * TODO
 * Created by dongdaiming on 2018-10-12 17:51
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-context.xml")
public class BatchDAOTest {

    final Snowflake snowflake = new Snowflake(6, 8);

    @Test
    public void testBatchSaveOrders() {
        List<Integer> list = Arrays.asList(34, 100, 200, 201, 300, 400, 401);
        int c = 0;
        for (int i = 0, size = list.size(); i < size; i++) {
            bathInsert(list.get(i));
            c += list.get(i);
        }
        System.out.println(c);
    }

    private void bathInsert(int limit) {
        int batchSize = 200;
        List<Order> list = new ArrayList<>(limit);
        String sql = "insert into main_order (order_status, order_no, order_time, uid, amount) values (?, ?, ?, ?, ?);";
        Order o;
        for (int i = 0; i < limit; i++) {
            o = new Order();
            o.setUserId(i % 50L);
            o.setOrderNo(String.valueOf(snowflake.nextId()));
            o.setOrderTime(DateUtils.addSeconds(new Date(), i));
            o.setAmount(i * 100L + i);
            o.setOrderStatus(i % 4);
            list.add(o);
        }
        int n = BatchDAOSkeleton.jdbcBatchInsert(null, list, sql, batchSize);
        Assert.assertEquals(limit, n);
    }

    private static class Order implements BatchDAOSkeleton.BatchInsertion {

        /**  */
        private Integer orderStatus;

        /**  */
        private String orderNo;

        /**  */
        private Date orderTime;

        /**  */
        private Long userId;

        /**  */
        private Long amount;

        public Integer getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(Integer orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public Date getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(Date orderTime) {
            this.orderTime = orderTime;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }


        public Long getAmount() {
            return amount;
        }

        public void setAmount(Long amount) {
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "orderStatus=" + orderStatus +
                    ", orderNo='" + orderNo + '\'' +
                    ", orderTime=" + orderTime +
                    ", userId=" + userId +
                    ", amount=" + amount +
                    '}';
        }

        @Override
        public Object[] toArgArray() {
            return new Object[]{orderStatus, orderNo, orderTime, userId, amount};
        }
    }
}
