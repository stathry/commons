package org.stathry.commons.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.stathry.commons.enums.FileArea;

import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author dongdaiming
 * @date 2018/4/19
 */
public class ExcelWritting implements ExcelWritable {
    @Override
    public void writeMaps(String path, List<Map<String, String>> maps, List<String> keys) {

    }

    @Override
    public void writeMaps(String path, List<Map<String, String>> maps, List<String> keys, FileArea area) {

    }

    @Override
    public void writeMaps(String path, List<Map<String, String>> maps, List<String> keys, List<String> titles, FileArea area) {

    }

    @Override
    public void writeMaps(Sheet sheet, List<Map<String, String>> maps, List<String> keys, List<String> titles, FileArea area) {

    }

    @Override
    public void writeLinkedMaps(String path, List<Map<String, String>> maps) {

    }

    @Override
    public void writeLinkedMaps(String path, List<Map<String, String>> maps, List<String> titles) {

    }

    @Override
    public void writeLinkedMaps(Sheet sheet, List<Map<String, String>> maps, List<String> titles) {

    }
}
