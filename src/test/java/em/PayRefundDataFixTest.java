package em;

import com.alibaba.fastjson.JSON;
import em.dto.KFOfflineRefund;
import em.dto.PayNotifyResult;
import em.dto.RefundNotifyResult;
import org.apache.commons.io.FileUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * PayRefundDataFixTest
 *
 * @author dongdaiming(董代明)
 * @date 2019-08-01
 */
public class PayRefundDataFixTest {

    private static final String OUTPUT_FILENAME = "output.txt";

    @Test
    public void testGenRefundNotify() throws IOException {
        List<RefundNotifyResult> data = readRefundNotifyData();

        String curl = "curl -X POST \\\n" +
                "  http://localhost:8120/maintain/refund/notify \\\n" +
                "  -H 'Content-Type: application/json' \\\n" +
                "  -H 'Postman-Token: 998267f8-67e4-4447-9ae0-7bed3883ee36' \\\n" +
                "  -H 'cache-control: no-cache' \\\n" +
                "  -d ";

        StringBuilder builder = new StringBuilder();
        for (RefundNotifyResult e : data) {
            builder.append(curl).append("'").append(JSON.toJSONStringWithDateFormat(e, "yyyy-MM-dd HH:mm:ss")).append("'").append("\r\n\r\n");
        }

        File output = new File(System.getProperty("user.dir") + "/src/test/resources/" + OUTPUT_FILENAME);
        System.out.println(output.getPath());
        FileUtils.writeStringToFile(output, builder.toString(), Charset.defaultCharset(), false);
        System.out.println(FileUtils.readFileToString(output, Charset.defaultCharset()));
    }

    @Test
    public void testGenPayNotify() throws IOException {
        List<PayNotifyResult> data = readPayNotifyData();

        String curl = "curl -X POST \\\n" +
                "  http://localhost:8120/pay/notify \\\n" +
                "  -H 'Content-Type: application/json' \\\n" +
                "  -H 'Postman-Token: 998267f8-67e4-4447-9ae0-7bed3883ee36' \\\n" +
                "  -H 'cache-control: no-cache' \\\n" +
                "  -d ";

        StringBuilder builder = new StringBuilder();
        for (PayNotifyResult e : data) {
            builder.append(curl).append("'").append(JSON.toJSONStringWithDateFormat(e, "yyyy-MM-dd HH:mm:ss")).append("'").append("\r\n");
        }

        File output = new File(System.getProperty("user.dir") + "/src/test/resources/" + OUTPUT_FILENAME);
        System.out.println(output.getPath());
        FileUtils.writeStringToFile(output, builder.toString(), Charset.defaultCharset(), false);
        System.out.println(FileUtils.readFileToString(output, Charset.defaultCharset()));
    }

/*
    @Test
    public void testGenDelPayColl() throws IOException {
        List<Long> doubleIds = IdTest.readIdList("classpath:ids.txt");
        Set<Long> ids = new HashSet<>(doubleIds);

        List<PayNotifyResult> data = readPayNotifyData();

        Set<Long> dataIds = data.stream().map(e -> e.getOrderId()).collect(Collectors.toSet());
        System.out.println("doubleIds:" + ids.size() + ",dataIds size:" + dataIds.size());
//        data = data.stream().filter(e -> ids.contains(e.getOrderId())).collect(Collectors.toList());
        List<PayNotifyResult> result = new ArrayList<>(data.size());
        for (PayNotifyData e : data) {
            if (ids.contains(e.getOrderId())) {
                result.add(e);
            }
        }
        System.out.println("filtered data size:" + result.size());

        StringBuilder builder = new StringBuilder();
        for (PayNotifyData e : result) {
            builder.append("delete from ord_").append(e.getOrderId() % 8).append(".t_ord_collection where order_id = ").append(e.getOrderId()).append(" and pay_payment_no = '").append(e.getPaymentNo()).append("';").append("\n");
        }
        System.out.println(builder.toString());
    }*/

    private List<PayNotifyResult> readPayNotifyData() {
        ImportParams importParams = new ImportParams();
        importParams.setStartRows(1);
        List<PayNotifyResult> list = ExcelImportUtil.importExcel(new File("/TS/数据修复/月度对账数据修复/fix/pay1908.xlsx"), PayNotifyResult.class, importParams);
        System.out.println(JSON.toJSONStringWithDateFormat(list, "yyyy-MM-dd HH:mm:ss"));

        return list;
    }

    private List<RefundNotifyResult> readRefundNotifyData() {
        ImportParams importParams = new ImportParams();
        importParams.setStartRows(1);
        List<RefundNotifyResult> list = ExcelImportUtil.importExcel(new File("/TS/数据修复/月度对账数据修复/fix/refund1908.xlsx"), RefundNotifyResult.class, importParams);
        System.out.println(JSON.toJSONStringWithDateFormat(list, "yyyy-MM-dd HH:mm:ss"));

        return list;
    }

    @Test
    public void testKFOfflineRefund() {
        ImportParams importParams = new ImportParams();
        importParams.setStartRows(1);
        List<KFOfflineRefund> list = ExcelImportUtil.importExcel(new File("/TS/客服线下退款/客服线下退款-89.xlsx"), KFOfflineRefund.class, importParams);

        System.out.println(JSON.toJSONStringWithDateFormat(list, "yyyy-MM-dd HH:mm:ss"));
        for (KFOfflineRefund e : list) {
        }

    }

}
