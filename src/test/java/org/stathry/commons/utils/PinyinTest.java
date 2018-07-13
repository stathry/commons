package org.stathry.commons.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import org.junit.Test;

import java.util.Arrays;

/**
 * TODO
 * Created by dongdaiming on 2018-07-02 10:22
 */
public class PinyinTest {

    @Test
    public void testToPinyin() {
        String[] a = PinyinHelper.toHanyuPinyinStringArray('单');
        System.out.println(Arrays.toString(a));
    }
}
