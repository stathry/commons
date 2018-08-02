package org.stathry.commons.log;

/**
 * 没有被引用的类不会被加载
 * Created by dongdaiming on 2018-07-31 13:51
 */
public class ErrorClass {

    static {
        System.out.println("ErrorClass initialized.");
        int i = 1 / 0;
    }
}
