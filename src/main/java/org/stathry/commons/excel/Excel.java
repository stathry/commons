package org.stathry.commons.excel;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;

/**
 * TODO
 *
 * @author stathry
 * @date 2018/4/19
 */
public interface Excel {

    String xls = "xls";

    String xlsx = "xlsx";

    default String fileNameToType(String filename) {
        if (StringUtils.isBlank(filename)) {
            return "";
        }
        String name =FilenameUtils.getExtension(filename);
        String type = "";
        if(name.endsWith(xls)) {
            type = xls;
        } else if(name.endsWith(xlsx)) {
            type = xlsx;
        }
        return type;
    }

    default boolean isExcel(String filename) {
        if (StringUtils.isBlank(filename)) {
            return false;
        }
        String name =FilenameUtils.getExtension(filename);
        boolean is = false;
        if(name.endsWith(xls)) {
            is = true;
        } else if(name.endsWith(xlsx)) {
            is = true;
        }
        return is;
    }

}
