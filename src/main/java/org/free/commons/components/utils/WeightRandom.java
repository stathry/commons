package org.free.commons.components.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 权重随机
 */
public class WeightRandom<K, V extends Number> {
    
    private TreeMap<Double, K> weightMap = new TreeMap<Double, K>();

    public WeightRandom(LinkedHashMap<K, V> weight) {
        if(weight == null || weight.isEmpty()) {
            throw new IllegalArgumentException("weight is null");
        }
        
        for (Entry<K, V> w : weight.entrySet()) {
            // 统一转为double
            double lastWeight = weightMap.isEmpty() ? 0 : weightMap.lastKey().doubleValue();

            // 权重累加
            weightMap.put(w.getValue().doubleValue() + lastWeight, w.getKey());
        }
    }

    public K random() {
        double randomWeight = weightMap.lastKey() * Math.random();
        SortedMap<Double, K> tailMap = weightMap.tailMap(randomWeight, false);
        return weightMap.get(tailMap.firstKey());
    }
    
    public static void main(String[] args) {
        int times = 1000000;
        long start = System.currentTimeMillis();
        LinkedHashMap<Integer, Double> weight = new LinkedHashMap<>(4);
        weight.put(1, 0.1);
        weight.put(2, 0.2);
        weight.put(3, 0.3);
        weight.put(4, 0.4);
        WeightRandom<Integer, Double> random = new WeightRandom<Integer, Double>(weight);
        
        Map<Integer,Integer> counter = new HashMap<>(4);
        Integer r;
        for (int i = 0; i < times; i++) {
            r = random.random();
            if(counter.containsKey(r)) {
                counter.put(r, (counter.get(r) + 1));
            } else {
                counter.put(r, 1);
            }
        }
        System.out.println("1:" + counter.get(1) * 1.0 / times);
        System.out.println("2:" + counter.get(2) * 1.0 / times);
        System.out.println("3:" + counter.get(3) * 1.0 / times);
        System.out.println("4:" + counter.get(4) * 1.0 / times);
        System.out.println("cost time in ms:" + (System.currentTimeMillis() - start));
    }

}
