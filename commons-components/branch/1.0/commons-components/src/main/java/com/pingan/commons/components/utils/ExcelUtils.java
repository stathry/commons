/**
 * Copyright 2012-2016 Deppon Co., Ltd.
 */
package com.pingan.commons.components.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pingan.commons.components.enums.ExcelTypeEnums;
import com.pingan.commons.components.pojo.config.ExcelParams;
import com.pingan.commons.components.pojo.config.StyleParams;

/**
 * @author dongdaiming@deppon.com
 *
 *         2016年8月22日
 */
public class ExcelUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);

	public static Workbook export(String path, Map<String, Object> header, List<Map<String, Object>> data, ExcelParams params) {

		Workbook workbook = null;
		Sheet sheet = null;
		
		if(data == null || data.isEmpty() || data.get(0) == null || data.get(0).isEmpty() || params == null) {
			LOGGER.error("the exprot excel param invalid.");
			return null;
		}
		
		workbook = ExcelTypeEnums.xls.equals(params.getType()) ? new HSSFWorkbook() : new XSSFWorkbook();
		sheet = workbook.createSheet(params.getSheetName());
		sheet.setDefaultColumnWidth(params.getColumnWidth());
		sheet.setDefaultRowHeightInPoints(params.getRowHeight());
		generate(workbook, sheet, header, data, params);
		
		if(StringUtils.isBlank(path)) {
			try {
				workbook.close();
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
				// ingore
			}
			return workbook;
		}
		
		FileOutputStream out = null;
		FileUtils.mkdirs(path);
		try {
			out = new FileOutputStream(path);
			workbook.write(out);
			return workbook;
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// ingore
				}
			}
			if(workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					// ingore
				}
			}
		}
		
	}
	
	public static Workbook export(String path, List<String> header, List<List<String>> data, ExcelParams params) {
		String[] header1 = new String[header.size()];
		String[][] data1 = new String[data.size()][];
		return export(path, header.toArray(header1), data.toArray(data1), params);
	}
	
	public static Workbook export2(String path, List<String> header, List<List<String>> data, ExcelParams params) {
		// TODO
		return null;
	}
	
	public static Workbook export(String path, String[] header, String[][] data, ExcelParams params) {
		Workbook workbook = null;
		Sheet sheet = null;
		
		if(data == null || data.length < 1 || data[0] == null || data[0].length < 1 || params == null) {
			LOGGER.error("the exprot excel param invalid.");
			return null;
		}
		
		workbook = ExcelTypeEnums.xls.equals(params.getType()) ? new HSSFWorkbook() : new XSSFWorkbook();
		sheet = workbook.createSheet(params.getSheetName());
		sheet.setDefaultColumnWidth(params.getColumnWidth());
		sheet.setDefaultRowHeightInPoints(params.getRowHeight());
		generate(workbook, sheet, header, data, params);
		
		if(StringUtils.isBlank(path)) {
			try {
				workbook.close();
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
				// ingore
			}
			return workbook;
		}
		
		FileOutputStream out = null;
		FileUtils.mkdirs(path);
		try {
			out = new FileOutputStream(path);
			workbook.write(out);
			return workbook;
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// ingore
				}
			}
			if(workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					// ingore
				}
			}
		}
		
		
	}
	
	private static void generate(Workbook workbook, Sheet sheet, String[] header, String[][] data, ExcelParams params) {
		
		StyleParams titleStyleParam = params.getTitleStyle() != null ? params.getTitleStyle() : ExcelHelper.getTitleStyle() ;
		CellStyle titleCellStyle = ExcelHelper.getCellStyle(workbook, titleStyleParam);
		StyleParams headerStyleParam = params.getHeaderStyle() != null ? params.getHeaderStyle() : ExcelHelper.getHeaderStyle(titleStyleParam) ;
		CellStyle headerCellStyle = ExcelHelper.getCellStyle(workbook, headerStyleParam);
		StyleParams defaultStyleParam = params.getDefaultStyle() != null ? params.getDefaultStyle() : ExcelHelper.getDefaultStyle(headerStyleParam) ;
		CellStyle defaultCellStyle = ExcelHelper.getCellStyle(workbook, defaultStyleParam);
		
		if(StringUtils.isBlank(params.getTitle()) && (header == null || header.length < 1)) {
			createRowsCells(sheet, data, defaultCellStyle, defaultStyleParam);
		}
		
		else if(StringUtils.isNotBlank(params.getTitle()) && (header == null || header.length < 1)) {
			CellRangeAddress range = new CellRangeAddress(titleStyleParam.getStartRow(), titleStyleParam.getStartRow(), titleStyleParam.getStartColumn(), titleStyleParam.getStartColumn() + header.length - 1);
			sheet.addMergedRegion(range);
			// 创建表头
			createRowCells(sheet, Arrays.copyOf(new String[]{params.getTitle()}, data[0].length), titleCellStyle, titleStyleParam);
			
			// 创建数据行列
			createRowsCells(sheet, data, titleCellStyle, titleStyleParam);
		}
		
		else if(StringUtils.isBlank(params.getTitle()) && (header != null && header.length > 0)) {
			// 创建标题列
			createRowCells(sheet, header, headerCellStyle, headerStyleParam);
			
			// 创建数据行列
			createRowsCells(sheet, data, defaultCellStyle, defaultStyleParam);
		}
		
		else {
			CellRangeAddress range = new CellRangeAddress(titleStyleParam.getStartRow(), titleStyleParam.getStartRow(), titleStyleParam.getStartColumn(), titleStyleParam.getStartColumn() + header.length - 1);
			sheet.addMergedRegion(range);
			// 创建表头
			createRowCells(sheet, Arrays.copyOf(new String[]{params.getTitle()}, data[0].length), titleCellStyle, titleStyleParam);
			
			// 创建标题列
			createRowCells(sheet, header, headerCellStyle, headerStyleParam);
			
			// 创建数据行列
			createRowsCells(sheet, data, defaultCellStyle, defaultStyleParam);
		}
		
	}
	
	private static void generate(Workbook workbook, Sheet sheet, Map<String, Object> header, List<Map<String, Object>> data, ExcelParams params) {
		
		StyleParams titleStyleParam = params.getTitleStyle() != null ? params.getTitleStyle() : ExcelHelper.getTitleStyle() ;
		CellStyle titleCellStyle = ExcelHelper.getCellStyle(workbook, titleStyleParam);
		StyleParams headerStyleParam = params.getHeaderStyle() != null ? params.getHeaderStyle() : ExcelHelper.getHeaderStyle(titleStyleParam) ;
		CellStyle headerCellStyle = ExcelHelper.getCellStyle(workbook, headerStyleParam);
		StyleParams defaultStyleParam = params.getDefaultStyle() != null ? params.getDefaultStyle() : ExcelHelper.getDefaultStyle(headerStyleParam) ;
		CellStyle defaultCellStyle = ExcelHelper.getCellStyle(workbook, defaultStyleParam);
		
		String[] keyIndex = params.getKeyIndex2();
		if(keyIndex == null) {
			keyIndex = new String[params.getKeyIndex3().size()];
			params.getKeyIndex3().toArray(keyIndex);
		}
		if(StringUtils.isBlank(params.getTitle()) && (header == null || header.isEmpty())) {
			createRowsCells(sheet, data, defaultCellStyle, defaultStyleParam, keyIndex);
		}
		
		else if(StringUtils.isNotBlank(params.getTitle()) && (header == null || header.isEmpty())) {
			CellRangeAddress range = new CellRangeAddress(titleStyleParam.getStartRow(), titleStyleParam.getStartRow(), titleStyleParam.getStartColumn(), titleStyleParam.getStartColumn() + header.size() - 1);
			sheet.addMergedRegion(range);
			// 创建表头
			createRowCells(sheet, Arrays.copyOf(new String[]{params.getTitle()}, data.get(0).size()), titleCellStyle, titleStyleParam);
			
			// 创建数据行列
			createRowsCells(sheet, data, titleCellStyle, titleStyleParam, keyIndex);
		}
		
		else if(StringUtils.isBlank(params.getTitle()) && (header != null && header.isEmpty())) {
			// 创建标题列
			createRowCells(sheet, header, headerCellStyle, headerStyleParam, keyIndex);
			
			// 创建数据行列
			createRowsCells(sheet, data, defaultCellStyle, defaultStyleParam, keyIndex);
		}
		
		else {
			CellRangeAddress range = new CellRangeAddress(titleStyleParam.getStartRow(), titleStyleParam.getStartRow(), titleStyleParam.getStartColumn(), titleStyleParam.getStartColumn() + header.size() - 1);
			sheet.addMergedRegion(range);
			// 创建表头
			createRowCells(sheet, Arrays.copyOf(new String[]{params.getTitle()}, data.get(0).size()), titleCellStyle, titleStyleParam);
			
			// 创建标题列
			createRowCells(sheet, header, headerCellStyle, headerStyleParam, keyIndex);
			
			// 创建数据行列
			createRowsCells(sheet, data, defaultCellStyle, defaultStyleParam, keyIndex);
		}
		
	}
	
	private static void createRowsCells(Sheet sheet,String[][] data, CellStyle cellStyle, StyleParams styleParam) {
		int rowDataIndex = 0;
		for (int i = 0; i < data.length; i++) {
			Row row = sheet.createRow(i + styleParam.getStartRow());
			String[] rowData = data[rowDataIndex++];
			
			int columnDataIndex = 0;
			for (int j = 0; j < rowData.length ; j++) {
				Cell cell = row.createCell(j + styleParam.getStartColumn(), Cell.CELL_TYPE_STRING);
				cell.setCellValue(rowData[columnDataIndex++]);
				cell.setCellStyle(cellStyle);
			}
			
		}
	}
	
	private static void createRowCells(Sheet sheet,String[] cellData, CellStyle cellStyle, StyleParams styleParam) {
		Row row = sheet.createRow(styleParam.getStartRow());
		for (int i = 0; i < cellData.length; i++) {
			Cell headerCell = row.createCell(i + styleParam.getStartColumn(), Cell.CELL_TYPE_STRING);
			headerCell.setCellStyle(cellStyle);
			headerCell.setCellValue(cellData[i]);
		}
	}
	
	private static void createRowsCells(Sheet sheet,List<Map<String,Object>> data, CellStyle cellStyle, StyleParams styleParam, String[] keyIndex) {
		int rowIndex = 0;
		for (Map<String, Object> rowData : data) {
			Row row = sheet.createRow(rowIndex + styleParam.getStartRow());
			
			for (int cellIndex = 0; cellIndex < rowData.size() ; cellIndex++) {
				Cell cell = row.createCell(cellIndex + styleParam.getStartColumn(), Cell.CELL_TYPE_STRING);
				cell.setCellValue(TextUtils.toString(rowData.get(keyIndex[cellIndex])));
				cell.setCellStyle(cellStyle);
			}
			
			rowIndex++;
		}
	}
	
	private static void createRowCells(Sheet sheet,Map<String, Object> cellData, CellStyle cellStyle, StyleParams styleParam, String[] keyIndex) {
		Row row = sheet.createRow(styleParam.getStartRow());
		int cellIndex = 0;
		for (String key : keyIndex) {
			Cell headerCell = row.createCell(cellIndex++ + styleParam.getStartColumn(), Cell.CELL_TYPE_STRING);
			headerCell.setCellStyle(cellStyle);
			headerCell.setCellValue(TextUtils.toString(cellData.get(key)));
		}
	}
	
}
