package org.test.powermock;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;

/**
 * WhiteboxTest
 * Created by dongdaiming on 2018-12-29 17:07
 */
@RunWith(PowerMockRunner.class)
public class WhiteboxTest {

    //Whitebox可以读取和设置private、static、final域的值
    @Test
    public void testReadFieldByWhiteBox() {
        ArrayList list = new ArrayList();
        Assert.assertEquals(10, ((Integer) Whitebox.getInternalState(ArrayList.class, "DEFAULT_CAPACITY")).intValue());

        Assert.assertEquals(0, ((Integer) Whitebox.getInternalState(list, "modCount")).intValue());
        list.add('a');
        list.add('b');
        Assert.assertEquals(2, ((Integer) Whitebox.getInternalState(list, "modCount")).intValue());
    }

    @Test
    public void testSetFieldByWhiteBox() {
        ArrayList list = new ArrayList();
        Whitebox.setInternalState(list, "modCount", 8);
        Assert.assertEquals(8, ((Integer) Whitebox.getInternalState(list, "modCount")).intValue());

        Object[] objects = new Object[]{"emma"};
        Whitebox.setInternalState(ArrayList.class, "EMPTY_ELEMENTDATA", objects);
        Assert.assertArrayEquals(objects, Whitebox.getInternalState(ArrayList.class, "EMPTY_ELEMENTDATA"));
    }
}
