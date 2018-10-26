/*
 * Copyright © stathry@126.com All Rights Reserved
 */
package org.stathry.commons.model;

import java.io.Serializable;

/**
 * @author Demon
 * @date 2016年12月14日
 */
public interface Page extends Serializable {

	public static final int DEFAULT_PAGE_SIZE = 20;
	public static final int MAX_PAGE_SIZE = 2000;
	public static final Integer[] DEFAULT_PAGE_LIST = new Integer[] { 10, 20, 50 };

	public Integer getPageNo();

	public void setPageNo(Integer pageNo);

	public Integer getPageSize();

	public void setPageSize(Integer pageSize);

	public Long getStart();

	public void setStart(Long start);

	public Long getOffset();

	public void setOffset(Long offset);

	public Long getEnd();

	public void setEnd(Long end);
}
