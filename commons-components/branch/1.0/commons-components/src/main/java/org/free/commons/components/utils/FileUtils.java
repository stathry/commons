/**
 * Copyright 2012-2016 Deppon Co., Ltd.
 */
package org.free.commons.components.utils;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

/**
 * @author dongdaiming@deppon.com
 *
 * 2016年8月22日
 */
public class FileUtils {
	
	public static boolean exists(String path) {
		if(StringUtils.isBlank(path)) {
			return false;
		}
		return new File(path).exists();
	}
	
	public static boolean mkdirs(String path) {
		if(StringUtils.isBlank(path)) {
			return false;
		}
		File parent = new File(path).getParentFile();
		if(!parent.exists()) {
			return parent.mkdirs();
		}
		return parent.exists();
	}

}
