package org.test.testng;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.stathry.commons.utils.FileUtils;
import org.stathry.commons.utils.ParamNameUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Date;

/**
 * ParameterizedTest
 * Created by dongdaiming on 2018-12-26 19:00
 */
public class TestNGParameterizedTest {

    // 参数和预期结果的顺序与构造函数应一致
    @DataProvider(name = "toUnderNameParams")
    public Object[][] params() {
        return new Object[][]{
                        {"", null},
                        {"", ""},
                        {"employee_info", "employee_info"},
                        {"employee_info", "employeeInfo"},
                        {"employee_a_info", "employeeAInfo"}

        };
    }

    @Test(dataProvider = "toUnderNameParams")
    public void testToUnderName(String result, String param) {
        System.out.println("running " + Thread.currentThread().getStackTrace()[1].getMethodName()
                + ", at " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        Assert.assertEquals(result, ParamNameUtils.toUnderName(param));
        System.out.println("param:" + param + ", exp:" + result);
    }

    @Test
    public void testReadCSV() throws IOException {
        System.out.println("running " + Thread.currentThread().getStackTrace()[1].getMethodName()
                + ", at " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        Object[][] data = FileUtils.readTextData("/excel/case1.csv");
        Assert.assertNotNull(data);
        System.out.println(JSON.toJSONString(data));
    }

    @Test
    public void testReadCSVByCaseName() throws IOException {
        System.out.println("running " + Thread.currentThread().getStackTrace()[1].getMethodName()
                + ", at " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        Object[][] data = FileUtils.readTextData("/excel/case2.csv", "case2");
        Assert.assertNotNull(data);
        System.out.println(JSON.toJSONString(data));

        Object[][] data1 = FileUtils.readTextData("/excel/case2.csv", "case1");
        Assert.assertNotNull(data1);
        System.out.println(JSON.toJSONString(data1));
    }

    @DataProvider(name = "toUnderNameParams-csv")
    public Object[][] csvParams() {
        System.out.println("running " + Thread.currentThread().getStackTrace()[1].getMethodName()
                + ", at " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        try {
            return FileUtils.readTextData("/excel/case1.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test(dataProvider = "toUnderNameParams-csv")
    public void testToUnderName2(String result, String param) {
        System.out.println("running " + Thread.currentThread().getStackTrace()[1].getMethodName()
                + ", at " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));

        Assert.assertEquals(result, ParamNameUtils.toUnderName(param));

        System.out.println("param:" + param + ", exp:" + result);
    }

    @DataProvider(name = "case2")
    public Object[][] csvParamsCase2() {
        System.out.println("running " + Thread.currentThread().getStackTrace()[1].getMethodName()
                + ", at " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        try {
            return FileUtils.readTextData("/excel/case2.csv", "case2");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test(dataProvider = "case2")
    public void testToUnderNameCase2(String result, String param) {
//        System.out.println("running " + Thread.currentThread().getStackTrace()[1].getMethodName()
//                + ", at " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));

        Assert.assertEquals(result, ParamNameUtils.toUnderName(param));

        System.out.println("param:" + param + ", exp:" + result);
    }

}
