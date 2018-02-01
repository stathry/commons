/**
 * Copyright 2012-2016 free Co., Ltd.
 */
package org.bryadong.commons.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

/**
 * @author dongdaiming@free.com
 *
 *         2016年8月22日
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

	public static boolean mkdirs(String path) {
		if (StringUtils.isBlank(path)) {
			return false;
		}
		File parent = new File(path).getParentFile();
		if (!parent.exists()) {
			return parent.mkdirs();
		}
		return parent.exists();
	}

	public static boolean createFile(String path) throws IOException {
		if (StringUtils.isBlank(path)) {
			return false;
		}
		File file = new File(path);
		return createFile(file);
	}

	public static boolean createFile(File file) throws IOException {
		if (file == null) {
			return false;
		}
		if (file.exists()) {
			return true;
		}
		File parent = file.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		return file.createNewFile();
	}

	public static List<File> splitFileByLines(String path, int lines) throws IOException {
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
			if (rcount >= lines) {
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
	
	public static void main(String[] args) {
//		splitFileByLines("", 200);
	}

}
