package org.stathry.commons.excel;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ExcelWriting
 *
 * @author dongdaiming
 * @date 2018/4/19
 */
public class ExcelWriting {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelWriting.class);

    public static void writeMaps(String path, List<Map<String, Object>> maps, List<String> keys) {
        checkParams(path, maps, keys);
        writeMapsToExcel(path, maps, keys, null);
    }

    public static void writeMaps(String path, List<Map<String, Object>> maps, List<String> keys, List<String> header) {
        checkParams(path, maps, keys);
        writeMapsToExcel(path, maps, keys, header);
    }

    public static void writeLinkedMaps(String path, List<LinkedHashMap<String, Object>> maps) {
        checkParams(path, maps, null);
        writeMapsToExcel(path, maps, null, null);
    }

    public static void writeLinkedMaps(String path, List<LinkedHashMap<String, Object>> maps, List<String> header) {
        checkParams(path, maps, null);
        writeMapsToExcel(path, maps, null, header);
    }

    public static boolean writeMapsToExcel(String path, List<? extends Map> maps, List<String> keys, List<String> header) {
        File file = createNewFile(path);
        if(file == null || !file.exists()) {
            LOGGER.error("create new excel failed, path {}.", path);
            return false;
        }

        LOGGER.info("writing data of maps, size {}, path {}.", maps.size(), path);

        boolean r = true;
        String type = FilenameUtils.getExtension(path).toLowerCase();
        Workbook book = null;
        try {
            book = "xls".equals(type) ? new HSSFWorkbook() : new XSSFWorkbook();
            Sheet sheet = book.createSheet(FilenameUtils.getBaseName(path));

            writeHeader(sheet, header);
            writeData(book, sheet, maps, keys, header);
            LOGGER.info("write data of maps completion, size {}, path {}.", maps.size(), path);
            book.write(new FileOutputStream(file));
        } catch (Exception e) {
            r = false;
            LOGGER.error("write data of maps error.", e);
        } finally {
            if(book != null) {
                try {
                    book.close();
                } catch (IOException e) {
                }
            }
        }

        return r;
    }

    private static void writeData(Workbook book, Sheet sheet, List<? extends Map> maps, List<String> keys, List<String> header) {
        int ir = sheet.getLastRowNum();
        ir = (header == null || header.isEmpty()) ? ir : (ir + 1);
        Row row;
        int rows = maps.size();
        int cols;
        Map<String, Object> map;
        Object value;
        CellStyle dateStyle = ExcelUtils.createDateStyle(book);
        CellStyle floatStyle = ExcelUtils.createFloatStyle(book);
        if(keys != null && !keys.isEmpty()) {
            cols = keys.size();
            for (int i = 0; i < rows; i++, ir++) {
                if(i % 50 == 0) {
                    LOGGER.info("write excel rows index {}.", i);
                }
                row = sheet.createRow(ir);
                map = maps.get(i);
                for (int j = 0; j < cols; j++) {
                    value = map.get(keys.get(j));
                    ExcelUtils.createCell(row, j, value, dateStyle, floatStyle);
                }
            }
        } else {
            int j;
            for (int i = 0; i < rows; i++, ir++) {
                if(i % 50 == 0) {
                    LOGGER.info("write excel rows index {}.", i);
                }
                row = sheet.createRow(ir);
                map = maps.get(i);
                j = 0;
                for (Map.Entry<String, Object> e : map.entrySet()) {
                    ExcelUtils.createCell(row, j++, e.getValue(), dateStyle, floatStyle);
                }
            }
        }
    }

    private static void writeHeader(Sheet sheet, List<String> header) {
        if(header == null || header.isEmpty()) {
            return;
        }
        Row row = sheet.createRow(0);
        Cell cell;
        for (int i = 0, size = header.size(); i < size; i++) {
            cell = row.createCell(i, Cell.CELL_TYPE_STRING);
            cell.setCellValue(header.get(i));
        }
    }

    private static File createNewFile(String path) {
        File file = new File(path);
        try {
            file.getParentFile().mkdirs();
            if(file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            LOGGER.error("create new file error, path " + path, e);
            return null;
        }
        return file;
    }

    private static void checkParams(String path, List<? extends Map> maps, List<String> keys) {
        Assert.hasText(path, "required path.");
        String type = FilenameUtils.getExtension(path).toLowerCase();
        Assert.isTrue("xls".equals(type) || "xlsx".equals(type), "not support excel type.");
        Assert.notEmpty(maps, "required maps data.");
        Assert.notEmpty(maps.get(0), "required maps data.");
        Assert.isTrue(maps.get(0) instanceof LinkedHashMap || (keys != null && !keys.isEmpty()),
                "type of maps is not LinkedHashMap or keys is empty.");
    }

}
