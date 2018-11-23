/**
 * Copyright 2012-2016 free Co., Ltd.
 */
package org.stathry.commons.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

/**
 * @author dongdaiming@free.com
 * 2016年8月23日
 */

@SuppressWarnings("all")
public class CollectionUtils {

    /**
     * 泛型list转换为数组
     *
     * @param src List<T> 原List
     * @return T[] 转换后的数组
     */
    public static <T> T[] listToArray(List<T> src, Class<T> type) {
        if (src == null || src.isEmpty()) {
            return null;
        }
        // 初始化泛型数组
        // JAVA中不允许这样初始化泛型数组： T[] dest = new T[src.size()];
        T[] dest = (T[]) Array.newInstance(type, src.size());
        for (int i = 0; i < src.size(); i++) {
            dest[i] = src.get(i);
        }
        return (T[]) dest;
    }

    /**
     * 泛型嵌套list转换为二维数组
     *
     * @param src List<List<T>> 原嵌套list （子list的长度必须相等）
     * @return T[][] 转换后的二维数组
     */
    public static <T> T[][] listsToArrays(List<List<T>> src, Class<T> type) {
        if (src == null || src.isEmpty()) {
            return null;
        }

        // 初始化泛型二维数组
        // JAVA中不允许这样初始化泛型二维数组： T[][] dest = new T[src.size()][];
        T[][] dest = dest = (T[][]) Array.newInstance(type, src.size(), src.get(0).size());

        for (int i = 0; i < src.size(); i++) {
            for (int j = 0; j < src.get(i).size(); j++) {
                dest[i][j] = src.get(i).get(j);
            }
        }

        return dest;
    }

    public static <E> int countSameElementsOf(Collection<E> c1, Collection<E> c2) {
        if (c1 == null || c1.isEmpty() || c2 == null || c2.isEmpty()) {
            return -1;
        }
        int c = 0;
        Iterator<E> it;
        E e1;
        int size1 = c1.size(), size2 = c2.size();
        if (size1 < size2) {
            c = countByCollectionType(c1, c2, c, size1);
        } else {
            c = countByCollectionType(c2, c1, c, size1);
        }

        return c;
    }

    private static <E> int countByCollectionType(Collection<E> c1, Collection<E> c2, int c, int size1) {
        Iterator<E> it;
        if (c1 instanceof RandomAccess && c1 instanceof List) {
            List<E> c11 = (List<E>) c1;
            for (int i = 0; i < size1; i++) {
                if (c2.contains(c11.get(i))) {
                    c++;
                }
            }
        } else {
            it = c1.iterator();
            for (; it.hasNext(); ) {
                if (c2.contains(it.next())) {
                    c++;
                }
            }
        }
        return c;
    }

}
