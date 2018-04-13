package org.stathry.commons.utils;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ForceCrackUtils {
    @Test
    public void forceCrackExcel() throws Exception {
        String path = "/doc/temp.xlsx";
        InputStream inp = new FileInputStream(path);
//        String p = "10052350";
//        String p = "1005ms2350";
//        String p = "1005Ms2350";
//        String p = "10052350ms";
//        String p = "10052350Ms";
//
//       String p = "090988";
        String p = "0909ms88";
//        String p = "1005Ms2350";
//        String p = "10052350ms";
//        String p = "10052350Ms";
        Workbook wb = null;
        try {
            wb = WorkbookFactory.create(inp, p);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Assert.assertTrue(wb != null);
    }
}
