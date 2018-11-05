package org.stathry.commons.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 高精计算
 * 
 * @author dongdaiming
 * @date 2018年1月17日
 */
public class DecimalUtils {

	private static final int PRECISION = 22;
	private static final int SCALE = 2;
	private static final RoundingMode MODE = RoundingMode.HALF_UP;

	private final MathContext mc;
	private final DecimalFormat decimalFormatter;

	/**
	 * @param precision 总精度数
	 * @param mode 		舍入模式
	 */
	public DecimalUtils(int precision, int scale, RoundingMode mode) {
		mc = new MathContext(precision, mode);
		decimalFormatter = new DecimalFormat();
		decimalFormatter.setRoundingMode(mode);
		decimalFormatter.setGroupingUsed(false);
		decimalFormatter.setMaximumIntegerDigits(precision - scale);
		decimalFormatter.setMaximumFractionDigits(scale);
		decimalFormatter.setMinimumFractionDigits(scale);
	}

	public DecimalUtils() {
		this(PRECISION, SCALE, MODE);
	}

	public BigDecimal add(BigDecimal d1, BigDecimal d2) {
		return d1.add(d2, mc);
	}

	public BigDecimal subtract(BigDecimal d1, BigDecimal d2) {
		return d1.subtract(d2, mc);
	}

	public BigDecimal multiply(BigDecimal d1, BigDecimal d2) {
		return d1.multiply(d2, mc);
	}

	public BigDecimal divide(BigDecimal d1, BigDecimal d2) {
		return d1.divide(d2, mc);
	}

	public String formatDecimal(Object n) {
		if (n.getClass() == String.class) {
		    n = new BigDecimal(n.toString());
		} else if(!(n instanceof Number)){
		    throw new IllegalArgumentException("not a number.");
        }
		return decimalFormatter.format(n);
	}

    public static String format(Object n) {
        return format(n, SCALE, MODE);
    }

    public static String format(Object n, int scale, RoundingMode mode) {
	    if(n == null) {
	        return "";
        }
        if(n.getClass() != String.class && !(n instanceof Number)){
            throw new IllegalArgumentException("not a number.");
        }
        n = n.getClass() == BigDecimal.class ? n : new BigDecimal(n.toString());
        scale = scale < 0 ? 0 : scale;
        DecimalFormat decimalFormatter = new DecimalFormat();
        decimalFormatter.setRoundingMode(mode);
        decimalFormatter.setGroupingUsed(false);
        decimalFormatter.setMaximumIntegerDigits(PRECISION - scale);
        decimalFormatter.setMaximumFractionDigits(scale);
        decimalFormatter.setMinimumFractionDigits(scale);
        return decimalFormatter.format(n);
    }

}
