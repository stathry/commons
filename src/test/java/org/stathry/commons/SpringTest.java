package org.stathry.commons;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.stathry.commons.bean.ReplaceableBean;
import org.stathry.commons.utils.ApplicationContextUtils;

import javax.sql.DataSource;

/**
 * SpringTest
 * Created by dongdaiming on 2018-06-05 14:20
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:spring-context.xml")
public class SpringTest {

    @Test
    public void test() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
        DataSource bean = context.getBean(DataSource.class);
        System.out.println(bean.getClass().getName());
        Assert.assertNotNull(bean);
    }

    @Test
    public void testStart() {
        System.out.println("spring started.");
        System.out.println(System.getProperties());
        Assert.assertEquals("conf/redis.properties", System.getProperty("redis.properties.filename"));
        Assert.assertTrue(!System.getProperties().isEmpty());
    }

        @Test
    public void testGetBeanByNameAndType() {
        DruidDataSource bean = ApplicationContextUtils.getBean("dataSource", DruidDataSource.class);
        System.out.println(bean);
        Assert.assertNotNull(bean);
    }

    @Test
    public void testHelloMethodReplacer() {
        ReplaceableBean bean = ApplicationContextUtils.getBean("replaceableBean1", ReplaceableBean.class);
        Assert.assertEquals("hi world", bean.hello("world"));
    }

    @Test
    public void testSrcHelloMethod() {
        ReplaceableBean bean = ApplicationContextUtils.getBean("replaceableBean2", ReplaceableBean.class);
        Assert.assertEquals("hello world", bean.hello("world"));
    }

}
