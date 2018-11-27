package com.google.common;

import com.google.common.base.Throwables;
import org.junit.Test;

import java.io.FileInputStream;
import java.sql.SQLException;

/**
 * ThrowablesTest
 * Created by dongdaiming on 2018-11-27 13:09
 */
public class ThrowablesTest {

    @Test
    public void testRootCause() {
        try {
            int n = 1/0;
        } catch (Exception e) {
            Exception ce = new RuntimeException(e);
            Throwable te = Throwables.getRootCause(ce);
            System.out.println(te.getClass() + ", " + te.getMessage());
        }
    }

    @Test
    public void testPropagate0() throws SQLException {
        try {
            new FileInputStream("xyz");
        } catch (Exception e) {
            Throwables.propagateIfPossible(e, SQLException.class);

            Exception ce = new RuntimeException(e);
            System.out.println(ce.getClass() + ", " + ce.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testPropagate1() {
        try {
            int n = (Integer) null;
        } catch (Exception e) {
            Throwables.propagateIfPossible(e, ArithmeticException.class); // 如果异常类型匹配或者异常是运行时异常(非检查异常)都会被抛出

            Exception ce = new RuntimeException(e);
            System.out.println(ce.getClass() + ", " + ce.getMessage());
        }
    }

    @Test(expected = ArithmeticException.class)
    public void testPropagate2() {
        try {
            int n = 1/0;
        } catch (Exception e) {
            Throwables.propagateIfPossible(e, ArithmeticException.class);

            Exception ce = new RuntimeException(e);
            System.out.println(ce.getClass() + ", " + ce.getMessage());
        }
    }
}
