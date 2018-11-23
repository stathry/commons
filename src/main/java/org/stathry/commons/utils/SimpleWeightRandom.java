package org.stathry.commons.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权重随机
 */
public class SimpleWeightRandom<K extends Comparable, W extends Number> implements WeightRandom {

    private final Map<K, WeightEntry<K, W>> weightMap;

    private double sum = 0.0d;

    public SimpleWeightRandom(Map<K, W> keyWeightMap) {
        if (keyWeightMap == null || keyWeightMap.isEmpty()) {
            throw new IllegalArgumentException("required keyWeightMap.");
        }
        Map<K, WeightEntry<K, W>> map = initWeightMap(keyWeightMap);
        weightMap = MapSortUtils.sortByValueKey(map, false);
    }

    public SimpleWeightRandom(List<K> keyList, List<W> weightList) {
        if (keyList == null || keyList.isEmpty() || weightList == null || keyList.size() != weightList.size()) {
            throw new IllegalArgumentException("required keyList or weightList.");
        }
        Map<K, WeightEntry<K, W>> map = initWeightMap(keyList, weightList);
        weightMap = MapSortUtils.sortByValueKey(map, false);
    }

    private Map<K, WeightEntry<K, W>> initWeightMap(Map<K, W> keyWeightMap) {
        Map<K, WeightEntry<K, W>> map = new HashMap<>();
        K k;
        W w;
        double value, min, max;
        WeightEntry<K, W> entry;
        for (Map.Entry<K, W> e : keyWeightMap.entrySet()) {
            k = e.getKey();
            w = e.getValue();
            value = w.doubleValue();
            if (value < 0) {
                throw new IllegalArgumentException("weight < 0.");
            }
            min = sum;
            sum += value;
            max = sum;
            entry = new WeightEntry<>(k, w, min, max);
            map.put(k, entry);
        }
        return map;
    }

    private Map<K, WeightEntry<K, W>> initWeightMap(List<K> keyList, List<W> weightList) {
        int size = keyList.size();
        Map<K, WeightEntry<K, W>> map = new HashMap<>();

        K k;
        W w;
        double value, min, max;
        WeightEntry<K, W> entry;
        for (int i = 0; i < size; i++) {
            k = keyList.get(i);
            w = weightList.get(i);
            value = w.doubleValue();
            if (value < 0) {
                throw new IllegalArgumentException("weight < 0.");
            }
            min = sum;
            sum += value;
            max = sum;
            entry = new WeightEntry<>(k, w, min, max);
            map.put(k, entry);
        }
        return map;
    }

    @Override
    public K randomKey() {
        double r = sum * Math.random();
        K k = null;
        for (Map.Entry<K, WeightEntry<K, W>> e : weightMap.entrySet()) {
            if (r >= e.getValue().getMin() && r < e.getValue().getMax()) {
                k = e.getKey();
                break;
            }
        }
        return k;
    }

    @Override
    public String toString() {
        return "SimpleWeightRandom{" +
                "weightMap=" + weightMap +
                ", sum=" + sum +
                '}';
    }

    private static class WeightEntry<K, V extends Number> implements Comparable<WeightEntry> {
        private final K key;
        private final V weight;
        private final double min;
        private final double max;

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

        @Override
        public String toString() {
            return "WeightEntry{" +
                    "key=" + key +
                    ", weight=" + weight +
                    '}';
        }

        @Override
        public int compareTo(WeightEntry o) {
            Double weight1 = weight.doubleValue();
            Double weight2 = o.weight.doubleValue();
            return weight1.compareTo(weight2);
        }
    }

}
