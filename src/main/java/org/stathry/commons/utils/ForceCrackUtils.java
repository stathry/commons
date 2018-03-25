package org.stathry.commons.utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ForceCrackUtils {
    @Test
    public void forceCrackExcel() throws Exception {
        String path = "/doc/my/15b782e2-9758-4470-8f9c-aac5a218bf-lend-16.xlsx";
        char[] dict = (DictUtils.CHARS_0_9 + DictUtils.CHARS_a_z + DictUtils.CHARS_A_Z).toCharArray();
        System.out.println(dict);
//        InputStream inp = new FileInputStream(path);
//        Workbook wb = WorkbookFactory.create(inp, "abc");
//        if(wb != null) {
//            System.out.println();
//        }
    }
}
