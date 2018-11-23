package org.stathry.commons.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * TODO
 *
 * @author dongdaiming
 */
public class FileUtilsTest {

    @Test
    public void testReadJSONStr() throws IOException {
        String s = FileUtils.readFileToString(new File("/temp/taobaoOData.txt"), "utf-8");
        System.out.println(s);
        JSONObject j = JSON.parseObject(s);
        System.out.println();
        System.out.println(j.toJSONString());
    }

    @Test
    public void test1() throws IOException {
        long start = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            FileUtils.writeStringToFile(new File("/temp2/temp/file" + i + ".txt"), " 中文 content" + i, "utf-8");
        }

        System.out.println((System.currentTimeMillis() - start) / 1000);
    }

    @Test
    public void test12() throws IOException {
        long start = System.currentTimeMillis();
        File f = new File("/temp/a.txt");
        String s = "给定一个直接字节缓冲区，Java 虚拟机将尽最大努力直接对它执行本机 I/O 操作。也就是说，它会在每一次调用底层操作系统的本机 I/O 操作之前(或之后)，尝试避免将缓冲区的内容拷贝到一个中间缓冲区中(或者从一个中间缓冲区中拷贝数据)";
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < 1000000; i++) {
            b.append(s);
            if (i % 1000 == 0 && i != 0) {
                FileUtils.writeStringToFile(f, b.toString(), "utf-8", true);
                b.setLength(0);
            }
        }

        System.out.println((System.currentTimeMillis() - start) / 1000);
    }

    @Test
    public void test2() throws IOException {
        System.out.println(new File("/temp2/temp/file0.txt").length());
        System.out.println(" 中文 content0".getBytes().length);
    }


}
