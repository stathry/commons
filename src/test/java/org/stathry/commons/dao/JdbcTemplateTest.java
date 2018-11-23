package org.stathry.commons.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

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

}
