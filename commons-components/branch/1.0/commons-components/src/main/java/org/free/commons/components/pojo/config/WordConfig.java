/**
 * Copyright 2016-2100 free Co., Ltd.
 */
package org.free.commons.components.pojo.config;

import org.free.commons.components.constants.DefaultWordConfig;

/**
 * @author dongdaiming@free.com
 *
 *         2016年8月18日
 */

public class WordConfig {

	private String fileType = DefaultWordConfig.FILE_TYPE_DOCX;
	private String filePath = DefaultWordConfig.FILE_PATH;
	private String creator = DefaultWordConfig.CREATOR;
	private String company = DefaultWordConfig.COMPANY;

	private int headerFontSize = DefaultWordConfig.HEADER_FONT_SIZE;
	private String headerFont = DefaultWordConfig.HEADER_FONT;
	private String headerColor = "black";
	
	private int fontSize = DefaultWordConfig.FONT_SIZE;
	private String font = DefaultWordConfig.FONT;
	private String color = "black";

	private String varPattern = DefaultWordConfig.VAR_PATTERN;
	private String varPrefix = DefaultWordConfig.VAR_PREFIX;
	private String varSuffix = DefaultWordConfig.VAR_SUFFIX;

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public int getHeaderFontSize() {
		return headerFontSize;
	}

	public void setHeaderFontSize(int headerFontSize) {
		this.headerFontSize = headerFontSize;
	}

	public String getHeaderFont() {
		return headerFont;
	}

	public void setHeaderFont(String headerFont) {
		this.headerFont = headerFont;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public String getHeaderColor() {
		return headerColor;
	}

	public void setHeaderColor(String headerColor) {
		this.headerColor = headerColor;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String getVarPattern() {
		return varPattern;
	}

	public void setVarPattern(String varPattern) {
		this.varPattern = varPattern;
	}

	public String getVarPrefix() {
		return varPrefix;
	}

	public void setVarPrefix(String varPrefix) {
		this.varPrefix = varPrefix;
	}

	public String getVarSuffix() {
		return varSuffix;
	}

	public void setVarSuffix(String varSuffix) {
		this.varSuffix = varSuffix;
	}

}
