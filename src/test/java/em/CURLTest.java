package em;

import com.alibaba.fastjson.JSON;
import em.dto.PayNotifyData;
import em.dto.PayNotifyResult;
import org.apache.commons.io.FileUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * CUrlTest
 *
 * @author dongdaiming(董代明)
 * @date 2019-05-17 14:23
 */
public class CURLTest {

    private static final String INPUT_FILENAME = "input.txt";
    private static final String OUTPUT_FILENAME = "output.txt";

    @Test
    public void testGenPayNotifyFromLog() throws Exception {
        File file = ResourceUtils.getFile("classpath:" + INPUT_FILENAME);
        List<String> list = FileUtils.readLines(file, "utf-8");
        String curl = "curl -X POST \\\n" +
                "  http://localhost:8120/pay/notify \\\n" +
                "  -H 'Content-Type: application/json' \\\n" +
                "  -H 'Postman-Token: 998267f8-67e4-4447-9ae0-7bed3883ee36' \\\n" +
                "  -H 'cache-control: no-cache' \\\n" +
                "  -d ";

        StringBuilder builder = new StringBuilder();
        for (String e : list) {
            int n1 = e.indexOf("{"), n2 = e.indexOf("}");
            if (n1 != -1 && n2 != -1) {
                builder.append(curl).append("'").append(e, n1, n2 + 1).append("'").append("\r\n\r\n");
            }
        }

        File output = new File(System.getProperty("user.dir") + "/src/test/resources/" + OUTPUT_FILENAME);
        FileUtils.writeStringToFile(output, builder.toString(), Charset.defaultCharset(), false);
        System.out.println(FileUtils.readFileToString(output, Charset.defaultCharset()));
    }

    @Test
    public void testGenUpdateOrderStatus() throws Exception {
        List<Long> list = IdTest.readIdList("classpath:ids.txt");
        String curl = "curl -X POST \\\n" +
                "  http://localhost:8120/maintain/update/status \\\n" +
                "  -H 'Content-Type: application/json' \\\n" +
                "  -H 'Postman-Token: 01951b42-8fd8-467b-a56d-b7fceca5efe6' \\\n" +
                "  -H 'cache-control: no-cache' \\\n" +
                "  -d ";
        String body = "{\n" +
                "\t\"orderId\":250592124731601,\n" +
                "\t\"status\":1\n" +
                "}";

        StringBuilder builder = new StringBuilder();
        for (Long e : list) {
            builder.append(curl).append("'").append(body.replaceAll("250592124731601", e.toString())).append("'").append("\r\n\r\n");
        }

        File output = new File(System.getProperty("user.dir") + "/src/test/resources/" + OUTPUT_FILENAME);
        FileUtils.writeStringToFile(output, builder.toString(), Charset.defaultCharset(), false);
        System.out.println(FileUtils.readFileToString(output, Charset.defaultCharset()));
    }

    @Test
    public void testGenPayNotify() throws IOException {
        List<PayNotifyData> data = readPayNotifyData();

        String curl = "curl -X POST \\\n" +
                "  http://localhost:8120/pay/notify \\\n" +
                "  -H 'Content-Type: application/json' \\\n" +
                "  -H 'Postman-Token: 998267f8-67e4-4447-9ae0-7bed3883ee36' \\\n" +
                "  -H 'cache-control: no-cache' \\\n" +
                "  -d ";

        StringBuilder builder = new StringBuilder();
        for (PayNotifyData e : data) {
            builder.append(curl).append("'").append(JSON.toJSONStringWithDateFormat(e, "yyyy-MM-dd HH:mm:ss")).append("'").append("\r\n\r\n");
        }

        File output = new File(System.getProperty("user.dir") + "/src/test/resources/" + OUTPUT_FILENAME);
        System.out.println(output.getPath());
        FileUtils.writeStringToFile(output, builder.toString(), Charset.defaultCharset(), false);
        System.out.println(FileUtils.readFileToString(output, Charset.defaultCharset()));
    }

    private List<PayNotifyData> readPayNotifyData() {
        ImportParams importParams = new ImportParams();
        importParams.setStartRows(1);
        List<PayNotifyResult> list = ExcelImportUtil.importExcel(new File("/TS/支付通知失败/20190717-20190719-notifyOrderFail-dest.xls"), PayNotifyResult.class, importParams);
        System.out.println(JSON.toJSONStringWithDateFormat(list, "yyyy-MM-dd HH:mm:ss"));

        return list.stream().map(e -> {
            PayNotifyData p = new PayNotifyData();
            p.setPaymentNo(e.getPayment_no());
            p.setOrderCollectionNo(e.getOrder_collection_no());
            p.setOrderId(e.getOrder_no());
            p.setPayAmount(e.getPay_amount());
            p.setAcutalPayAmount(e.getPay_amount());
            p.setPayTimeEnd(e.getGmt_modify());
            return p;
        }).collect(Collectors.toList());
    }

    @Test
    public void testGenDelPayColl() throws IOException {
        List<Long> doubleIds = IdTest.readIdList("classpath:ids.txt");
        Set<Long> ids = new HashSet<>(doubleIds);

        List<PayNotifyData> data = readPayNotifyData();

        Set<Long> dataIds = data.stream().map(e -> e.getOrderId()).collect(Collectors.toSet());
        System.out.println("doubleIds:" + ids.size() + ",dataIds size:" + dataIds.size());
//        data = data.stream().filter(e -> ids.contains(e.getOrderId())).collect(Collectors.toList());
        List<PayNotifyData> result = new ArrayList<>(data.size());
        for (PayNotifyData e : data) {
            if(ids.contains(e.getOrderId())) {
                result.add(e);
            }
        }
        System.out.println("filtered data size:" + result.size());

        StringBuilder builder = new StringBuilder();
        for (PayNotifyData e : result) {
            builder.append("delete from ord_").append(e.getOrderId() % 8).append(".t_ord_collection where order_id = ").append(e.getOrderId()).append(" and pay_payment_no = '").append(e.getPaymentNo()).append("';").append("\n");
        }
        System.out.println(builder.toString());
    }


}
