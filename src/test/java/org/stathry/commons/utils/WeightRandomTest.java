package org.stathry.commons.utils;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 */
public class WeightRandomTest {

//    性能测试:{TreeWeightRandom2=3689, TreeWeightRandom1=3791, SimpleWeightRandom1=5915, SimpleWeightRandom2=5925}
//    性能:TreeWeightRandom2 > TreeWeightRandom1 > SimpleWeightRandom1 > SimpleWeightRandom2=5925

    private static final int LIMIT = 1000000;

    @Test
    public void testTreeWeightRandom1() {
        Map<String, Integer> weight = new HashMap<>();
        weight.put("A", 1);
        weight.put("B", 2);
        weight.put("C", 3);
        weight.put("D", 4);
        TreeWeightRandom<String, Integer> random = new TreeWeightRandom<>(weight);

        count(LIMIT, random, "TreeWeightRandom1");
    }

    @Test
    public void testTreeWeightRandom2() {
        List<String> keys = Arrays.asList("A", "B", "C", "D");
        List<Integer> values = Arrays.asList(1, 2, 3, 4);
        TreeWeightRandom<String, Integer> random = new TreeWeightRandom<>(keys, values);

        count(LIMIT, random, "TreeWeightRandom2");
    }

    @Test
    public void testSimpleWeightRandom1() {
        Map<String, Integer> wMap = new HashMap<>();
        wMap.put("B", 3);
        wMap.put("D", 1);
        wMap.put("A", 4);
        wMap.put("C", 2);
        SimpleWeightRandom<String, Integer> weightRandom = new SimpleWeightRandom<>(wMap);

        count(LIMIT, weightRandom, "SimpleWeightRandom1");
    }

    @Test
    public void testSimpleWeightRandom2() {
        List<String> keyList = Arrays.asList("B", "D", "A", "C");
        List<Integer> weightList = Arrays.asList(3, 1, 4, 2);
        SimpleWeightRandom<String, Integer> weightRandom = new SimpleWeightRandom<>(keyList, weightList);

        count(LIMIT, weightRandom, "SimpleWeightRandom2");
    }

    /**
     * @param limit
     * @param weightRandom
     */
    private <K extends Comparable, W extends Number> long count(int limit, WeightRandom<K, W> weightRandom, String flag) {
        Map<K, Integer> counter = new HashMap<>();
        K k;
        Integer c;
        long start = System.currentTimeMillis();
        for (int i = 0; i < limit; i++) {
            k = weightRandom.randomKey();
            c = counter.get(k);
            c = c == null ? 1 : (c + 1);
            counter.put(k, c);
        }

        long totalTime = (System.currentTimeMillis() - start);
//        System.out.println(flag + ",limit:" + limit + ",totalTime:" + totalTime + " ms, avgTime:" + (totalTime * 1.0 / limit) + " ms");
//        System.out.println("weightMap:" + weightRandom);
//        System.out.println("statisticsMap:" + JSON.toJSONString(counter));
//        for (java.util.Map.Entry<K, Integer> e : counter.entrySet()) {
//            System.out.println(e.getKey() + ":" + e.getValue() * 1.0 / limit);
//        }
        return totalTime;
    }

    @Test
    public void testAll() {
        Map<String, Integer> weightMap = new HashMap<>();
        weightMap.put("A", 1);
        weightMap.put("B", 2);
        weightMap.put("C", 3);
        weightMap.put("D", 4);
        List<String> keys = Arrays.asList("A", "B", "C", "D");
        List<Integer> values = Arrays.asList(1, 2, 3, 4);

        String tr1 = "TreeWeightRandom1", tr2 = "TreeWeightRandom2", sr1 = "SimpleWeightRandom1", sr2 = "SimpleWeightRandom2";
        Map<String, Long> sumTime = new HashMap<>();
        for (int i = 0; i < 100; i++) {

            sumCount(new TreeWeightRandom<>(weightMap), tr1, sumTime);

            sumCount(new TreeWeightRandom<>(keys, values), tr2, sumTime);

            sumCount(new SimpleWeightRandom<>(weightMap), sr1, sumTime);

            sumCount(new SimpleWeightRandom<>(keys, values), sr2, sumTime);
        }


        System.out.println(MapSortUtils.sortByValue(sumTime, true));
    }

    private void sumCount(WeightRandom<String, Integer> wr, String flag, Map<String, Long> sumTime) {
        long time = count(LIMIT, wr, flag);
        Long sum = sumTime.get(flag);
        sum = sum == null ? time : (time + sum);
        sumTime.put(flag, sum);
    }

}
