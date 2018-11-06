package org.stathry.commons.data;

import org.springframework.beans.factory.support.MethodReplacer;

import java.lang.reflect.Method;

/**
 * TODO
 * Created by dongdaiming on 2018-11-06 14:53
 */
public class HelloMethodReplacer implements MethodReplacer {
    @Override
    public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {
        String hi = "hi ";
        if(args != null && args.length > 0) {
            hi = hi + args[0];
        }
        System.out.println(hi);
        return hi;
    }
}
