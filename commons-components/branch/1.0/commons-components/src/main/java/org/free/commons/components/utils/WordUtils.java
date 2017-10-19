/**
 * Copyright 2016-2100 free Co., Ltd.
 */
package org.free.commons.components.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.free.commons.components.pojo.config.DocInfo;
import org.jeecgframework.poi.exception.excel.ExcelExportException;
import org.jeecgframework.poi.word.WordExportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MS word工具类
 * @author dongdaiming@free.com
 *
 *         2016年8月17日
 */
public class WordUtils {

	private static Logger LOGGER = LoggerFactory.getLogger(WordUtils.class);
	
	public static final String TYPE_DOC = "doc";
	public static final String TYPE_DOCX = "docx";

	/**
	 * 生成word文档对象
	 * @param templatePath 模板路径
	 * @param data 数据,key的格式为"{{key1}}"
	 * @return document
	 * @throws Exception
	 */
	public static XWPFDocument generateDocx(String templatePath, Map<String, Object> data) throws Exception {
		XWPFDocument doc = null;
		if(StringUtils.isBlank(templatePath) || data == null ) {
			LOGGER.warn("invalid param. templatePath[{}] and data can not be null", templatePath);
			throw new ExcelExportException("invalid param. templatePath and data can not be null");
		}
		
		try {
			doc = WordExportUtil.exportWord07(templatePath, data);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
		
		return doc;
	}
	
	/**
	 * 导出word文档
	 * @param templatePath 模板路径
	 * @param destPath 导出路径
	 * @param data 数据
	 * @throws Exception
	 */
	public static void exportDocx(String templatePath, String destPath, Map<String, Object> data) throws Exception {
		XWPFDocument doc = null;
		FileOutputStream out = null;
		
		if(StringUtils.isBlank(templatePath) || StringUtils.isBlank(destPath) || data == null ) {
			LOGGER.warn("invalid param. templatePath, destPath and data can not be null");
			throw new ExcelExportException("invalid param. templatePath, destPath and data can not be null");
		}
		
		try {
			doc = WordExportUtil.exportWord07(templatePath, data);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
		
		if(doc == null) {
			LOGGER.error("export docx file error.");
			throw new ExcelExportException("export docx file error.");
		}
		
		try {
			out = new FileOutputStream(destPath);
			out.flush();
			doc.write(out);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		} finally {
			if(out != null) {
				out.close();
			}
		}
	}
	
	/**
	 * 获取docx文件实体类
	 * 
	 * @param path
	 *            docx文件路径
	 * @return text docx内容
	 */
	public static DocInfo readWord(String path) {
		if (StringUtils.isBlank(path)) {
			LOGGER.warn("the read file path[{}] invalid", path);
			return null;
		}

		return readWord(new File(path));
	}

	/**
	 * 获取docx文件实体类
	 * 
	 * @param path
	 *            docx文件
	 * @return text docx内容
	 */
	public static DocInfo readWord(File file) {

		if (file == null || !file.exists() || file.isDirectory()
				|| !file.canRead()) {
			LOGGER.warn(
					"the read file[{}] is null or not exists or is a directory or can not read",
					file);
			return null;
		}

		InputStream in = null;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			LOGGER.warn("file[{}] with the specified pathname does not exist",
					file);
			return null;
		}

		if (file.getPath().toLowerCase().endsWith(TYPE_DOC)) {

		} else if (file.getPath().toLowerCase().endsWith(TYPE_DOCX)) {

		} else {
		}
		switch (FilenameUtils.getExtension(file.getPath())) {
		case TYPE_DOC:
			return readDoc(in, file);
		case TYPE_DOCX:
			return readDocx(in, file);

		default:
			LOGGER.warn("not support file type[{}]",
					FilenameUtils.getExtension(file.getPath()));
			try {
				in.close();
			} catch (IOException e) {
				// ingore
			}
			return null;
		}

	}

	/**
	 * 获取docx文件实体类
	 * 
	 * @param path
	 *            docx输入流
	 * @return text docx内容
	 */
	public static DocInfo readDocx(InputStream in, File file) {
		XWPFWordExtractor extractor = null;
		XWPFDocument doc = null;
		String text = null;
		DocInfo entity = null;

		if (in == null) {
			LOGGER.warn("the read inPutStrea[{}] invalid", in);
			return null;
		}

		try {
			doc = new XWPFDocument(in);
			extractor = new XWPFWordExtractor(doc);
			entity = new DocInfo();
			String title = getTitle(doc);
			text = extractor.getText();
			entity.setTitle(title);
			entity.setContent(text.replaceFirst(title, ""));
			CoreProperties props = extractor.getCoreProperties();
			entity.setCreator(props.getCreator());
			try {
				entity.setLastModifiedBy(extractor.getPackage()
						.getPackageProperties().getLastModifiedByProperty()
						.getValue());
			} catch (InvalidFormatException e) {
				// ingore
			}
			entity.setCreatedDate(props.getCreated());
			entity.setLastModifiedDate(props.getModified());
			// entity.setLastModifiedDate(new Date(file.lastModified()));
			entity.setCharacters(extractor.getExtendedProperties()
					.getCharacters());
			entity.setCompany(extractor.getExtendedProperties().getCompany());

		} catch (FileNotFoundException e) {
			LOGGER.warn("the file does not exist", e);
			return null;
		} catch (IOException e) {
			LOGGER.warn(e.getMessage(), e);
			return null;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (extractor != null) {
					extractor.close();
				}
			} catch (IOException e) {
				// ingore
			}
		}
		return entity;
	}

