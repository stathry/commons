package org.stathry.commons.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权重随机
 */
public class SimpleWeightRandomBuilder<K, W extends Number> {

    private Map<K, WeightEntry<K, W>> weightMap;
    
    private double sum = 0.0d;
    
    private final boolean startOpen;

    @Override
    public String toString() {
        return "SimpleWeightRandomBuilder{" +
                "weightMap=" + weightMap +
                ", sum=" + sum +
                ", startOpen=" + startOpen +
                '}';
    }

    public SimpleWeightRandomBuilder(List<K> keyList, List<W> weightList, boolean startOpen) {
        if (keyList == null || keyList.isEmpty() || weightList == null || keyList.size() != weightList.size()) {
            throw new IllegalArgumentException("required keyList or weightList.");
        }
        this.startOpen = startOpen;
        int size = keyList.size();
        weightMap = new HashMap<>(size * 2);

        K k;
        W w;
        double value, min, max;
        WeightEntry<K, W> entry;
        for (int i = 0; i < size; i++) {
            k = keyList.get(i);
            w = weightList.get(i);
            value = weightList.get(i).doubleValue();
            min = sum;
            sum += value;
            max = sum;
            entry = new WeightEntry<>(k, w, min, max);
            weightMap.put(k, entry);
        }
    }

    public K random() {
        double r = sum * Math.random();
        for (Map.Entry<K, WeightEntry<K, W>> e : weightMap.entrySet()) {
            if (startOpen && r >= e.getValue().getMin() && r < e.getValue().getMax()) {
                return e.getKey();
            } else if (r > e.getValue().getMin() && r <= e.getValue().getMax()) {
                return e.getKey();
            }
        }
        return null;
    }

    static class WeightEntry<K, V extends Number> {
        private K key;
        private V weight;
        private double min;
        private double max;

        public WeightEntry(K key, V weight, double min, double max) {
            super();
            this.key = key;
            this.weight = weight;
            this.min = min;
            this.max = max;
        }

        public K getKey() {
            return key;
        }

        public V getWeight() {
            return weight;
        }

        public double getMin() {
            return min;
        }

        public double getMax() {
            return max;
        }
        
        public static void main(String[] args) {

        }

    }

}
