package org.stathry.commons.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigManager {

    private static final String NAME_BASE = "conf" ;
    public static final String NAME_CONF = "config";
    public static final String NAME_SYS = "system";
    private static final ConcurrentHashMap<String, ConcurrentHashMap<String, String>> PROPS = new ConcurrentHashMap<>(4);

    static {
        getMap(NAME_CONF);
        getMap(NAME_SYS);
    }

    private ConfigManager() {}

    public static void main(String[] args) {
        System.out.println("1:" + get("order.desc"));
        Assert.hasText(get("order.desc"), "error");

        System.out.println("2:" + getObj("k9", List.class));
        Assert.notNull(getObj("k9", List.class), "error");

        System.out.println("3:" + getSysStr("order.desc2"));
        Assert.hasText(getSysStr("order.desc2"), "error");

        System.out.println("3:" + getSysObj("k99", List.class));
        Assert.notNull(getSysObj("k99", List.class), "error");
    }

    public static String get(String key) {
        if(StringUtils.isBlank(key)) return "";
        return StringUtils.trimToEmpty(getMap(NAME_CONF).get(key));
    }

    public static <T> T getObj(String key, Class<T> clazz) {
        if(StringUtils.isBlank(key)) return null;
        return JSON.parseObject(getMap(NAME_CONF).get(key), clazz);
    }

    public static String get(String resourceName, String key) {
        if(StringUtils.isBlank(key) || StringUtils.isBlank(resourceName)) return "";
        return StringUtils.trimToEmpty(getMap(resourceName).get(key));
    }

    public static String get(String resourceName, String key, String defaultValue) {
        if(StringUtils.isBlank(key) || StringUtils.isBlank(resourceName)) return "";
        String v = getMap(resourceName).get(key);
        return v == null ? defaultValue : v;
    }

    public static <T> T getObj(String resourceName, String key, Class<T> clazz) {
        if(StringUtils.isBlank(key) || StringUtils.isBlank(resourceName)) return null;
        return JSON.parseObject(getMap(resourceName).get(key), clazz);
    }

    public static String getSysStr(String key) {
        return get(NAME_SYS, key);
    }

    public static String getSysStr(String key, String defaultValue) {
        return get(NAME_SYS, key, defaultValue);
    }

    public static <T> T getSysObj(String key, Class<T> clazz) {
        return getObj(NAME_SYS, key, clazz);
    }

    public static ConcurrentHashMap<String, String> getMap(String name) {
        ConcurrentHashMap p = PROPS.get(name);
        if(p != null) {
            return p;
        }

        synchronized (ConfigManager.class) {
            p = PROPS.get(name);
            if(p != null) {
                return p;
            }
            Properties pp = loadProperties(name);
            if(pp == null) {
                return new ConcurrentHashMap();
            }
            ConcurrentHashMap pn = new ConcurrentHashMap(pp);
            PROPS.put(name, pn);
            return pn;
        }
    }

    private static Properties loadProperties(String name) {
        Properties prop = new Properties();
        String resourceName = NAME_BASE + "/" + name + ".properties";
        try(InputStream in = ConfigManager.class.getClassLoader().getResourceAsStream(resourceName);
            Reader r = new InputStreamReader(in, "UTF-8")) {
            prop.load(r);
        } catch (Exception e) {
        }
        Assert.notEmpty(prop, "loadProperties " + name + " error.");
        return prop;
    }

}
