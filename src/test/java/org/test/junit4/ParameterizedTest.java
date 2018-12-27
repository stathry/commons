package org.test.junit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.stathry.commons.utils.ParamNameUtils;

import java.util.Arrays;
import java.util.Collection;

/**
 * ParameterizedTest
 * Created by dongdaiming on 2018-12-26 19:00
 */
@RunWith(Parameterized.class)
public class ParameterizedTest {

    private String param;
    private String expectedResult;

    public ParameterizedTest(String expectedResult, String param) {
        this.expectedResult = expectedResult;
        this.param = param;
    }

    // 参数和预期结果的顺序与构造函数应一致
    @Parameterized.Parameters
    public static Collection params() {
        return Arrays.asList(
                new Object[][]{
                        {"", null},
                        {"", ""},
                        {"employee_info", "employee_info"},
                        {"employee_info", "employeeInfo"},
                        {"employee_a_info", "employeeAInfo"}

        }
        );
    }

    @Test
    public void testToUnderName() {
        Assert.assertEquals(expectedResult, ParamNameUtils.toUnderName(param));
    }
}
