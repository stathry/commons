package org.stathry.commons.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;

/**
 * IP获取
 * Created by dongdaiming on 2018-07-20 15:23
 */
public class IPUtils {

    private IPUtils() {
    }

    /**
     * 获取本机IP
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getCurIp() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    /**
     * 获取本机IP末尾
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getCurIpTail() throws UnknownHostException {
        String a = InetAddress.getLocalHost().getHostAddress();
        return a.substring(a.lastIndexOf(".") + 1);
    }

    /**
     * 获取机器标识
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getCurIpQuietly() {
        String a = "";
        try {
            a = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
        }
        return a;
    }

    public static int getHostFlagQuietly(int bound) {
        int a;
        try {
            a = InetAddress.getLocalHost().getHostAddress().hashCode();
            a = Math.abs(a) % bound;
        } catch (Exception e) {
            a = new SecureRandom().nextInt(bound);
        }
        return a;
    }
}
