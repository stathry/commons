package org.testframework.assertj;

import com.google.common.collect.ImmutableMap;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * AssertJ常见数据类型断言测试
 *
 * @author dongdaiming(董代明)
 * @date 2019-06-13 13:53
 */
public class AssertJDataAssertTest {

    @Test
    public void test() {
    int n = (99 / 2) * 1 / 10 * 24 + (99 / 2) * 1 % 10;
        System.out.println(n);
        System.out.println(105/24);
        System.out.println(105 % 24);
    }

    @Test
    public void testStringAssert() {
        String str = "AssertJ";
        String c = "Z";
        Assertions.assertThat(str).isNotBlank();
        Assertions.assertThat(str).startsWith("Ass");
        Assertions.assertThat(str).contains("ss");
        Assertions.assertThat(str).matches("A\\w+J");

        Assertions.assertThat(c).isIn("X", "Z");
        Assertions.assertThat(c).isEqualToIgnoringCase("z");
    }

    @Test
    public void testNumberAssert() {
        int n = 1024;

        Assertions.assertThat(n).isEqualTo(1024);
        Assertions.assertThat(n).isGreaterThanOrEqualTo(1024);
        Assertions.assertThat(n).isLessThan(1025);
        Assertions.assertThat(n).isPositive();
        Assertions.assertThat(n).isBetween(1023, 1025);
        Assertions.assertThat(n).isIn(1024, 1000);
    }

    @Test
    public void testCollectionAssert() {
        Collection<Integer> collection = Arrays.asList(6, 8, 10);

        Assertions.assertThat(collection).isNotEmpty().hasSize(3);
        Assertions.assertThat(collection).size().isBetween(2, 4);
        Assertions.assertThat(collection).first().isEqualTo(6);
        Assertions.assertThat(collection).last().isEqualTo(10);
        Assertions.assertThat(collection).containsAnyOf(8, 88);
        Assertions.assertThat(collection).contains(6, 10);
        Assertions.assertThat(collection).containsOnly(6, 8, 10);
    }

    @Test
    public void testMapAssert() {
        Map<Integer, String> map = ImmutableMap.of(6, "v6", 8, "v8", 10, "v10");

        Assertions.assertThat(map).isNotEmpty().hasSize(3);
        Assertions.assertThat(map).size().isGreaterThan(2);
        Assertions.assertThat(map).containsKeys(6, 8);
        Assertions.assertThat(map).containsOnlyKeys(10, 8 , 6);
        Assertions.assertThat(map).containsValues("v8", "v6");
        Assertions.assertThat(map).containsOnly(Assertions.entry(6, "v6"), Assertions.entry(8, "v8"), Assertions.entry(10, "v10"));

    }

}
