package org.stathry.commons.utils;

/**
 * 权重随机
 * Created by dongdaiming on 2018-11-06 10:19
 */
public interface WeightRandom<K extends Comparable, W extends Number> {

    K randomKey();
}
