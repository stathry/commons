package org.stathry.commons.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ParamNameUtils
 * Created by dongdaiming on 2018-12-26 18:49
 */
public class ParamNameUtils {

    private static Pattern UP = Pattern.compile("[A-Z]");

    public static String toUnderName(String name) {
        if(name == null || name.trim().isEmpty()) {
            return "";
        }
        Matcher matcher = UP.matcher(name);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, "_" + matcher.group());
        }
        return matcher.appendTail(buffer).toString().toLowerCase();
    }
}
