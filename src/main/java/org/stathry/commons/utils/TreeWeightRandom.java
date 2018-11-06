package org.stathry.commons.utils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * 权重随机(TreeMap实现)
 */
public class TreeWeightRandom<K extends Comparable, W extends Number> implements WeightRandom {
    
    private TreeMap<Double, K> weightMap = new TreeMap<>();

    public TreeWeightRandom(Map<K, W> weight) {
        if(weight == null || weight.isEmpty()) {
            throw new IllegalArgumentException("randomKey weight is null");
        }
        
        double value, lastWeight;
        for (Entry<K, W> w : weight.entrySet()) {
            value = w.getValue().doubleValue();
                if(value < 0) {
                    throw new IllegalArgumentException("randomKey weight < 0.");
                }
            // 统一转为double
            lastWeight = weightMap.isEmpty() ? value : weightMap.lastKey().doubleValue() + value;
            // 权重累加
            weightMap.put(lastWeight, w.getKey());
        }
    }
    
    public TreeWeightRandom(List<K> keys, List<W> values) {
        if(keys == null || keys.isEmpty() || values == null || values.isEmpty() || keys.size() != values.size()) {
            throw new IllegalArgumentException("illegal randomKey weight");
        }
        
        double value, lastWeight;
        for (int i = 0, size = keys.size(); i < size; i++) {
            value = values.get(i).doubleValue();
            if(value < 0) {
                throw new IllegalArgumentException("randomKey weight < 0.");
            }
            // 统一转为double
            lastWeight = weightMap.isEmpty() ? value : weightMap.lastKey().doubleValue() + value;
            // 权重累加
            weightMap.put(lastWeight, keys.get(i));
        }
    }

    @Override
    public K randomKey() {
        TreeMap<Double, K> wm = weightMap;
        double randomWeight = wm.lastKey() * Math.random();
        return wm.get(wm.tailMap(randomWeight, false).firstKey());
    }

    @Override
    public String toString() {
        return "TreeWeightRandom{" +
                "weightMap=" + weightMap +
                '}';
    }
}
