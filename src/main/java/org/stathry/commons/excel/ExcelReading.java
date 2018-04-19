package org.stathry.commons.excel;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.stathry.commons.enums.FileArea;
import org.stathry.commons.pojo.config.ExcelRange;
import org.stathry.commons.utils.FileUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel数据读取
 *
 * @author dongdaiming
 * @date 2018/4/19
 */
@Component
public class ExcelReading implements ExcelReadable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReading.class);

    @Override
    public List<Map<String, String>> readToMaps(String path, List<String> keys) {
        return readToMaps(path, keys, new FileArea(0, maxColumn(), 0, maxRow()));
    }

    @Override
    public List<Map<String, String>> readToMaps(String path, List<String> keys, FileArea area) {
        return readToMaps(path, 0, keys, area);
    }

    @Override
    public List<Map<String, String>> readToMaps(String path, Integer sheetIndex, List<String> keys, FileArea area) {
        checkReadParams(path, sheetIndex, keys, area);

        return readDataToMapsFromExcel(path, sheetIndex,null, keys, area);
    }

    @Override
    public List<Map<String, String>> readToMaps(String path, String sheetName, List<String> keys, FileArea area) {
        checkReadParams(path, null, keys, area);

        return readDataToMapsFromExcel(path, null,sheetName, keys, area);
    }

    @Override
    public List<List<String>> readToLists(String path) {
        return readToLists(path, new FileArea(0, maxColumn(), 0, maxRow()));
    }

    @Override
    public List<List<String>> readToLists(String path, FileArea area) {
        return readToLists(path, 0, area);
    }

    @Override
    public List<List<String>> readToLists(String path, Integer sheetIndex, FileArea area) {
        checkReadParams(path, sheetIndex, area);

        return readDataToListsFromExcel(path, sheetIndex, null, area);
    }

    @Override
    public List<List<String>> readToLists(String path, String sheetName, FileArea area) {
        checkReadParams(path, null, area);

        return readDataToListsFromExcel(path, null, sheetName, area);
    }

    @Override
    public String readOneCell(String path, int rowIndex, int cellIndex) {
        return null;
    }

    @Override
    public String readOneCell(Sheet sheet, int rowIndex, int cellIndex) {
        return null;
    }

    private List<Map<String, String>> readDataToMapsFromExcel(String path, Integer sheetIndex, String sheetName,
                                                              List<String> keys, FileArea area) {
        String filename = FilenameUtils.getName(path);
        InputStream in = null;
        InputStream bin = null;
        Workbook book = null;
        try {
            in = new FileInputStream(path);
            bin = new BufferedInputStream(in);
            book = WorkbookFactory.create(bin);

            Sheet sheet = getBookSheet(sheetIndex, sheetName, book);
            if(sheet == null) {
                LOGGER.warn("sheet is empty, path {}.", path);
                return Collections.emptyList();
            }

            LOGGER.info("loading data from excel {}, area {}.", filename, area.toString());
            List<Map<String, String>> data = readDataToMaps(keys, area, sheet);
            LOGGER.info("loaded data from excel {} completed, size {}.", filename, data.size());
            return data;
        } catch (Exception e) {
            LOGGER.error("read excel {} error.", path);
            LOGGER.error("read excel error.", e);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(bin);
            IOUtils.closeQuietly(book);
        }
        return null;
    }

    private List<List<String>> readDataToListsFromExcel(String path, Integer sheetIndex, String sheetName, FileArea area) {
        String filename = FilenameUtils.getName(path);
        InputStream in = null;
        InputStream bin = null;
        Workbook book = null;
        try {
            in = new FileInputStream(path);
            bin = new BufferedInputStream(in);
            book = WorkbookFactory.create(bin);

            Sheet sheet = getBookSheet(sheetIndex, sheetName, book);
            if(sheet == null) {
                LOGGER.warn("sheet is empty, path {}.", path);
                return Collections.emptyList();
            }

            LOGGER.info("loading data from excel {}, area {}.", filename, area.toString());

            List<List<String>> data = readDataToLists(area, sheet);

            LOGGER.info("loaded data from excel {} completed, size {}.", filename, data.size());
            return data;
        } catch (Exception e) {
            LOGGER.error("read excel {} error.", path);
            LOGGER.error("read excel error.", e);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(bin);
            IOUtils.closeQuietly(book);
        }
        return null;
    }

    private void checkReadParams(String path, Integer sheetIndex, List<String> keys, FileArea area) {
        Assert.notNull(area, "required area.");
        Assert.isTrue(area.getColumnStart() >= 0 && area.getColumnEnd() <= maxColumn(), "invalid area.");
        Assert.isTrue(area.getRowStart() >= 0 && area.getRowEnd() <= maxRow(), "invalid area.");
        Assert.isTrue(FileUtils.exists(path), "file not found," + path);
        Assert.notEmpty(keys, "required keys.");
        Assert.isTrue(keys.size() == (area.getColumnEnd() - area.getColumnStart()), "keys size not match to columns.");
        String filename = FilenameUtils.getName(path);
        String type =fileNameToType(filename);
        Assert.hasText(type, "file is not a excel," + path);
        Assert.isTrue(sheetIndex == null || sheetIndex >= 0, "sheetIndex must great than 0.");
    }

    private void checkReadParams(String path, Integer sheetIndex, FileArea area) {
        Assert.notNull(area, "required area.");
        Assert.isTrue(area.getColumnStart() >= 0 && area.getColumnEnd() <= maxColumn(), "invalid area.");
        Assert.isTrue(area.getRowStart() >= 0 && area.getRowEnd() <= maxRow(), "invalid area.");
        Assert.isTrue(FileUtils.exists(path), "file not found," + path);
        String filename = FilenameUtils.getName(path);
        String type =fileNameToType(filename);
        Assert.hasText(type, "file is not a excel," + path);
        Assert.isTrue(sheetIndex == null || sheetIndex >= 0, "sheetIndex must great than 0.");
    }

    private List<Map<String, String>> readDataToMaps(List<String> keys, FileArea area, Sheet sheet) {
        List<Map<String, String>> data = new ArrayList<>(area.getRowEnd() - area.getRowStart());
        Row row;
        Cell cell;
        int keySize = keys.size();
        Map<String, String> map;

        for (int iRow = area.getRowStart(), rowEnd = area.getRowEnd(); iRow < rowEnd; iRow++) {
            LOGGER.debug("loading data, rowIndex {}.", iRow);
            row = sheet.getRow(iRow);
            if (row == null) {
                break;
            }
            map = new HashMap<>(keySize * 2);
            for (int iCell = area.getColumnStart(), cellEnd = area.getColumnEnd(); iCell < cellEnd; iCell++) {
                cell = row.getCell(iCell);
                if (cell == null) {
                    continue;
                }
                map.put(keys.get(iCell), cellValue(cell));
            }
            data.add(map);
        }
        return data;
    }

    private List<List<String>> readDataToLists(FileArea area, Sheet sheet) {
        List<List<String>> data = new ArrayList<>(area.getRowEnd() - area.getRowStart());
        Row row;
        Cell cell;
        List<String> list;
        int columns = area.getColumnEnd() - area.getColumnStart();
        for (int iRow = area.getRowStart(), rowEnd = area.getRowEnd(); iRow < rowEnd; iRow++) {
            LOGGER.debug("loading data, rowIndex {}.", iRow);
            row = sheet.getRow(iRow);
            if (row == null) {
                break;
            }
            list = new ArrayList<>(columns);
            for (int iCell = area.getColumnStart(), cellEnd = area.getColumnEnd(); iCell < cellEnd; iCell++) {
                cell = row.getCell(iCell);
                if (cell == null) {
                    continue;
                }
                list.add(cellValue(cell));
            }
            data.add(list);
        }
        return data;
    }

    private Sheet getBookSheet(Integer sheetIndex, String sheetName, Workbook book) {
        if (sheetIndex != null) {
            return book.getSheetAt(sheetIndex);
        } else if(StringUtils.isNotBlank(sheetName)) {
            return book.getSheet(sheetName);
        } else {
            Sheet sheet = null;
            Iterator<Sheet> it = book.sheetIterator();
            while (it.hasNext()) {
                sheet = it.next();
                if (sheet != null) {
                    break;
                }
            }
            return sheet;
        }
    }
}
