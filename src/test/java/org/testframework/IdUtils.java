package org.testframework;

import java.util.UUID;

/**
 * IdUtils
 * Created by dongdaiming on 2018-12-29 14:58
 */
public final class IdUtils {

    private String me;

    public IdUtils() {
    }

    public IdUtils(String me) {
        this.me = me;
    }

    public static String id() {
        return UUID.randomUUID().toString();
    }

    public String pubId(boolean random) {
        return "hello-" + pid(random);
    }

    private String pid(boolean random) {
        return UUID.randomUUID().toString();
    }

    public final String fId() {
        return "final";
    }

    public String showMe() {
        return me;
    }

}
