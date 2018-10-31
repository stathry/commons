package com.google.common.collect;

import com.google.common.primitives.Primitives;
import org.junit.Test;

/**
 * TODO
 * Created by dongdaiming on 2018-10-29 13:49
 */
public class PrimitivesTest {

    @Test
    public void testAllPri() {
        System.out.println(Primitives.allPrimitiveTypes());
        System.out.println(Primitives.allWrapperTypes());
    }
}
