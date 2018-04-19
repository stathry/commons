package org.stathry.commons.excel;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.stathry.commons.enums.FileArea;
import org.stathry.commons.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel数据写入
 *
 * @author stathry
 * @date 2018/4/19
 */
public interface ExcelWritable extends Excel {

    void writeMaps(String path, List<Map<String, String>> maps, List<String> keys);

    void writeMaps(String path, List<Map<String, String>> maps, List<String> keys, FileArea area);

    void writeMaps(String path, List<Map<String, String>> maps, List<String> keys, List<String> titles, FileArea area);

    void writeMaps(Sheet sheet, List<Map<String, String>> maps, List<String> keys, List<String> titles, FileArea area);

    void writeLinkedMaps(String path, List<Map<String, String>> maps);

    void writeLinkedMaps(String path, List<Map<String, String>> maps, List<String> titles);

    void writeLinkedMaps(Sheet sheet, List<Map<String, String>> maps, List<String> titles);

    default String writeDatePattern() {
        return "yyyy-MM-dd HH:mm:ss";
    }

    /**
     * workbook中的数据写入到file
     * @param book
     * @param file
     * @throws IOException
     */
    default void writeBook(Workbook book, File file) throws IOException {
        if(file == null || !FileUtils.createNewFile(file)) {
            throw new FileNotFoundException(file == null ? "" : file.getPath());
        }
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            book.write(out);
        } catch (IOException e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(book);
        }
    }

}
