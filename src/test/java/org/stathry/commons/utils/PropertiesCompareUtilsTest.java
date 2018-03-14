package org.stathry.commons.utils;

import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * TODO
 *
 * @date 2018/3/14
 */
public class PropertiesCompareUtilsTest {

    public static boolean compareProperties(String path1, String path2) throws IOException {
        Properties prop1 = PropertiesLoaderUtils.loadProperties(new FileSystemResource(path1));
        Properties prop2 = PropertiesLoaderUtils.loadProperties(new FileSystemResource(path2));
        TreeMap<String, String> t1 = new TreeMap<String, String>((java.util.Map) prop1);
        TreeMap<String, String> t2 = new TreeMap<String, String>((java.util.Map) prop2);

        TreeMap<String, String> maxMap = t1.size() >= t2.size() ? t1 : t2;
        System.out.println("maxMap is " + (t1.size() >= t2.size() ? path1 : path2));
        System.out.println("size1=" + t1.size() + ",size2=" + t2.size());
        boolean same = false;
        boolean allSame = true;
        for (Map.Entry<String, String> e : maxMap.entrySet()) {
            same = e.getValue().equals(t2.get(e.getKey()));
            allSame = !same ? false : allSame;
            if (!same) {
                allSame = false;
                System.out.println("K=" + e.getKey() + ",same=" + same + ",maxV=" + e.getValue() + ",V=" + t2.get(e.getKey()));
            } else {
//            System.out.println("K=" + e.getKey() + ",same=" + same);
            }
        }
        allSame = allSame ? t1.size() == t2.size() : allSame;
        return allSame;
    }

    @Test
    public void testCompareProperties() throws IOException {
//        boolean allSame = compareProperties("/temp/galaxyETL-config-prod.properties", "/temp/galaxyETL-config-test.properties");
        boolean allSame = compareProperties("/temp/galaxy-config-test.properties", "/temp/galaxy-config-prod.properties");
        System.out.println();
        System.out.println("allSame=" + allSame);
    }

}