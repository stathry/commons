/**
 * Copyright 2012-2016 free Co., Ltd.
 */
package org.stathry.commons.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.util.Assert;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author dongdaiming@free.com
 * <p>
 * 2016年8月22日
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

    private static final String LINE_SEP = SystemUtils.LINE_SEPARATOR;
    private static final String CHARSET = "UTF-8";

    public static boolean exists(String path) {
        if (StringUtils.isBlank(path)) {
            return false;
        }
        return new File(path).exists();
    }

    public static boolean createFile(String path, boolean forceNew) throws IOException {
        if (StringUtils.isBlank(path)) {
            return false;
        }
        return createFile(new File(path), forceNew);
    }

    public static boolean createFile(File file, boolean forceNew) throws IOException {
        if (file == null) {
            return false;
        }
        boolean fex = file.exists();
        boolean r;
        if (forceNew && fex) {
            file.delete();
            r = file.createNewFile();
        } else if (forceNew && !fex) {
            file.getParentFile().mkdirs();
            r = file.createNewFile();
        } else if (!forceNew && fex) {
            r = true;
        } else {
            file.getParentFile().mkdirs();
            r = file.createNewFile();
        }
        return r;
    }

    public static List<File> splitFileByLines(String path, int lineLimit) throws IOException {
        File file = new File(path);
        List<File> fs = new ArrayList<>();
        LineIterator lit = IOUtils.lineIterator(new BufferedInputStream(new FileInputStream(file)), CHARSET);
        StringBuilder buf = new StringBuilder();
        String pre = FilenameUtils.getPath(file.getName());
        String suf = "." + FilenameUtils.getExtension(file.getName());
        String bname = FilenameUtils.getBaseName(file.getName());
        int rcount = 0;
        int fcount = 0;
        File f = new File(pre + bname + fcount + suf);
        f.createNewFile();
        fs.add(f);
        for (; lit.hasNext(); fcount++) {
            rcount++;
            if (rcount >= lineLimit) {
                write(f, buf, CHARSET, true);
                buf.setLength(0);
                f = new File(pre + bname + fcount + suf);
                f.createNewFile();
                rcount = 0;
            }
            buf.append(lit.next());
            buf.append(LINE_SEP);
        }
        return fs;
    }

    public static List<File> splitFileBySize(String path) throws IOException {
        return null;
    }

    public static Object[][] readTextData(String resourceName, String caseName) throws IOException {
        if(StringUtils.isBlank(caseName)) {
            return readTextData(resourceName);
        }

        InputStream in = Thread.currentThread().getClass().getResourceAsStream(resourceName);
        Assert.notNull(in, "not found " + resourceName);
        List<String> lines = IOUtils.readLines(in, "utf-8");
        IOUtils.closeQuietly(in);
        if (lines != null && !lines.isEmpty()) {
            Object[][] arr = new Object[lines.size()][];
            int cols = 0, rowIndex = 0;
            String[] row, rowData;
            Object obj;
            for (int i = 0, size = lines.size(); i < size; i++) {
                if(StringUtils.isBlank(lines.get(i))) {
                    continue;
                }
                row = lines.get(i).split(",");
                if(row[0].equals(caseName)) {
                    cols = cols == 0 ? (row.length - 1) : cols;
                    rowData = Arrays.copyOfRange(row, 1, row.length);
                    for (int j = 0; j < cols; j++) {
                        obj = row[j + 1];
                        if (obj == null) {
                            continue;
                        } else if ("null".equals(obj)) {
                            rowData[j] = null;
                        } else if ("''".equals(obj)) {
                            rowData[j] = "";
                        }
                    }
                    arr[rowIndex++] = rowData;
                }
            }
            arr = Arrays.copyOf(arr, rowIndex);
            return arr;
        }
        return null;
    }

    public static Object[][] readTextData(String resourceName) throws IOException {
        InputStream in = Thread.currentThread().getClass().getResourceAsStream(resourceName);
        Assert.notNull(in, "not found " + resourceName);
        List<String> lines = IOUtils.readLines(in, "utf-8");
        IOUtils.closeQuietly(in);
        if (lines != null && !lines.isEmpty()) {
            int cols = lines.get(lines.size() - 1).split(",").length;
            Object[][] data = new Object[lines.size()][cols];
            String[] row;
            Object obj;
            for (int i = 0, size = lines.size(); i < size; i++) {
                if(StringUtils.isBlank(lines.get(i))) {
                    continue;
                }
                row = lines.get(i).split(",");
                data[i] = row.length == 0 ? new String[cols] : row;
                for (int j = 0; j < cols; j++) {
                    obj = data[i][j];
                    if (obj == null) {
                        continue;
                    } else if ("null".equals(obj)) {
                        data[i][j] = null;
                    } else if ("''".equals(obj)) {
                        data[i][j] = "";
                    }
                }
            }
            return data;
        }
        return null;
    }

    public static void main(String[] args) {
//		splitFileByLines("", 200);
    }

}
