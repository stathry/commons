package org.junit4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * 使用测试套件进行批量测试
 * Created by dongdaiming on 2018-12-26 17:54
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({BeforeAfterRunOrderTest.class, AssertTest.class})
public class SuiteTest {
}
