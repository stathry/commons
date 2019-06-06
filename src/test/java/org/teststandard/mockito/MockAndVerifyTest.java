package org.teststandard.mockito;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.VerificationInOrderFailure;

import java.util.List;

/**
 * MockAndVerifyTest
 * Created by dongdaiming on 2018-12-28 09:57
 */
public class MockAndVerifyTest {

    // mock的实例并未真正实现该类
    @Test
    public void testMockInstance() {
        List mockedList = Mockito.mock(List.class);
        mockedList.add(1);
        Assert.assertEquals(0, mockedList.size());
        Assert.assertNull(mockedList.get(0));
        Assert.assertNull(mockedList.get(1)); // 非IndexOutOfBoundsException
    }

    // verify用于验证被mock的实例是否调用过该方法
    @Test
    public void testVerifySuccess() {
        List mockedList = Mockito.mock(List.class);
        mockedList.add(1);
        mockedList.get(0);
        mockedList.clear();

        Mockito.verify(mockedList).add(1);
        Mockito.verify(mockedList).get(0);
        Mockito.verify(mockedList).clear();
    }

    @Test
    public void testVerifyInvokeTimes() {
        List mockedList = Mockito.mock(List.class);
        mockedList.add(1);
        mockedList.add(1);
        mockedList.get(0);

//         Mockito.verify(mockedList).add(1); // fail
//         Mockito.verify(mockedList).add(0); // fail
        Mockito.verify(mockedList, Mockito.times(2)).add(1);
        Mockito.verify(mockedList, Mockito.atLeastOnce()).add(1);
        Mockito.verify(mockedList, Mockito.atLeast(1)).get(0);
        Mockito.verify(mockedList, Mockito.atMost(1)).get(0);
    }

    @Test
    public void testVerifyOrder1() {
        List list1 = Mockito.mock(List.class);
        List list2 = Mockito.mock(List.class);

        InOrder order = Mockito.inOrder(list1, list2);

        list1.add(0);
        list2.add(0);

        order.verify(list1).add(0);
        order.verify(list2).add(0);
    }

    @Test(expected = VerificationInOrderFailure.class)
    public void testVerifyOrder2() {
        List list1 = Mockito.mock(List.class);
        List list2 = Mockito.mock(List.class);

        InOrder order = Mockito.inOrder(list1, list2);

        list2.add(0);
        list1.add(0);

        order.verify(list1).add(0);
        order.verify(list2).add(0);
    }

    @Test
    public void testVerifyInteractions() {
        List list1 = Mockito.mock(List.class);
        List list2 = Mockito.mock(List.class);
        List list3 = Mockito.mock(List.class);

        list1.add(0);
        list2.add(0);
        Mockito.verify(list1).add(0);
        Mockito.verify(list1, Mockito.never()).add(100);
        Mockito.verifyZeroInteractions(list3);
    }

}
