package com.google.common.collect;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import org.apache.commons.lang3.RandomStringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Assert;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * TODO
 * Created by dongdaiming on 2018-11-27 13:15
 */
public class OrderingTest {

    // Ordering可基于字符串或自然顺序排序，支持部分元素为空的集合排序，并可以指定空元素排在头部或尾部
    @Test
    public void testOrdering1() {
        Random random = new SecureRandom();
        List<String> strA = new ArrayList<>();
        List<Integer> intA = new ArrayList<>();
        strA.add(null);
        intA.add(null);
        for (int i = 0; i < 20; i++) {
            strA.add(RandomStringUtils.randomAlphabetic(3));
            intA.add(random.nextInt(100));
        }
        strA.add(null);
        intA.add(null);
        System.out.println(JSON.toJSONString(strA));
        System.out.println(JSON.toJSONString(intA));
        System.out.println();

        Ordering<String> strOrdering = Ordering.usingToString().nullsFirst();
        Ordering<Integer> intOrdering = Ordering.natural().nullsLast();

        Assert.assertFalse(strOrdering.isOrdered(strA));
        Assert.assertFalse(intOrdering.isOrdered(intA));

        // jdk自带的Collections.sort当元素为空时有NPE
//        Collections.sort(strA);
//        Collections.sort(intA);
//        System.out.println("Collections.sort:" + strA);
//        System.out.println("Collections.sort:" + intA);

        System.out.println("Ordering.sort:" + strOrdering.sortedCopy(strA));
        System.out.println("Ordering.sort:" + intOrdering.sortedCopy(intA));

        System.out.println(strOrdering.max(strA));
        System.out.println(intOrdering.max(intA));
        System.out.println(strOrdering.min(strA));
        System.out.println(intOrdering.min(intA));

        Assert.assertNull(strOrdering.min(strA));
        Assert.assertNull(intOrdering.max(intA));
    }

    @Test
    public void testOrdering2() {
        List<User> users = new ArrayList<>();
        Random random = new SecureRandom();
        users.add(new User(888, "xyz", null));
        for (int i = 0; i < 20; i++) {
            users.add(new User(random.nextInt(1000), RandomStringUtils.randomAlphabetic(6), RandomStringUtils.randomAlphabetic(6)));
        }
        users.add(new User(888, "xyz", "abc"));
        System.out.println(JSON.toJSONString(users));

        Ordering<User> userOrdering = Ordering.natural().nullsLast().onResultOf(new Function<User, Comparable>() {
            @Override
            public Comparable apply(@Nullable User input) {
                return input.id;
            }
        });

        List<User> usersSorted = userOrdering.sortedCopy(users);

        System.out.println(JSON.toJSONString(usersSorted));
    }

    // ComparisonChain是可用于实现链式比较，为排序提供便利
    @Test
    public void testComparisonChain() {
        List<User> users = new ArrayList<>();
        Random random = new SecureRandom();
        users.add(new User(888, "xyz", null));
        for (int i = 0; i < 20; i++) {
            users.add(new User(random.nextInt(1000), RandomStringUtils.randomAlphabetic(6), RandomStringUtils.randomAlphabetic(6)));
        }
        users.add(new User(888, "xyz", "abc"));
        System.out.println(JSON.toJSONString(users));

        Collections.sort(users, ((o1, o2) -> {
            return ComparisonChain.start().compare(o1.id, o2.id).compare(o1.name1, o2.name1)
                    .compare(o1.name2, o2.name2, Ordering.natural().nullsLast()).result();
        }));

        System.out.println(JSON.toJSONString(users));
    }

    private static class User {
        private int id;
        private String name1;
        private String name2;

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name1='" + name1 + '\'' +
                    ", name2='" + name2 + '\'' +
                    '}';
        }

        public User(int id, String name1, String name2) {
            this.id = id;
            this.name1 = name1;
            this.name2 = name2;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName1() {
            return name1;
        }

        public String getName2() {
            return name2;
        }
    }
}
