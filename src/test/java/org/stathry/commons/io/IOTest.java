package org.stathry.commons.io;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * TODO
 * Created by dongdaiming on 2018-06-05 09:45
 */
public class IOTest {

    private static final int W_LIMIT = 1000_0000;
    private static final String LINE_SEP = SystemUtils.LINE_SEPARATOR;

    // testBufferedWriter1 > testIOUWrite1 > testFileUWrite1
    // testBufferedWriter1:1698,1514,1604
    @Test
    public void testBufferedWriter1() throws IOException {
        String sep = LINE_SEP;
        File f = File.createTempFile("br-", ".txt", new File("/temp/io"));
        BufferedWriter br = new BufferedWriter(new FileWriter(f));
        long begin = System.currentTimeMillis();
        for (int i = 0; i < W_LIMIT; i++) {
            br.write(new StringBuilder().append(i).append(sep).toString());
        }
        long end = System.currentTimeMillis();
        System.out.println("testBufferedWriter1:" + f.getPath());
        System.out.println("testBufferedWriter1:" + (end - begin));
        br.close();
    }

    // testBufferedWriter1:超级慢
    @Test
    public void testFileUWrite1() throws IOException {
        String sep = LINE_SEP;
        File f = File.createTempFile("br-", ".txt", new File("/temp/io"));
        long begin = System.currentTimeMillis();
        for (int i = 0; i < W_LIMIT; i++) {
            FileUtils.write(f, new StringBuilder().append(i).append(sep).toString(), "utf-8", true);
        }
        long end = System.currentTimeMillis();
        System.out.println("testFileUWrite1:" + f.getPath());
        System.out.println("testFileUWrite1:" + (end - begin));
    }

    // testIOUWrite1:2509,2411,2325
    @Test
    public void testIOUWrite1() throws IOException {
        String sep = LINE_SEP;
        File f = File.createTempFile("br-", ".txt", new File("/temp/io"));
        FileOutputStream out = new FileOutputStream(f);
        BufferedOutputStream br = new BufferedOutputStream(out);
        long begin = System.currentTimeMillis();
        for (int i = 0; i < W_LIMIT; i++) {
            IOUtils.write(new StringBuilder().append(i).append(sep).toString(), br, "utf-8");
        }
        long end = System.currentTimeMillis();
        System.out.println("testIOUWrite1:" + f.getPath());
        System.out.println("testIOUWrite1:" + (end - begin));
        out.close();
        br.close();
    }

}
