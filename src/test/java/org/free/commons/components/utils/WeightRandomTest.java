package org.free.commons.components.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * TODO
 */
public class WeightRandomTest {

    @Test
    public void testMap1() {
        int limit = 1000000;
        Map<String, Integer> weight = new HashMap<>(4);
        weight.put("A", 1);
        weight.put("B", 2);
        weight.put("C", 3);
        weight.put("D", 4);
        WeightRandom<String, Integer> random = new WeightRandom<>(weight);
        
        count(limit, random);
    }
    @Test
    public void testMap2() {
        int limit = 1000000;
        Map<String, Integer> weight = new HashMap<>(4);
        weight.put("A", 1);
        weight.put("B", 0);
        WeightRandom<String, Integer> random = new WeightRandom<>(weight);
        
        count(limit, random);
    }
    
    @Test
    public void testList1() {
        int limit = 1000000;
        List<String> keys = Arrays.asList("A", "B", "C", "D");
        List<Integer> values = Arrays.asList(1, 2, 3, 4);
        WeightRandom<String, Integer> random = new WeightRandom<>(keys, values);
        
        count(limit, random);
    }

    /**
     * @param limit
     * @param start
     * @param random
     */
    private void count(int limit, WeightRandom<String, Integer> random) {
        long start = System.currentTimeMillis();
        Map<String,Integer> counter = new HashMap<>(4);
        String r;
        for (int i = 0; i < limit; i++) {
            r = random.random();
            if(counter.containsKey(r)) {
                counter.put(r, (counter.get(r) + 1));
            } else {
                counter.put(r, 1);
            }
        }

        System.out.println(counter);
        System.out.println("cost time in ms:" + (System.currentTimeMillis() - start));

        for (java.util.Map.Entry<String, Integer> e : counter.entrySet()) {
            System.out.println(e.getKey() + ":" + e.getValue() * 1.0 / limit);
        }
    }
    
}
