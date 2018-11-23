package org.stathry.commons.io;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author dongdaiming
 * @date 2017年12月28日
 */
public class LongFileRWTest {

    private static final String lineSep = System.lineSeparator();
    private static final int tn = 20;
    private static final int rn = 10000;
    private static final int bloking = 100000000;
    private static final int lines = 10000;

//	testWriteLongFile-tn-20-rn10000-sec70
//	testWriteLongFileBatch-tn-20-rn10000-sec16


    @Test
    public void testWriteLongFile() throws InterruptedException {
        long start = System.currentTimeMillis();
        File f = new File("/temp/iotest/LOAN1.txt");
        if (f.exists()) {
            f.delete();
        }
        String s = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
        ExecutorService exec = new ThreadPoolExecutor(0, tn, 60, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(bloking));
        for (int i = 0; i < tn; i++) {
            final int idx = i;
            exec.submit(() -> {
                try {
                    for (int j = 0; j < rn; j++) {
                        FileUtils.write(f, s + "_" + idx + "_" + j + lineSep, "utf-8", true);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        exec.shutdown();

        exec.awaitTermination(30, TimeUnit.MINUTES);
        long end = System.currentTimeMillis();
        System.out.println("testWriteLongFile-tn-" + tn + "-rn" + rn + "-sec" + TimeUnit.MILLISECONDS.toSeconds(end - start));
        // write1/write2= 559.89/76.112 = 7.36
    }

    // 批次写入
    @Test
    public void testWriteLongFileBatch() throws InterruptedException {
        long start = System.currentTimeMillis();
        File f = new File("/temp/iotest/LOAN2.txt");
        if (f.exists()) {
            f.delete();
        }
        String s = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
        ExecutorService exec = new ThreadPoolExecutor(0, tn, 60, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(bloking));
        for (int i = 0; i < tn; i++) {
            final int idx = i;
            exec.submit(() -> {
                StringBuilder sb = new StringBuilder();
                try {
                    for (int j = 0; j < rn; j++) {
                        sb.append(s);
                        sb.append("_");
                        sb.append(idx);
                        sb.append("_");
                        sb.append(j);
                        sb.append(lineSep);
                        if (j % 100 == 0 && j != 0) {
                            FileUtils.write(f, sb.toString(), "utf-8", true);
                            sb.setLength(0);
                        }
                    }
                    if (sb.length() > 0) {
                        FileUtils.write(f, sb.toString(), "utf-8", true);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        exec.shutdown();
        exec.awaitTermination(30, TimeUnit.MINUTES);
        long end = System.currentTimeMillis();
        System.out.println("testWriteLongFileBatch-tn-" + tn + "-rn" + rn + "-sec" + TimeUnit.MILLISECONDS.toSeconds(end - start));
    }

    @Test
    public void testSpiltLongFileJDK() {
        long start = System.currentTimeMillis();
        File f = new File("/temp/iotest/LOAN1.txt");
        File dir = new File("/temp/iotest/LOAN1");
        long i = 0;
        File fi = null;
        String name = FilenameUtils.getBaseName(f.getName());
        String s;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f)))) {
            while ((s = in.readLine()) != null) {
                if (i % lines == 0) {
                    fi = File.createTempFile(name, ".txt", dir);
                }
                FileUtils.write(fi, s, "utf-8", true);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start) * 1.0 / 1000);
    }

    @Test
    public void testSpiltLongFileApache() {
        long start = System.currentTimeMillis();
        File f = new File("/temp/iotest/LOAN1.txt");
        File dir = new File("/temp/iotest/LOAN1");
        long i = 0;
        File fi = null;
        String name = FilenameUtils.getBaseName(f.getName());
        LineIterator it = null;
        try {
            it = IOUtils.lineIterator(new FileInputStream(f), "utf-8");
            while (it.hasNext()) {
                if (i % lines == 0) {
                    fi = File.createTempFile(name, ".txt", dir);
                }
                FileUtils.write(fi, it.nextLine(), "utf-8", true);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start) * 1.0 / 1000);
        // 424.526/130.561
    }

    @Test
    public void testSpiltLongFileApacheBatch() {
        long start = System.currentTimeMillis();
        File f = new File("/temp/iotest/LOAN1.txt");
        File dir = new File("/temp/iotest/LOAN11");
        long i = 0;
        File fi = null;
        String name = FilenameUtils.getBaseName(f.getName());
        LineIterator it = null;
        StringBuilder s = new StringBuilder();
        try {
            it = IOUtils.lineIterator(new FileInputStream(f), "utf-8");
            while (it.hasNext()) {
                if (i % lines == 0) {
                    fi = File.createTempFile(name, ".txt", dir);
                }
                if (i % 100 == 0 && i != 0) {
                    FileUtils.write(fi, s.toString(), "utf-8", true);
                    s.setLength(0);
                }
                s.append(it.nextLine());
                i++;
            }
            if (s.length() > 0) {
                FileUtils.write(fi, s.toString(), "utf-8", true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start) * 1.0 / 1000);
    }

    @Test
    public void testSpiltLongFileByLine() {
        File f = new File("/temp/iotest/LOAN.txt");
        long i = 0;
        File fi = null;
        File p = f.getParentFile();
        String name = FilenameUtils.getBaseName(f.getName());
        String s;
        LineIterator it = null;
        try {
            it = IOUtils.lineIterator(new BufferedInputStream(new FileInputStream(f)), "utf-8");
            while (StringUtils.isNotBlank((s = it.nextLine()))) {
                if (i % lines == 0) {
                    fi = File.createTempFile(name, ".txt", p);
                }
                FileUtils.write(fi, s, "utf-8", true);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (it != null) {
                it.close();
            }
        }
    }

    @Test
    public void testSpiltLongFileByLine2() {
        File f = new File("/temp/iotest/LOAN.txt");
        long i = 0;
        File fi = null;
        File p = f.getParentFile();
        String name = FilenameUtils.getBaseName(f.getName());
        try (Scanner sc = new Scanner(new BufferedInputStream(new FileInputStream(f)), "utf-8")) {
            while (sc.hasNextLine()) {
                if (i % lines == 0) {
                    fi = File.createTempFile(name, ".txt", p);
                }
                FileUtils.write(fi, sc.nextLine(), "utf-8", true);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
