package em.order.count;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ExcelDataAligningTest
 *
 * @author dongdaiming(董代明)
 * @date 2019-04-22 12:12
 */
public class ExcelDataAligningTest {

    private static final String BASE_DIR = "/TS/creditOrderCount/";
    public static final String COUNT_SQL = "dateCreditOrderCount.sql";
    public static final String OUTPUT_FILE = "target/全国信用单统计-src-";

    private static final String LAST_COUNT_START_DATE = "2019-05-20";
    private static final String LAST_COUNT_END_DATE = "2019-05-27";
    private static final String THIS_COUNT_END_DATE = "2019-06-03";

    private static final String INPUT_TEMPLATE = "creditOrder.xlsx";
    private static final String INPUT_FILE = "creditOrderCopy.xlsx";

    /**
     * 更新城市信用单统计时间
     *
     * @throws IOException
     */
    @Test
    public void testUpdateCityOrderCountTime() throws IOException {
        File file = new File(BASE_DIR + COUNT_SQL);
        String sql = FileUtils.readFileToString(file, Charset.defaultCharset());

        if (sql.contains(LAST_COUNT_START_DATE) && sql.contains(LAST_COUNT_END_DATE) && THIS_COUNT_END_DATE.compareTo(LAST_COUNT_END_DATE) > 0) {
            sql = sql.replaceAll(LAST_COUNT_END_DATE, THIS_COUNT_END_DATE);
            sql = sql.replaceAll(LAST_COUNT_START_DATE, LAST_COUNT_END_DATE);
            FileUtils.writeStringToFile(file, sql, Charset.defaultCharset(), false);
        }

        FileUtils.deleteQuietly(new File(BASE_DIR + INPUT_FILE));
        FileUtils.copyFile(new File(BASE_DIR + INPUT_TEMPLATE), new File(BASE_DIR + INPUT_FILE));

        System.out.println("count time range: [" + LAST_COUNT_END_DATE + ", " + THIS_COUNT_END_DATE + "]");
    }

    /**
     * 信用单统计-数据对齐
     *
     * @throws Exception
     */
    @Test
    public void testAligningOrderCount() throws Exception {
        ImportParams importParams = new ImportParams();
        importParams.setStartRows(1);
        List<CreditOrderCounter> list = ExcelImportUtil.importExcel(new File(BASE_DIR + INPUT_FILE), CreditOrderCounter.class, importParams);

        List<AlignOrderCounter> data = alignOrderCount(list);

        exportCountMap(data, BASE_DIR + OUTPUT_FILE + LocalDate.now().minusDays(1) + ".xls");

    }

    /**
     * 数据对齐(假设信用产品id倒排)
     *
     * @param list
     * @return
     */
    private List<AlignOrderCounter> alignOrderCount(List<CreditOrderCounter> list) {
        List<AlignOrderCounter> data = new ArrayList<>(list.size() / 2);
        AlignOrderCounter c;
        for (int i = 0, size = list.size(); i < size; i += 2) {
            c = new AlignOrderCounter();
            c.setDate(list.get(i).getD1());
            c.setO13(list.get(i).getO1());
            c.setO11(list.get(i + 1).getO1());
            c.setO23(list.get(i).getO2());
            c.setO21(list.get(i + 1).getO2());
            c.setO33(list.get(i).getO3());
            c.setO31(list.get(i + 1).getO3());
            data.add(c);
        }
        return data;

    }

    private void exportCountMap(List<AlignOrderCounter> counters, String expFile) throws IOException {
        ExportParams exportParams = new ExportParams();
        exportParams.setCreateHeadRows(true);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, AlignOrderCounter.class, counters);
        File file = new File(expFile);
        workbook.write(new FileOutputStream(file));
        workbook.close();
        System.out.println(expFile);
    }


}
