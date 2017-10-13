package org.free.commons.components.utils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 权重随机
 */
public class WeightRandom<K, V extends Number> {
    
    private TreeMap<Double, K> weightMap = new TreeMap<Double, K>();

    public WeightRandom(Map<K, V> weight) {
        if(weight == null || weight.isEmpty()) {
            throw new IllegalArgumentException("random weight is null");
        }
        
        double value = 0;
        for (Entry<K, V> w : weight.entrySet()) {
            value = w.getValue().doubleValue();
                if(value < 0) {
                    throw new IllegalArgumentException("random weight < 0.");
                }
            // 统一转为double
            double lastWeight = weightMap.isEmpty() ? 0 : weightMap.lastKey().doubleValue();

            // 权重累加
            weightMap.put(value + lastWeight, w.getKey());
        }
    }
    
    public WeightRandom(List<K> keys, List<V> values) {
        if(keys == null || keys.isEmpty() || values == null || values.isEmpty() || keys.size() != values.size()) {
            throw new IllegalArgumentException("illegal random weight");
        }
        
        double value = 0;
        for (int i = 0; i < keys.size(); i++) {
            value = values.get(i).doubleValue();
            if(value < 0) {
                throw new IllegalArgumentException("random weight < 0.");
            }
            // 统一转为double
            double lastWeight = weightMap.isEmpty() ? 0 : weightMap.lastKey().doubleValue();
            
            // 权重累加
            weightMap.put(value + lastWeight, keys.get(i));
        }
    }

    public K random() {
        double randomWeight = weightMap.lastKey() * Math.random();
        SortedMap<Double, K> tailMap = weightMap.tailMap(randomWeight, false);
        return weightMap.get(tailMap.firstKey());
    }

}
