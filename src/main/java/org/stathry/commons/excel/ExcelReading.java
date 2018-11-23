package org.stathry.commons.excel;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.stathry.commons.model.dto.FileArea;
import org.stathry.commons.utils.FileUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Excel数据读取
 *
 * @author dongdaiming
 * @date 2018/4/19
 */
public class ExcelReading {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReading.class);
    private static final String LINE_SEP = SystemUtils.LINE_SEPARATOR;
    private static final String COL_SEP = ",\t";

    public static String readToString(String path) {
        return readToStringFromExcel(path, null,
                ExcelConstant.MAX_CONTINUOUS_EMPTY_ROWS, ExcelConstant.MAX_CONTINUOUS_EMPTY_COLS, LINE_SEP, COL_SEP);
    }

    public static String readToString(String path, String password) {
        return readToStringFromExcel(path, password,
                ExcelConstant.MAX_CONTINUOUS_EMPTY_ROWS, ExcelConstant.MAX_CONTINUOUS_EMPTY_COLS, LINE_SEP, COL_SEP);
    }

    public static <T> List<T> readToObjects(String path, Class<T> clazz) {
        return readToObjects(path, clazz, new FileArea());
    }

    public static <T> List<T> readToObjects(String path, Class<T> clazz, FileArea area) {
        area = area == null ? new FileArea() : area;
        checkReadParams(path, null, area);

        List<Map<String, String>> maps = readDataToMapsFromExcel(path, null, area, null, null);
        String js = JSON.toJSONStringWithDateFormat(maps, ExcelConstant.READ_DATE_PATTERN);
        return JSON.parseArray(js, clazz);
    }

    /**
     * 读取excel数据到map
     *
     * @param path excel文件路径
     * @return
     */
    public static List<Map<String, String>> readToMaps(String path) {
        FileArea area = new FileArea();
        checkReadParams(path, null, area);

        return readDataToMapsFromExcel(path, null, area, null, null);
    }

    /**
     * 读取excel数据到map
     *
     * @param path excel文件路径
     * @param keys 生成map的key
     * @return
     */
    public static List<Map<String, String>> readToMaps(String path, List<String> keys) {
        FileArea area = new FileArea();
        checkReadParams(path, null, area);

        return readDataToMapsFromExcel(path, keys, area, null, null);
    }

    /**
     * 读取excel数据到map
     *
     * @param path excel文件路径
     * @param keys 生成map的key
     * @param area 读取数据的行列范围
     * @return
     */
    public static List<Map<String, String>> readToMaps(String path, List<String> keys, FileArea area) {
        area = area == null ? new FileArea() : area;
        checkReadParams(path, null, area);

        return readDataToMapsFromExcel(path, keys, area, null, null);
    }

    /**
     * 读取excel数据到map
     *
     * @param path       excel文件路径
     * @param keys       生成map的key
     * @param area       读取数据的行列范围
     * @param sheetIndex sheet索引
     * @return
     */
    public static List<Map<String, String>> readToMaps(String path, List<String> keys, FileArea area, Integer sheetIndex) {
        area = area == null ? new FileArea() : area;
        checkReadParams(path, sheetIndex, area);

        return readDataToMapsFromExcel(path, keys, area, sheetIndex, null);
    }

    /**
     * 读取excel数据到map
     *
     * @param path      excel文件路径
     * @param keys      生成map的key
     * @param area      读取数据的行列范围
     * @param sheetName sheet名称
     * @return
     */
    public static List<Map<String, String>> readToMaps(String path, List<String> keys, FileArea area, String sheetName) {
        area = area == null ? new FileArea() : area;
        checkReadParams(path, null, area);

        return readDataToMapsFromExcel(path, keys, area, null, sheetName);
    }

    public static List<List<String>> readToLists(String path) {
        return readToLists(path, new FileArea());
    }

    public static List<List<String>> readToLists(String path, FileArea area) {
        return readToLists(path, 0, area);
    }

    public static List<List<String>> readToLists(String path, Integer sheetIndex, FileArea area) {
        checkReadParams(path, sheetIndex, area);

        return readDataToListsFromExcel(path, sheetIndex, null, area);
    }

    public static List<List<String>> readToLists(String path, String sheetName, FileArea area) {
        checkReadParams(path, null, area);

        return readDataToListsFromExcel(path, null, sheetName, area);
    }

    private static String readToStringFromExcel(String path, String password,
                                                int maxContinuousEmptyRows, int maxContinuousEmptyCols, String lineSep, String colSep) {
        String filename = FilenameUtils.getName(path);
        InputStream in = null;
        InputStream bin = null;
        Workbook book = null;
        try {
            in = new FileInputStream(path);
            bin = new BufferedInputStream(in);
            // https://blog.csdn.net/csnewdn/article/details/53641308
            book = StringUtils.isBlank(password) ? WorkbookFactory.create(bin) : WorkbookFactory.create(bin, password);
            Sheet sheet = getBookSheet(null, null, book);
            if (sheet == null) {
                LOGGER.warn("sheet is empty, path {}.", path);
                return "";
            }

            LOGGER.info("loading data from excel {}.", filename);
            String data = readToStringFromSheet(sheet, maxContinuousEmptyRows, maxContinuousEmptyCols, lineSep, colSep);
            LOGGER.info("loaded data from excel {} completed, content:\n{}", filename, data);
            return data;
        } catch (Exception e) {
//            LOGGER.error("read excel error, path " + path, e);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(bin);
            IOUtils.closeQuietly(book);
        }
        return null;
    }

    private static String readToStringFromSheet(Sheet sheet, int maxContinuousEmptyRows, int maxContinuousEmptyCols, String lineSep, String colSep) {
        StringBuilder b = new StringBuilder();
        Row row;
        Cell cell;
        StringBuilder rowValue;
        String cellValue;
        int continuousEmptyRows = 0;
        int continuousEmptyCols;
        for (int i = 0, maxRow = ExcelConstant.MAX_ROW; i < maxRow; i++) {
            rowValue = new StringBuilder();
            row = sheet.getRow(i);
            continuousEmptyCols = 0;
            if (row == null || continuousEmptyRows >= maxContinuousEmptyRows) {
                break;
            }

            for (int j = 0, maxCol = ExcelConstant.MAX_COLUMN; j < maxCol; j++) {
                cell = row.getCell(j);
                if (cell == null || continuousEmptyCols >= maxContinuousEmptyCols) {
                    break;
                }
                cellValue = ExcelUtils.cellValue(cell);
                if (StringUtils.isBlank(cellValue)) {
                    continuousEmptyCols++;
                } else {
                    continuousEmptyCols = 0;
                    rowValue.append(cellValue).append(colSep);
                }
            }
            if (rowValue.length() == 0) {
                continuousEmptyRows++;
            } else {
                rowValue.delete(rowValue.lastIndexOf(colSep), rowValue.length() - 1);
                continuousEmptyRows = 0;
                b.append(rowValue).append(lineSep);
            }
        }
        return b.toString();
    }

    /**
     * 读取excel数据到map(默认读取第一个非空sheet,默认以首行作为map的key)
     *
     * @param path
     * @param sheetIndex
     * @param sheetName
     * @param keys
     * @param area
     * @return
     */
    private static List<Map<String, String>> readDataToMapsFromExcel(String path, List<String> keys, FileArea area, Integer sheetIndex, String sheetName) {
        String filename = FilenameUtils.getName(path);
        InputStream in = null;
        InputStream bin = null;
        Workbook book = null;
        try {
            in = new FileInputStream(path);
            bin = new BufferedInputStream(in);
            book = WorkbookFactory.create(bin);

            Sheet sheet = getBookSheet(sheetIndex, sheetName, book);
            if (sheet == null) {
                LOGGER.warn("sheet is empty, path {}.", path);
                return Collections.emptyList();
            }

            if (keys == null || keys.isEmpty()) {
                keys = readHeader(sheet);
                area.setRowStart(area.getRowStart() == 0 ? 1 : area.getRowStart());
            }

            LOGGER.info("loading data from excel {}, area {}.", filename, area.toString());
            List<Map<String, String>> data = readDataToMaps(keys, area, sheet);
            LOGGER.info("loaded data from excel {} completed, size {}.", filename, data.size());
            return data;
        } catch (Exception e) {
            LOGGER.error("read excel error, path " + path, e);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(bin);
            IOUtils.closeQuietly(book);
        }
        return null;
    }

    private static List<String> readHeader(Sheet sheet) {
        Row row = sheet.getRow(sheet.getFirstRowNum());
        List<String> keys = new ArrayList<>();
        for (int i = row.getFirstCellNum(), end = row.getLastCellNum(); i < end; i++) {
            keys.add(ExcelUtils.cellValue(row.getCell(i)));
        }
        return keys;
    }

    private static List<List<String>> readDataToListsFromExcel(String path, Integer sheetIndex, String sheetName, FileArea area) {
        String filename = FilenameUtils.getName(path);
        InputStream in = null;
        InputStream bin = null;
        Workbook book = null;
        try {
            in = new FileInputStream(path);
            bin = new BufferedInputStream(in);
            book = WorkbookFactory.create(bin);

            Sheet sheet = getBookSheet(sheetIndex, sheetName, book);
            if (sheet == null) {
                LOGGER.warn("sheet is empty, path {}.", path);
                return Collections.emptyList();
            }

            LOGGER.info("loading data from excel {}, area {}.", filename, area.toString());

            List<List<String>> data = readDataToLists(area, sheet);

            LOGGER.info("loaded data from excel {} completed, size {}.", filename, data.size());
            return data;
        } catch (Exception e) {
            LOGGER.error("read excel error, path " + path, e);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(bin);
            IOUtils.closeQuietly(book);
        }
        return null;
    }

    private static void checkReadParams(String path, Integer sheetIndex, FileArea area) {
        Assert.notNull(area, "required area.");
        Assert.isTrue(area.getColumnStart() >= 0 && area.getColumnEnd() <= ExcelConstant.MAX_COLUMN, "invalid area.");
        Assert.isTrue(area.getRowStart() >= 0 && area.getRowEnd() <= ExcelConstant.MAX_ROW, "invalid area.");
        Assert.isTrue(FileUtils.exists(path), "file not found," + path);
        String filename = FilenameUtils.getName(path);
        String type = ExcelUtils.fileNameToType(filename);
        Assert.hasText(type, "file is not a excel," + path);
        Assert.isTrue("xls".equalsIgnoreCase(type) || "xlsx".equalsIgnoreCase(type), "file is not a excel," + path);
        Assert.isTrue(sheetIndex == null || sheetIndex >= 0, "sheetIndex must great than 0.");
    }

    private static List<Map<String, String>> readDataToMaps(List<String> keys, FileArea area, Sheet sheet) {
        List<Map<String, String>> data = new ArrayList<>(area.getRowEnd() - area.getRowStart());
        Row row;
        Cell cell;
        Map<String, String> map;
        int rowStart = sheet.getFirstRowNum() > area.getRowStart() ? sheet.getFirstRowNum() : area.getRowStart();
        int rowEnd = sheet.getLastRowNum() < area.getRowEnd() ? sheet.getLastRowNum() : area.getRowEnd();
        row = sheet.getRow(rowStart);
        int cellStart = row.getFirstCellNum() > area.getColumnStart() ? row.getFirstCellNum() : area.getColumnStart();
        int cellEnd = row.getLastCellNum() < area.getColumnEnd() ? row.getLastCellNum() : area.getColumnEnd();
        Assert.notEmpty(keys, "not found keys.");
        Assert.isTrue(keys.size() <= (cellEnd + 1 - cellStart), "keys size not match to column range.");
        int keySize = keys.size();
        for (; rowStart <= rowEnd; rowStart++) {
            LOGGER.debug("loading data, rowIndex {}.", rowStart);
            row = sheet.getRow(rowStart);
            if (row == null) {
                break;
            }
            map = new HashMap<>(keySize * 2);
            for (int iCell = cellStart, ik = 0; iCell <= cellEnd; iCell++, ik++) {
                cell = row.getCell(iCell);
                if (cell == null) {
                    continue;
                }
                map.put(keys.get(ik), ExcelUtils.cellValue(cell));
            }
            data.add(map);
        }
        return data;
    }

    private static List<List<String>> readDataToLists(FileArea area, Sheet sheet) {
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
                list.add(ExcelUtils.cellValue(cell));
            }
            data.add(list);
        }
        return data;
    }

    private static Sheet getBookSheet(Integer sheetIndex, String sheetName, Workbook book) {
        if (sheetIndex != null) {
            return book.getSheetAt(sheetIndex);
        } else if (StringUtils.isNotBlank(sheetName)) {
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
