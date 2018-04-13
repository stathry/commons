package org.stathry.commons.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stathry.commons.enums.ExcelTypeEnums;
import org.stathry.commons.pojo.config.ExcelRange;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * TODO
 */
public class ExcelDataUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelDataUtils.class);

    private ExcelDataUtils() {
    }

    /**
     * 读取excel数据到map(以行首为key)
     *
     * @param path
     * @return
     */
    public static List<Map<String, String>> readToMap(String path) {

        return readToMap(path, null, null);
    }

    /**
     * 读取excel数据到map(指定key)
     *
     * @param path
     * @param keys
     * @param range
     * @return
     */
    public static List<Map<String, String>> readToMap(String path, List<String> keys, ExcelRange range) {
        if (!isValidFile(path)) {
            LOGGER.warn("invalid excel path {}.", path);
            return Collections.emptyList();
        }
        if (!isValidExcel(path)) {
            LOGGER.warn("invalid excel file, path {}.", path);
            return Collections.emptyList();
        }

        InputStream in = null;
        Workbook book = null;
        try {
            in = new FileInputStream(path);
            book = WorkbookFactory.create(in);
            Iterator<Sheet> it = book.sheetIterator();
            Sheet sheet = null;
            while (it.hasNext()) {
                sheet = it.next();
                if (sheet != null && sheet.getLastRowNum() >= 1) {
                    break;
                }
            }

            List<Map<String, String>> data = new ArrayList<>();

            range = range == null ? new ExcelRange() : range;
            initRange(sheet, range);
            validRange(range, keys);

            keys = (keys == null || keys.isEmpty()) ? new ArrayList<>() : keys;
            if(keys.isEmpty()) {
                loadKeys(keys, sheet, range);
            }

            LOGGER.info("load data from excel by range {}.", range.toString());
            Row row;
            Cell cell;

            int keySize = keys.size();

            Map<String, String> map;

            for (int iRow = range.getRowStart(), rowEnd = range.getRowEnd(); iRow < rowEnd; iRow++) {
                row = sheet.getRow(iRow);
                if (row == null) {
                    continue;
                }
                map = new HashMap<>(keySize * 2);
                for (int iCell = range.getCellStart(), cellEnd = range.getCellEnd(); iCell < cellEnd; iCell++) {
                    cell = row.getCell(iCell);
                    if (cell == null) {
                        continue;
                    }
                    map.put(keys.get(iCell), StringUtils.trimToEmpty(cell.getStringCellValue()));
                }
                data.add(map);
            }
            return data;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(book);
        }
        return null;
    }

    private static void validRange(ExcelRange range, List<String> keys) {
        boolean valid = true;
        if(keys != null && keys.size() != (range.getCellEnd() - range.getCellStart())) {
            valid = false;
        } else if(range.getCellEnd() < range.getCellStart() || range.getCellStart() < 0) {
            valid = false;
        } else if(range.getRowEnd() < range.getRowStart() || range.getRowStart() < 0) {
            valid = false;
        }
        if(!valid) {
            throw new IllegalArgumentException("invalid range:" + range.toString());
        }
    }

    private static void initRange(Sheet sheet, ExcelRange range) {
        range.setRowStart(range.getRowStart() == null ? sheet.getFirstRowNum() : range.getRowStart());
        range.setRowEnd(range.getRowEnd() == null ? (sheet.getLastRowNum() + 1) : range.getRowEnd());
        Row fRow = sheet.getRow(range.getRowStart());
        range.setCellStart(range.getCellStart() == null ? fRow.getFirstCellNum() : range.getCellStart());
        range.setCellEnd(range.getCellEnd() == null ? (fRow.getLastCellNum() + 1) : range.getCellEnd());
    }

    private static boolean isValidExcel(String path) {
        String type = FilenameUtils.getExtension(path);
        if (ExcelTypeEnums.xls.name().equalsIgnoreCase(type)
                || ExcelTypeEnums.xlsx.name().equalsIgnoreCase(type)) {
            return true;
        }
        return false;
    }

    private static void loadKeys(List<String> keys, Sheet sheet, ExcelRange range) {
        Cell cell;
        Row fRow = sheet.getRow(range.getRowStart());
        for (int iCell = range.getCellStart(); iCell < range.getCellEnd(); iCell++) {
            cell = fRow.getCell(iCell);
            if (cell == null) {
                continue;
            }
            keys.add(StringUtils.trimToEmpty(cell.getStringCellValue()));
        }
    }

    public static boolean isValidFile(String path) {
        if (StringUtils.isBlank(path)) {
            return false;
        }
        File f = new File(path);
        return f.exists() && f.isFile() && f.length() > 0;
    }

}