	/**
	 * 获取docx文件实体类
	 * 
	 * @param path
	 *            docx输入流
	 * @return text docx内容
	 */
	public static DocInfo readDoc(InputStream in, File file) {
		WordExtractor extractor = null;
		HWPFDocument doc = null;
		String text = null;
		DocInfo entity = null;

		if (in == null) {
			LOGGER.warn("the read inPutStrea[{}] invalid", in);
			return null;
		}

		try {
			doc = new HWPFDocument(in);
			extractor = new WordExtractor(doc);
			entity = new DocInfo();

			text = extractor.getText();
			String title = getTitle(extractor.getParagraphText());
			entity.setTitle(title);
			entity.setContent(text.replaceFirst(title, ""));

			entity.setSize(file.length());
			entity.setCompany(extractor.getDocSummaryInformation().getCompany());
			entity.setLastModifiedDate(new Date(file.lastModified()));
			entity.setCreator(doc.getSummaryInformation().getAuthor());
			entity.setCreatedDate(doc.getSummaryInformation()
					.getCreateDateTime());
			entity.setLastModifiedBy(doc.getSummaryInformation()
					.getLastAuthor());
			entity.setCharacters(doc.getSummaryInformation().getCharCount());

		} catch (FileNotFoundException e) {
			LOGGER.warn("the file does not exist", e);
			return null;
		} catch (IOException e) {
			LOGGER.warn(e.getMessage(), e);
			return null;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (extractor != null) {
					extractor.close();
				}
			} catch (IOException e) {
				// ingore
			}
		}
		return entity;
	}

	private static String getTitle(XWPFDocument doc) {
		if (doc == null) {
			return null;
		}
		for (XWPFParagraph p : doc.getParagraphs()) {
			// 从上往下查找，第一个内容不为空且对齐方式为居中或style不为空的段落视为标题（因为正文的style为空）
			if (StringUtils.isNotBlank(p.getText())
					&& (StringUtils.isNotBlank(p.getStyle()) || p
							.getAlignment().getValue() == ParagraphAlignment.CENTER
							.getValue())) {
				return p.getText().trim();
			}
		}
		return null;
	}

	private static String getTitle(String[] ps) {
		if (ps == null) {
			return null;
		}
		for (String p : ps) {
			// 从上往下查找，第一个内容不为空的段落视为标题
			if (StringUtils.isNotBlank(p)) {
				return p.trim();
			}
		}
		return null;
	}


}