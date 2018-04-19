package org.stathry.commons.excel;

import org.apache.commons.lang3.StringUtils;

/**
 * Excel类型
 *
 * @author dongdaiming
 * @date 2018/4/19
 */
public enum  ExcelType {
    /** excel 2003 */
    xls,
    /** excel 2007 */
    xlsx;

    public static ExcelType toType(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        ExcelType t = null;
        for (ExcelType e : values()) {
            if(e.name().equalsIgnoreCase(name)) {
                t = e;
                break;
            }
        }
        return t;
    }
}
