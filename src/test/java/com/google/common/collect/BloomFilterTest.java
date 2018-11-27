package com.google.common.collect;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * TODO
 * Created by dongdaiming on 2018-11-26 15:56
 */
public class BloomFilterTest {

    // https://segmentfault.com/a/1190000017137892?utm_source=tag-newest
    // 布隆过滤器将数据多次hash然后存入value为01的long数组，查询时再多次hash判断value是否都为1
    // 可用于超大数据集中判断指定数据是否存在
    @Test
    public void testBloomFilterInt() {
        int limit = 1000_0000;
        BloomFilter<Integer> filter = BloomFilter.create(Funnels.integerFunnel(), limit, 0.001);
        for (int i = 0; i <= limit; i++) {
            filter.put(i);
        }
        Assert.assertTrue(filter.mightContain(0));
        Assert.assertTrue(filter.mightContain(1));
        Assert.assertTrue(filter.mightContain(10));
        Assert.assertTrue(filter.mightContain(666));
        Assert.assertTrue(filter.mightContain(8888));
        Assert.assertTrue(filter.mightContain(10000));
        Assert.assertTrue(filter.mightContain(limit));
        Assert.assertFalse(filter.mightContain(limit * 10));
    }

    @Test
    public void testBloomFilterStr() {
        int limit = 1000_0000;
        BloomFilter<String> filter = BloomFilter.create(Funnels.stringFunnel(Charset.forName("utf-8")), limit, 0.001);
        for (int i = 0; i <= limit; i++) {
            filter.put(new StringBuilder("s").append(i).toString());
        }
        Assert.assertTrue(filter.mightContain("s0"));
        Assert.assertTrue(filter.mightContain("s1"));
        Assert.assertTrue(filter.mightContain("s10"));
        Assert.assertTrue(filter.mightContain("s666"));
        Assert.assertTrue(filter.mightContain("s8888"));
        Assert.assertTrue(filter.mightContain("s10000"));
        Assert.assertTrue(filter.mightContain("s" + limit));
        Assert.assertFalse(filter.mightContain("s" + (limit * 10)));
    }

}
