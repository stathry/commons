package com.google.common;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * GuavaTest
 * Created by dongdaiming on 2018-11-27 15:12
 */
public class GuavaTest {

    @Test
    public void testFile() throws IOException {
        Files.createParentDirs(new File("/temp/201811271641/1.txt"));
        Assert.assertTrue(new File("/temp/201811271641").exists());
    }

    @Test
    public void testCharStreams() throws IOException {
        FileReader fileR = new FileReader("/temp/auto/world/mapper/CityMapper.xml");
        System.out.println(CharStreams.toString(fileR));
    }

    @Test
    public void testByteStreams() throws IOException {
        FileInputStream fileIn = new FileInputStream("/temp/auto/springx/model/ZhimaScoreAccount.java");
        System.out.println(new String(ByteStreams.toByteArray(fileIn), "utf-8"));
    }

    @Test
    public void testCacheByCacheLoader() throws ExecutionException {
        int limit = 10000;
        int capacity = 1_0000;
        CacheLoader<Integer, String> loader = new CacheLoader<Integer, String>() {
            @Override
            public String load(Integer key) throws Exception {
                return new StringBuilder("nv").append(key).toString();
            }
        };

        LoadingCache<Integer, String> cache = CacheBuilder.newBuilder().concurrencyLevel(8).maximumSize(capacity)
                .expireAfterWrite(10, TimeUnit.SECONDS).initialCapacity(capacity).recordStats().build(loader);

        for (int i = 0; i < limit; i++) {
            cache.put(i, "v" + i);
        }
        System.out.println("size1:" + cache.size());
        System.out.println(cache.size());

        int n;
        Random random = new SecureRandom();
        for (int i = 0; i < 1000; i++) {
            n = random.nextInt(10000);
            System.out.println("k:" + n + ", v=" + cache.get(n));
        }
        System.out.println("size2:" + cache.size());

        System.out.println(cache.stats());
    }

    @Test
    public void testCacheByCall() throws ExecutionException {
        int limit = 10000;
        int capacity = 1_0000;

        Cache<Integer, String> cache = CacheBuilder.newBuilder().concurrencyLevel(8).maximumSize(capacity)
                .expireAfterWrite(10, TimeUnit.SECONDS).initialCapacity(capacity).recordStats().build();

        for (int i = 0; i < limit; i++) {
            cache.put(i, "v" + i);
        }
        System.out.println("size1:" + cache.size());
        System.out.println(cache.size());

        int n;
        Random random = new SecureRandom();
        for (int i = 0; i < 1000; i++) {
            n = random.nextInt(10000);
            final int fn = n;
            System.out.println("k:" + n + ", v=" + cache.get(fn, () -> {
                return "ncv" + fn;
            }));
        }
        System.out.println("size2:" + cache.size());

        System.out.println(cache.stats());
    }

}
