package org.stathry.commons.utils;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 * Created by dongdaiming on 2018-11-05 17:56
 */
public class SimpleWeightRandomTest {

    @Test
    public void testPrint() {
        int limit = 1000000;
        long start = System.currentTimeMillis();
        Map<String, Integer> counter = new HashMap<>(4);
        List<String> keyList = Arrays.asList("A", "B", "C", "D");
        List<Integer> weightList = Arrays.asList(1, 2, 3, 4);
        SimpleWeightRandomBuilder<String, Integer> random = new SimpleWeightRandomBuilder<>(keyList, weightList, true);

        String r;
        for (int i = 0; i < limit; i++) {
             r = random.random();
            if (counter.containsKey(r)) {
                counter.put(r, (counter.get(r) + 1));
            } else {
                counter.put(r, 1);
            }
        }

        System.out.println(counter);
        long totalTime = (System.currentTimeMillis() - start);
        System.out.println("cost totalTime:" + totalTime + ", perTime:" + totalTime * 1.0 / limit);

        for(java.util.Map.Entry<String, Integer> e : counter.entrySet()) {
            System.out.println(e.getKey() + ":" + e.getValue() * 1.0 / limit);
        }
    }
}
