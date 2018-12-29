package org.test.mockito;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.matchers.GreaterThan;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * MockStubTest
 * Created by dongdaiming on 2018-12-28 09:57
 */
public class MockStubTest {

    @Test
    public void testStub1() {
        List mockedList = Mockito.mock(List.class);
        Mockito.when(mockedList.add("a")).thenReturn(true);
        Mockito.when(mockedList.get(0)).thenReturn("aa");
        Assert.assertTrue(mockedList.add("a"));
        Assert.assertEquals("aa", mockedList.get(0));
    }

    @Test
    public void testStubAndThenReturns() {
        Iterator itr = Mockito.mock(Iterator.class);
        Mockito.when(itr.next()).thenReturn("hello!").thenReturn("hello,world!").thenReturn("hello, 2019!");

        Assert.assertEquals("hello!", itr.next());
        Assert.assertEquals("hello,world!", itr.next());
        Assert.assertEquals("hello, 2019!", itr.next());
        Assert.assertEquals("hello, 2019!", itr.next());
    }

    // stub会覆盖
    @Test
    public void testStubDuplicate() {
        List list = Mockito.mock(List.class);
        Mockito.when(list.get(0)).thenReturn(666);
        Mockito.when(list.get(0)).thenReturn(888);
        Assert.assertEquals(888, list.get(0));
    }

    @Test
    public void testStubSmart() {
        Map map = Mockito.mock(Map.class, Mockito.RETURNS_SMART_NULLS);
        Mockito.when(map.get("a")).thenReturn("对A");
        Mockito.when(map.get("b")).thenReturn("nbb");

        Assert.assertEquals("对A", map.get("a"));
        Assert.assertEquals("nbb", map.get("b"));
        Assert.assertEquals(0, map.size());
        Assert.assertNotNull(map.get("c"));
    }

    @Test(expected = EOFException.class)
    public void testStubException() throws IOException {
        FileReader in = Mockito.mock(FileReader.class);
        Mockito.when(in.read()).thenThrow(new EOFException());
        in.read();
    }

    @Test
    public void testDeepStub() {
        Calendar c = Mockito.mock(Calendar.class);
        Mockito.when(c.getTime().getTime()).thenReturn(666L);
        Assert.assertEquals(666, c.getTime().getTime());

        Calendar c2 = Mockito.mock(Calendar.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(c2.getTime().getTime()).thenReturn(888L);
        Assert.assertEquals(888, c2.getTime().getTime());
    }

    // matcher会覆盖，所以stub时的范围应该先大后小
    @Test
    public void testMatcherStub1() {
        Map map = Mockito.mock(Map.class);
        Mockito.when(map.get(Mockito.anyInt())).thenReturn(666);
        Mockito.when(map.get(0)).thenReturn(888);
        Assert.assertEquals(666, (int)map.get(6));
        Assert.assertEquals(666, (int)map.get(8));
        Assert.assertEquals(888, (int)map.get(0));

        Map map2 = Mockito.mock(Map.class);
        Mockito.when(map2.get(Mockito.anyString())).thenReturn(888);
        Mockito.when(map2.get("girl")).thenReturn(666);
        Assert.assertEquals(888, (int)map2.get("a"));
        Assert.assertEquals(888, (int)map2.get("b"));
        Assert.assertEquals(666, (int)map2.get("girl"));
    }

    @Test(expected = InvalidUseOfMatchersException.class)
    public void testMatcherOnlyOne() {
        List list = Mockito.mock(List.class);
        int ge = Matchers.intThat(new GreaterThan<>(100));
        Mockito.when(list.get(Mockito.anyInt())).thenReturn(0);
        Mockito.when(list.get(ge)).thenReturn(100);
    }

    @Test
    public void testMatcherStubGreaterThan() {
        List list = Mockito.mock(List.class);
        int ge = Matchers.intThat(new GreaterThan<>(100));
        Mockito.when(list.get(ge)).thenReturn(100);

        Assert.assertEquals(100, list.get(888));
        Assert.assertEquals(100, list.get(666));
        Assert.assertEquals(null, list.get(0));
    }

    @Test
    public void testMatcherStubArgThat() {
        Map map = Mockito.mock(Map.class);
        Mockito.when(map.get(Matchers.argThat(new LessInt(80)))).thenReturn("B");

        Assert.assertEquals("B", map.get(0));
        Assert.assertEquals("B", map.get(60));
        Assert.assertEquals(null, map.get(90));
    }

    @Test
    public void testMatcherStubImplAnswer() {
        Map map = Mockito.mock(Map.class);
        Mockito.when(map.get(Mockito.anyInt())).thenAnswer(new SimpleScoreAnswer());

        Assert.assertEquals("N", map.get(0));
        Assert.assertEquals("N", map.get(30));
        Assert.assertEquals("Y", map.get(60));
        Assert.assertEquals("Y", map.get(80));
    }

    @Test
    public void testMatcherStubDefaultAnswer() {
        Map map = Mockito.mock(Map.class, new SimpleScoreAnswer());
        Mockito.when(map.get(0)).thenReturn("QAQ");
        Mockito.when(map.get(100)).thenReturn("OMG");

        Assert.assertEquals("QAQ", map.get(0));
        Assert.assertEquals("OMG", map.get(100));
        Assert.assertEquals("N", map.get(30));
        Assert.assertEquals("Y", map.get(60));
        Assert.assertEquals("Y", map.get(80));
    }

    // spy可用于局部模拟(模拟指定参数时返回值，未指定参数时则执行真实的方法实现)
    @Test
    public void testPartMock() {
        List<Integer> list0 = new ArrayList<>();
        List<Integer> list = Mockito.spy(list0);
        Mockito.when(list.size()).thenReturn(8);

        Assert.assertEquals(8, list.size());

        Exception e = null;
        try {
            // when的时候就会调用真实的方法，所以会抛异常
            Mockito.when(list.get(0)).thenReturn(8);
        } catch (Exception e1) {
            e = e1;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(e.getClass(), IndexOutOfBoundsException.class);

        list.add(666);
        Assert.assertEquals(666, list.get(0).intValue());
    }

    @Test
    public void testSpy() {
        List<Integer> spyList = Mockito.spy(new ArrayList<>());
        Mockito.when(spyList.get(0)).thenReturn(666);
        Assert.assertEquals(666, spyList.get(0).intValue());
        spyList.get(1);
    }

    @Test
    public void testStubReset() {
        List list = Mockito.mock(List.class);
        Mockito.when(list.get(0)).thenReturn(666);
        Mockito.when(list.get(1)).thenReturn(888);

        Assert.assertEquals(888, list.get(1));
        Assert.assertEquals(666, list.get(0));

        Mockito.reset(list);
        Assert.assertNull(list.get(1));
        Assert.assertNull(list.get(0));
    }

    public static class SimpleScoreAnswer implements Answer<String> {

        @Override
        public String answer(InvocationOnMock invocation) {
            int score = (Integer) invocation.getArguments()[0];
            return score < 60 ? "N" : "Y";
        }
    }

    public static class LessInt extends ArgumentMatcher<Integer> {

        private int n;

        public LessInt(int n) {
            this.n = n;
        }

        @Override
        public boolean matches(Object argument) {
            return argument != null && argument instanceof Integer && ((Integer)argument) < n;
        }
    }

}
