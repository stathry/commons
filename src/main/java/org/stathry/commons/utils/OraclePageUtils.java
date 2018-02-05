/*
 * Copyright © PING AN INSURANCE (GROUP) COMPANY OF CHINA ，LTD. All Rights Reserved
 */
package org.stathry.commons.utils;

import org.stathry.commons.pojo.Page;

/**
 * @author Demon
 * @date 2016年12月16日
 */
public final class OraclePageUtils {
	
	
	private static <T extends Page> void init(T t) {
		t.setPageNo(t.getPageNo() == null ? 1 : t.getPageNo());
		t.setPageSize(t.getPageSize() == null ? Page.DEFAULT_PAGE_SIZE : t.getPageSize());
	}
	
	private static <T extends Page> void limit(T t) {
		t.setPageNo(t.getPageNo() <= 0 ? 1 : t.getPageNo());
		t.setPageSize(t.getPageSize() <= 0 ? Page.DEFAULT_PAGE_SIZE : t.getPageSize());
		t.setPageSize(t.getPageSize() > Page.MAX_PAGE_SIZE ? Page.MAX_PAGE_SIZE : t.getPageSize());
	}
	
	private static <T extends Page> void range(T t) {
		t.setStart(t.getPageSize().longValue() * (t.getPageNo() - 1));
		t.setEnd(t.getPageNo().longValue() * t.getPageSize());
	}
	
	public static <T extends Page> void checkPage(T t) {
		if(t == null) {
			return;
		}
		
		init(t);
		limit(t);
		range(t);
	}

}
