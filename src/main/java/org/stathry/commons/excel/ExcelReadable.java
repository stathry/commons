package org.stathry.commons.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.stathry.commons.enums.FileArea;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel数据读取
 *
 * @author stathry
 * @date 2018/4/19
 */
public interface ExcelReadable {

    List<Map<String, String>> readToMaps(String path, List<String> keys);

    List<Map<String, String>> readToMaps(String path, List<String> keys, FileArea area);

    List<Map<String, String>> readToMaps(String path, Integer sheetIndex, List<String> keys, FileArea area);

    List<Map<String, String>> readToMaps(String path, String sheetName, List<String> keys, FileArea area);

//    List<Map<String, String>> readToMaps(Sheet sheet, List<String> keys, FileArea area);

    List<List<String>> readToLists(String path);

    List<List<String>> readToLists(String path, FileArea area);

    List<List<String>> readToLists(String path, Integer sheetIndex, FileArea area);

    List<List<String>> readToLists(String path, String sheetName, FileArea area);

//    List<List<String>> readToLists(Sheet sheet, FileArea area);

   String readOneCell(String path, int rowIndex, int cellIndex);

   String readOneCell(Sheet sheet, int rowIndex, int cellIndex);

   default int maxRow() {
       return 1000000;
   }

   default int maxColumn() {
       return 1000;
   }

   default String readDatePattern() {
       return "yyyy-MM-dd HH:mm:ss";
   }

   default String cellValue(Cell cell) {
       return cellValue(cell, readDatePattern());
   }

    /**
     * 读取单元格value
     * @param cell
     * @param datePattern
     * @return
     */
    default String cellValue(Cell cell, String datePattern) {
    if(cell == null) {
        return "";
    }
    int type = cell.getCellType();
    String value = "";
    switch (type) {
        case Cell.CELL_TYPE_STRING:
            value = cell.getStringCellValue();
            break;
        case Cell.CELL_TYPE_NUMERIC:
            value = String.valueOf(cell.getNumericCellValue());
            break;
        case Cell.CELL_TYPE_FORMULA:
            value = String.valueOf(cell.getCellFormula());
            break;
        case Cell.CELL_TYPE_BLANK:
            break;
        case Cell.CELL_TYPE_BOOLEAN:
            value = String.valueOf(cell.getBooleanCellValue());
            break;
        case Cell.CELL_TYPE_ERROR:
            value = String.valueOf(cell.getErrorCellValue());
            break;
    }
    if (StringUtils.isNotBlank(value)) {
        return StringUtils.trimToEmpty(value);
    }
    if (DateUtil.isCellDateFormatted(cell)) {
        Date date = cell.getDateCellValue();
        return DateFormatUtils.format(date, datePattern);
    }
    return value;
}
}
