package org.stathry.commons.bean;

/**
 * TODO
 * Created by dongdaiming on 2018-11-06 14:50
 */
public class ReplaceableBean {

    public String hello(String name) {
        String hello = "hello " + name;
        System.out.println(hello);
        return hello;
    }
}
