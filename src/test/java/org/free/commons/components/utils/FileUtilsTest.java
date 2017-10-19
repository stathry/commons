package org.free.commons.components.utils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 * TODO
 * @author dongdaiming
 */
public class FileUtilsTest {

    @Test
    public void test1() throws IOException {
        long start = System.currentTimeMillis();
        
        for(int i = 0; i < 10000; i++) {
            FileUtils.writeStringToFile(new File("/temp2/temp/file" + i + ".txt"), " 中文 content" + i, "utf-8");
        }
        
        System.out.println((System.currentTimeMillis() - start)/1000);
    }
    
    @Test
    public void test2() throws IOException {
        System.out.println(new File("/temp2/temp/file0.txt").length());
        System.out.println(" 中文 content0".getBytes().length);
    }
    

}
