/**
 * Copyright 2012-2016 free Co., Ltd.
 */
package org.stathry.commons.pojo.config;

import java.util.List;

import org.stathry.commons.enums.ExcelTypeEnums;

/**
 * @author dongdaiming@free.com
 *
 *         2016年8月22日
 */
public class ExcelParams {

	private ExcelTypeEnums type = ExcelTypeEnums.xls;
	private String sheetName = "sheet1";
	private String title;
	private short rowHeight = 20;
	private short columnWidth = 20;

	/**
	 * 键序列，逗号分隔
	 */
	private String keyIndex1;
	private String[] keyIndex2;
	private List<String> keyIndex3;

	private StyleParams titleStyle;
	private StyleParams headerStyle;
	private StyleParams defaultStyle;

	public String getKeyIndex1() {
		return keyIndex1;
	}

	public void setKeyIndex1(String keyIndex1) {
		this.keyIndex1 = keyIndex1;
	}

	public String[] getKeyIndex2() {
		return keyIndex2;
	}

	public void setKeyIndex2(String[] keyIndex2) {
		this.keyIndex2 = keyIndex2;
	}

	public List<String> getKeyIndex3() {
		return keyIndex3;
	}

	public void setKeyIndex3(List<String> keyIndex3) {
		this.keyIndex3 = keyIndex3;
	}

	public ExcelTypeEnums getType() {
		return type;
	}

	public void setType(ExcelTypeEnums type) {
		this.type = type;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public StyleParams getTitleStyle() {
		return titleStyle;
	}

	public void setTitleStyle(StyleParams titleStyle) {
		this.titleStyle = titleStyle;
	}

	public StyleParams getHeaderStyle() {
		return headerStyle;
	}

	public void setHeaderStyle(StyleParams headerStyle) {
		this.headerStyle = headerStyle;
	}

	public StyleParams getDefaultStyle() {
		return defaultStyle;
	}

	public void setDefaultStyle(StyleParams defaultStyle) {
		this.defaultStyle = defaultStyle;
	}

	public short getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(short rowHeight) {
		this.rowHeight = rowHeight;
	}

	public short getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(short columnWidth) {
		this.columnWidth = columnWidth;
	}

}
