/**
 * Copyright 2012-2016 free Co., Ltd.
 */
package org.stathry.commons.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DataFormatter {

    private static final int DEFAULT_PRECISION = 24;
    private static final int DEFAULT_FLOAT_SCALE = 2;
    private static final RoundingMode DEFAULT_FLOAT_MODE = RoundingMode.HALF_UP;
    private static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final int TYPE_STR = 1;
    public static final int TYPE_INT = 2;
    public static final int TYPE_FLOAT = 3;
    public static final int TYPE_DATE = 4;

    private final DecimalFormat decimalFormatter;
    private final SimpleDateFormat dateFormat;

    public DataFormatter() {
        this(DEFAULT_FLOAT_SCALE, DEFAULT_FLOAT_MODE, DEFAULT_DATETIME_PATTERN);
    }

    public DataFormatter(int scale, RoundingMode mode, String datePattern) {
        dateFormat = new SimpleDateFormat(datePattern);
        decimalFormatter = newDecimalFormat(scale, mode);
    }

    private DecimalFormat newDecimalFormat(int scale, RoundingMode mode) {
        DecimalFormat decimalFormatter = new DecimalFormat();
        decimalFormatter.setRoundingMode(mode);
        decimalFormatter.setGroupingUsed(false);
        decimalFormatter.setMaximumIntegerDigits(DEFAULT_PRECISION - scale);
        decimalFormatter.setMaximumFractionDigits(scale);
        decimalFormatter.setMinimumFractionDigits(scale);
        return decimalFormatter;
    }

    public String format(Object data) {
        return format(data, null);
    }

    public String format(Object data, DataFormat format) {
        if (data == null) {
            return "";
        }

        String value;
        if (data instanceof CharSequence) {
            value = data.toString();
        } else if (data instanceof Double || data instanceof Float || data instanceof BigDecimal) {
            BigDecimal dec = BigDecimal.valueOf(((Number)data).doubleValue());
            if (format == null || format.scale == -1) {
                value = decimalFormatter.format(dec);
            } else {
                dec = BigDecimal.valueOf(format.getMultiply()).multiply(dec);
                value = newDecimalFormat(format.getScale(), format.getMode()).format(dec);
            }
        } else if (data instanceof Integer || data instanceof Long) {
            value = format == null ? data.toString() : String.valueOf((long)(format.getMultiply() * ((Number)data).longValue()));
        } else if (data instanceof Date || data instanceof Calendar) {
            Date date = data instanceof Date ? (Date)data : ((Calendar)data).getTime();
            if (format == null) {
                value = dateFormat.format(date);
            } else {
                value = new SimpleDateFormat(format.getDestDatePattern()).format(date);
            }
        } else if (data instanceof char[]) {
            value = new String((char[]) data);
        } else {
            value = data.toString();
        }

        return value;
    }

    public String format(Object data, DataFormat format, int type) {
        if (type == 0) {
            return format(data, format);
        }

        String value;
        if (TYPE_STR == type) {
            value = data.toString();
        } else if (TYPE_FLOAT == type) {
            double d = data instanceof Number ? ((Number)data).doubleValue() : Double.parseDouble(data.toString());
            BigDecimal dec = BigDecimal.valueOf(d);
            if (format == null || format.scale == -1) {
                value = decimalFormatter.format(dec);
            } else {
                dec = BigDecimal.valueOf(format.getMultiply()).multiply(dec);
                value = newDecimalFormat(format.getScale(), format.getMode()).format(dec);
            }
        } else if (TYPE_INT == type || data instanceof Integer || data instanceof Long) {
            long l = data instanceof Number ? ((Number)data).longValue() : Long.valueOf(data.toString());
            value = format == null ? data.toString() : String.valueOf((long)(format.getMultiply() * l));
        } else if (TYPE_DATE == type || data instanceof Date || data instanceof Calendar) {
            Date date;
            if(data instanceof CharSequence && format != null) {
                try {
                    date = new SimpleDateFormat(format.getSrcDatePattern()).parse(data.toString());
                } catch (ParseException e) {
                    throw new IllegalArgumentException("srcDatePattern " + format.getSrcDatePattern() + ", " + data.toString());
                }
            } else {
                date = data instanceof Date ? (Date)data : ((Calendar)data).getTime();
            }
            if (format == null) {
                value = dateFormat.format(date);
            } else {
                value = new SimpleDateFormat(format.getDestDatePattern()).format(date);
            }
        } else if (data instanceof char[]) {
            value = new String((char[]) data);
        } else {
            value = data.toString();
        }

        return value;
    }

    public static class DataFormat {
        private int scale = -1;
        private RoundingMode mode = DEFAULT_FLOAT_MODE;
        private double multiply = 1.0;
        private String srcDatePattern = DEFAULT_DATETIME_PATTERN;
        private String destDatePattern = DEFAULT_DATETIME_PATTERN;

        public DataFormat(int scale, RoundingMode mode) {
            this.scale = scale;
            this.mode = mode;
        }

        public DataFormat(String destDatePattern) {
            this.destDatePattern = destDatePattern;
        }

        public DataFormat(double multiply) {
            this.multiply = multiply;
        }

        public DataFormat(String srcDatePattern, String destDatePattern) {
            this.srcDatePattern = srcDatePattern;
            this.destDatePattern = destDatePattern;
        }

        public int getScale() {
            return scale;
        }

        public void setScale(int scale) {
            this.scale = scale;
        }

        public RoundingMode getMode() {
            return mode;
        }

        public void setMode(RoundingMode mode) {
            this.mode = mode;
        }

        public double getMultiply() {
            return multiply;
        }

        public void setMultiply(double multiply) {
            this.multiply = multiply;
        }

        public String getSrcDatePattern() {
            return srcDatePattern;
        }

        public void setSrcDatePattern(String srcDatePattern) {
            this.srcDatePattern = srcDatePattern;
        }

        public String getDestDatePattern() {
            return destDatePattern;
        }

        public void setDestDatePattern(String destDatePattern) {
            this.destDatePattern = destDatePattern;
        }
    }

}
