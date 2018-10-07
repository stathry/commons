package org.stathry.commons.excel;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stathry.commons.math.DecimalUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ExcelUtils
 *
 * @author dongdaiming
 * @date 2018/4/19
 */
public class ExcelUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);

    private static final DecimalUtils DECIMAL_UTILS = new DecimalUtils(22, 2, RoundingMode.HALF_UP);

    private ExcelUtils() {}

    public static String cellValue(Cell cell) {
        return cellValue(cell, ExcelConstant.READ_DATE_PATTERN);
    }

    /**
     * 读取单元格value
     * @param cell
     * @param datePattern
     * @return
     */
    public static String cellValue(Cell cell, String datePattern) {
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
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    value = DateFormatUtils.format(date, datePattern);
                } else {
                    DecimalFormat f = new DecimalFormat();
                    f.setGroupingUsed(false);
                    f.setRoundingMode(ExcelConstant.FLOAT_MODE);
                    f.setMaximumFractionDigits(ExcelConstant.FLOAT_SCALE);
                    f.setMinimumFractionDigits(0);
                    value = f.format(new BigDecimal(String.valueOf(cell.getNumericCellValue())));
                }
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

        return StringUtils.trimToEmpty(value);
    }

    /**
     * 根据数据类型创建单元格并赋值
     * @param row
     * @param i
     * @param value
     */
    public static Cell createCell(Row row, int i, Object value, CellStyle dateStyle, CellStyle floatStyle) {
        Cell cell;
        if(value == null) {
            return row.createCell(i, Cell.CELL_TYPE_BLANK);
        }

        Class c = value.getClass();
        if(c == String.class) {
            cell = row.createCell(i, Cell.CELL_TYPE_STRING);
            cell.setCellValue((String) value);
        } else if(value instanceof Number) {
            cell = row.createCell(i, Cell.CELL_TYPE_NUMERIC);
            if(c == Double.class || c == Float.class || c == BigDecimal.class) {
             cell.setCellStyle(floatStyle);
             cell.setCellValue(Double.parseDouble(DECIMAL_UTILS.format(value)));
            } else {
                cell.setCellValue(((Number) value).doubleValue());
            }
        } else if(c == Date.class) {
            cell = row.createCell(i, Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue(((Date) value));
            cell.setCellStyle(dateStyle);
        } else if(c == Calendar.class) {
            cell = row.createCell(i, Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue(((Calendar) value));
            cell.setCellStyle(dateStyle);
        } else if(c == Boolean.class) {
            cell = row.createCell(i, Cell.CELL_TYPE_BOOLEAN);
            cell.setCellValue(((Boolean) value));
        } else {
            cell = row.createCell(i, Cell.CELL_TYPE_STRING);
            cell.setCellValue((String) value);
        }
        return cell;
    }

    public static String fileNameToType(String filename) {
        if (StringUtils.isBlank(filename)) {
            return "";
        }
        String name =FilenameUtils.getExtension(filename);
        String type = "";
        if(name.endsWith("xls")) {
            type = "xls";
        } else if(name.endsWith("xlsx")) {
            type = "xlsx";
        }
        return type;
    }

    public static boolean isExcel(String filename) {
        if (StringUtils.isBlank(filename)) {
            return false;
        }
        String name =FilenameUtils.getExtension(filename);
        boolean is = false;
        if(name.endsWith("xls")) {
            is = true;
        } else if(name.endsWith("xlsx")) {
            is = true;
        }
        return is;
    }

    public static CellStyle createDateStyle(Workbook book) {
        CellStyle style = book.createCellStyle();
        DataFormat format = book.createDataFormat();
        style.setDataFormat(format.getFormat(ExcelConstant.WRITE_DATE_PATTERN));
        return style;
    }

    public static CellStyle createFloatStyle(Workbook book) {
        CellStyle style = book.createCellStyle();
        DataFormat format = book.createDataFormat();
        style.setDataFormat(format.getFormat(ExcelConstant.WRITE_NUM_FORMAT));
        return style;
    }

}
