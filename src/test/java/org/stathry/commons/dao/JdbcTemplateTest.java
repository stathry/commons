package org.stathry.commons.dao;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;

/**
 * TODO
 * Created by dongdaiming on 2018-08-15 09:40
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-context.xml")
public class JdbcTemplateTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testSelectForUpdate() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                selectForUpdate();
            }
        });
        t1.start();

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                selectForUpdate();
            }
        });
        t2.start();

        while (t1.isAlive() || t2.isAlive()) {
        }
    }

    private void selectForUpdate() {
        long startTime = System.currentTimeMillis();
        String s = "select * from city where id = ? for update";
        Map<String, Object> map = jdbcTemplate.queryForMap(s, 15491);
        String th = Thread.currentThread().getName();
        String time = DateFormatUtils.format(new Date(), "HH:mm:ss.SSS");
        System.out.println(th + ", at " + time + ",cost time " + (System.currentTimeMillis() - startTime) + "--" + map);
    }


    @Test
    public void testSave() {
        String s = "INSERT INTO mx_bank_bill2 (order_no, bank_card, trans_date) VALUES (?, ?, NOW())";
        int n = jdbcTemplate.update(s, "o1", "bc1");
        System.out.println(n);
    }

    @Test
    public void testBatchSave() throws InterruptedException {
        String s = "INSERT INTO mx_bank_bill2 (order_no, bank_card, trans_date) VALUES (?, ?, NOW())";
        int limit = 1000;
        List<Object[]> args = new ArrayList<>(100);
        for (int i = 0, last = limit - 1; i < limit; i++) {
            if ((i != 0 && i % 100 == 0) || i == last) {
                jdbcTemplate.batchUpdate(s, args);
            }

            if (i != 0 && i % 100 == 0) {
                Thread.sleep(100);
                args = new ArrayList<>(100);
            }

            args.add(new Object[]{"o" + i / 10, "bc" + i / 5});
        }
    }

/*    CREATE TABLE `data_data1` (
            `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	`fstr1` VARCHAR(50) NOT NULL,
	`fint1` INT(11) NOT NULL,
	`ffloat1` DECIMAL(10,4) NOT NULL,
	`fdate1` DATETIME NOT NULL,
            `fdate2` TIMESTAMP NOT NULL,
            `fdate3` DATE NOT NULL,
            `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
)
    ENGINE=InnoDB
    ;*/

    @Test
    public void testBatchSave1() throws InterruptedException {
        Random random = new Random();
        String s = "INSERT INTO data_data1(fstr1, fint1, ffloat1, fdate1, fdate2, fdate3) VALUES (?, ?, ?, ?, ?, ?)";
        int limit = 20_0000;
        Date now = new Date();
        List<Object[]> args = new ArrayList<>(100);
        for (int i = 0, last = limit - 1; i < limit; i++) {
            if ((i != 0 && i % 100 == 0) || i == last) {
                jdbcTemplate.batchUpdate(s, args);
            }

            if (i != 0 && i % 100 == 0) {
                Thread.sleep(100);
                args = new ArrayList<>(100);
            }

            now = DateUtils.addMilliseconds(now, i);
            String d1 = DateFormatUtils.format(now, "yyyy-MM-dd HH:mm:ss");
            String d3 = DateFormatUtils.format(now, "yyyy-MM-dd");
            args.add(new Object[]{RandomStringUtils.randomAlphanumeric(6), random.nextInt(limit), random.nextInt(limit)/10000.0,
                    d1, d1, d3});
        }
    }

}
