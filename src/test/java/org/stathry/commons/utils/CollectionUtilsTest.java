package org.stathry.commons.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtilsTest {

    @Test
    public void testListToArray() {
        List<String> list = new ArrayList<String>();
        list.add("aa");
        list.add("bb");
        String[] array = CollectionUtils.listToArray(list, String.class);
        System.out.println("arrays.length: " + array.length);
        System.out.println("array[0]: " + array[0]);
        System.out.println("array[1]: " + array[1]);
    }

    @Test
    public void testListsToArrays() {
        List<String> list = new ArrayList<String>();
        list.add("aa");
        list.add("bb");
        List<String> list2 = new ArrayList<String>();
        list2.add("xy");
        list2.add("z");
        List<List<String>> data = new ArrayList<List<String>>();
        data.add(list);
        data.add(list2);
        String[][] arrays = CollectionUtils.listsToArrays(data, String.class);
        System.out.println("arrays.length: " + arrays.length);
        System.out.println("arrays[0][0]: " + arrays[0][0]);
        System.out.println("arrays[1][1]: " + arrays[1][1]);
    }


}
