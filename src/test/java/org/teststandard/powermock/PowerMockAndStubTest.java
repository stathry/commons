package org.teststandard.powermock;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.teststandard.IdUtils;
import org.teststandard.UserService;
import org.teststandard.UserServiceImpl;

import java.util.Iterator;
import java.util.List;


/**
 * PowerMockAndStubTest
 * Created by dongdaiming on 2018-12-29 14:31
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(IdUtils.class)
public class PowerMockAndStubTest {

    @Spy
    private UserService userService = new UserServiceImpl();

    @Test
    public void testStub() {
        List mockedList = Mockito.mock(List.class);
        Mockito.when(mockedList.get(0)).thenReturn("aa");
        Mockito.when(mockedList.get(1)).thenReturn("bb");
        Assert.assertEquals("aa", mockedList.get(0));
        Assert.assertEquals("bb", mockedList.get(1));

        List mockedList2 = PowerMockito.mock(List.class);
        Mockito.when(mockedList2.get(0)).thenReturn("cc");
        Mockito.when(mockedList2.get(1)).thenReturn("dd");
        Assert.assertEquals("cc", mockedList2.get(0));
        Assert.assertEquals("dd", mockedList2.get(1));
    }

    @Test
    public void testStubAndThenReturns() {
        Iterator itr = PowerMockito.mock(Iterator.class);
        Mockito.when(itr.next()).thenReturn("hello!").thenReturn("hello,world!").thenReturn("hello, 2019!");

        Assert.assertEquals("hello!", itr.next());
        Assert.assertEquals("hello,world!", itr.next());
        Assert.assertEquals("hello, 2019!", itr.next());
        Assert.assertEquals("hello, 2019!", itr.next());
    }

    @Test
    public void testMockStaticMethod() {
        PowerMockito.mockStatic(IdUtils.class);
        PowerMockito.when(IdUtils.id()).thenReturn("666");
        Assert.assertEquals("666", userService.reg("u1", "p1"));
        PowerMockito.verifyStatic(IdUtils.class);
    }

    @Test
    public void testMockPrivateMethod() throws Exception {
        IdUtils idUtils = PowerMockito.spy(new IdUtils());
        PowerMockito.when(idUtils, "pid", true).thenReturn("888");
        Assert.assertEquals("hello-888", idUtils.pubId(true));
        Assert.assertFalse("hello-888".equals(idUtils.pubId(false)));
    }

    @Test
    public void testMockFinalMethod() throws Exception {
        IdUtils idUtils = PowerMockito.spy(new IdUtils());
        PowerMockito.when(idUtils, "fId").thenReturn("haha");
        Assert.assertEquals("haha", idUtils.fId());
    }

    @Test
    public void testMockNewMethod() throws Exception {
        IdUtils inId = new IdUtils("invalid");
        IdUtils niceId = new IdUtils("nice");
        PowerMockito.whenNew(IdUtils.class).withArguments("god").thenReturn(inId);
        PowerMockito.whenNew(IdUtils.class).withArguments("genius").thenReturn(niceId);

        Assert.assertEquals("invalid", new IdUtils("god").showMe());
        Assert.assertEquals("nice", new IdUtils("genius").showMe());
        Assert.assertNull(new IdUtils("coder"));
    }

}
