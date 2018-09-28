package org.stathry.commons.excel;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * ExcelUtils
 *
 * @author dongdaiming
 * @date 2018/4/19
 */
public class ExcelUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);

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


}
