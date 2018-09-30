package org.stathry.commons.excel;

import java.math.RoundingMode;

/**
 * ExcelConstant
 * Created by dongdaiming on 2018-09-27 14:46
 */
public class ExcelConstant {

    public static int MAX_ROW = 10_0000;
    public static int MAX_COLUMN = 2000;
    public static String READ_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static String WRITE_DATE_PATTERN = "yyyy-mm-dd hh:mm:ss";
    public static String WRITE_NUM_FORMAT = "0.00";

    public static int MAX_CONTINUOUS_EMPTY_ROWS = 3;
    public static int MAX_CONTINUOUS_EMPTY_COLS = 3;
    public static int FLOAT_SCALE = 2;
    public static RoundingMode FLOAT_MODE = RoundingMode.HALF_UP;

    private ExcelConstant() {}
}
