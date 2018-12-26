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
    public void testCachePutGet() {
        Cache<Integer, String> cache = CacheBuilder.newBuilder().initialCapacity(2).maximumSize(16).concurrencyLevel(2).build();
        cache.put(1, "v1");
        Assert.assertEquals("v1",cache.getIfPresent(1));
        Assert.assertNull(cache.getIfPresent(2));
    }

    @Test
    public void testCachePutLoader() throws ExecutionException {
        CacheLoader<Integer, String> loader = new CacheLoader<Integer, String>() {
            @Override
            public String load(Integer key) throws Exception {
                return new StringBuilder("loader-").append(key).toString();
            }
        };

        LoadingCache<Integer, String> cache = CacheBuilder.newBuilder().initialCapacity(2).maximumSize(16).concurrencyLevel(2).build(loader);

        cache.put(1, "v1");
        Assert.assertEquals("v1",cache.getIfPresent(1));
        Assert.assertNull(cache.getIfPresent(2));
        Assert.assertEquals("loader-3", cache.get(3));
        System.out.println(cache.get(6));
        System.out.println(cache.get(8));
    }

    @Test
    public void testStatCacheQuery() {
        int limit = 10_0000;
        int capacity = 1_0000;

        Cache<Integer, String> cache = CacheBuilder.newBuilder().concurrencyLevel(8).maximumSize(capacity).initialCapacity(capacity)
                .expireAfterWrite(30, TimeUnit.SECONDS).recordStats().build();

        for (int i = 0; i < limit; i++) {
            cache.put(i, "v" + i);
        }
        System.out.println("size1:" + cache.size());

        for (int i = 0; i < limit; i++) {
            final int fn = i;
            cache.getIfPresent(fn);
        }

        System.out.println("size2:" + cache.size());
        System.out.println(cache.stats());
//        hitCount=10000, missCount=90000, loadSuccessCount=0, loadExceptionCount=0, totalLoadTime=0, evictionCount=90000
//        hitCount-命中数, missCount-未命中数, evictionCount-淘汰数(驱逐数)
    }



}
