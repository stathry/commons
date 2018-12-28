package org.stathry.commons.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author dongdaiming
 * @date 2017年9月1日
 */
public class MapSortUtils {

    private MapSortUtils() {
    }

    public static Map<String, String> toNewStringMap(Map<String, ?> map) {
        if (map == null || map.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, String> strMap = new HashMap<>(map.size());
        Object v;
        for (Map.Entry<String, ?> e : map.entrySet()) {
            v = e.getValue();
            strMap.put(e.getKey(), v == null ? "" : v.toString());
        }
        return strMap;
    }

    public static Map<String, String> toStringMap(Map<String, ?> map) {
        if (map == null || map.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, String> strMap = (Map) map;
        Object v;
        String str;
        for (Map.Entry<String, ?> e : map.entrySet()) {
            v = e.getValue();
            if (v == null || !(v instanceof String)) {
                str = v == null ? "" : v.toString();
                strMap.put(e.getKey(), str);
            }
        }
        return (Map<String, String>) map;
    }

    /**
     * map根据value排序（value不能为空）
     *
     * @param map
     * @param asc
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, final boolean asc) {
        if (map == null || map.isEmpty()) {
            return map;
        }
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                if (asc) {
                    return (o1.getValue()).compareTo(o2.getValue());
                } else {
                    return (o2.getValue()).compareTo(o1.getValue());
                }
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * map先根据value排序，如果value一直则根据key排序
     *
     * @param map
     * @param asc
     * @return
     */
    public static <K extends Comparable<? super K>, V extends Comparable<? super V>> Map<K, V> sortByValueKey(Map<K, V> map, final boolean asc) {
        if (map == null || map.isEmpty()) {
            return map;
        }
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                if (asc) {
                    if (o1.getValue().compareTo(o2.getValue()) == 0) {
                        return o1.getKey().compareTo(o2.getKey());
                    }
                    return (o1.getValue()).compareTo(o2.getValue());
                } else {
                    if (o2.getValue().compareTo(o1.getValue()) == 0) {
                        return o2.getKey().compareTo(o1.getKey());
                    }
                    return (o2.getValue()).compareTo(o1.getValue());
                }
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * map根据value排序(TreeMap会去重，size可能会变小)
     *
     * @param map
     * @param asc 是否为升序
     * @return
     */
    @Deprecated
    public static <K, V extends Comparable<V>> Map<K, V> sortByValue2(Map<K, V> map, final boolean asc) {
        if (map == null || map.isEmpty()) {
            return map;
        }
        Comparator<K> comparator = new MapValueComparator<K, V>(map, asc);
        Map<K, V> result = new TreeMap<K, V>(comparator);
        result.putAll(map);
        return result;
    }

    public static class MapValueComparator<K, V extends Comparable<? super V>> implements Comparator<K> {

        private Map<K, V> map;
        private boolean asc;

        public MapValueComparator(Map<K, V> map2, boolean asc) {
            this.map = map2;
            this.asc = asc;
        }

        @Override
        public int compare(K k1, K k2) {
            if (asc) {
                return map.get(k1).compareTo(map.get(k2));
            } else {
                return map.get(k2).compareTo(map.get(k1));
            }
        }

    }

}
