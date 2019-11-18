package em;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import em.dto.CollRefundCount;
import em.dto.OrderBasic;
import em.dto.OrderPayResultDTO;
import em.dto.Refund;
import em.dto.RefundOrder;
import org.apache.commons.io.FileUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * PayRefundDataFixTest
 *
 * @author dongdaiming(董代明)
 * @date 2019-08-01
 */
public class WxPayDataFixTest {

    private static final String INPUT_FILENAME = "input.txt";
    private static final String OUTPUT_FILENAME = "output.txt";
    private static final String ID_FILENAME = "ids.txt";

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Test
    public void testGenRefundNotify() throws IOException {
        List<Refund> data = readRefundData();

        String curl = "curl -X POST " +
                "  http://localhost:8120/maintain/refund/create " +
                "  -H 'Content-Type: application/json' " +
                "  -d ";

        StringBuilder builder = new StringBuilder();
        for (Refund e : data) {
            e.setStatus(1);
            e.setAmountRefundable(e.getAmountRefunded());
            builder.append(curl).append("'").append(JSON.toJSONStringWithDateFormat(e, "yyyy-MM-dd HH:mm:ss")).append("'").append("\r\n");
        }

        File output = new File(System.getProperty("user.dir") + "/src/test/resources/" + OUTPUT_FILENAME);
        System.out.println(output.getPath());
        FileUtils.writeStringToFile(output, builder.toString(), Charset.defaultCharset(), false);
        System.out.println(FileUtils.readFileToString(output, Charset.defaultCharset()));
    }

    private List<Refund> readRefundData() {
        ImportParams importParams = new ImportParams();
        importParams.setStartRows(1);
        List<Refund> list = ExcelImportUtil.importExcel(new File("/TS/wx-pay-fix/pes数据/refund-191102.xlsx"), Refund.class, importParams);
        System.out.println(JSON.toJSONStringWithDateFormat(list, "yyyy-MM-dd HH:mm:ss"));
        return list;
    }

    @Test
    public void testParsePayNotifyExcel() {
        ImportParams importParams = new ImportParams();
        importParams.setStartRows(1);
        List<OrderPayResultDTO> list = ExcelImportUtil.importExcel(new File("/TS/wx-pay-fix/pes数据/payNotify-191102.xlsx"), OrderPayResultDTO.class, importParams);
        // System.out.println(JSON.toJSONStringWithDateFormat(list, "yyyy-MM-dd HH:mm:ss"));
        list.forEach(e -> System.out.println(JSON.toJSONStringWithDateFormat(e, DATE_FORMAT)));
    }

    @Test
    public void testFilterType5() throws IOException {
        List<OrderPayResultDTO> list = readPayNotifyList();
        List<String> payResultDTOList = new ArrayList<>(4000);
        Map<Long, OrderBasic> orderBasicMap = readOrderBasicMap();
        Set<Long> set = new HashSet<>(4000);
        for (OrderPayResultDTO payResult : list) {
            OrderBasic basic = orderBasicMap.get(payResult.getOrderId());
            if (true) {
            // if (basic.getOrderType().intValue() == 5) {
                set.add(basic.getOrderId());
                payResultDTOList.add(JSON.toJSONStringWithDateFormat(payResult, DATE_FORMAT));
            }
        }

        File output = new File(System.getProperty("user.dir") + "/src/test/resources/" + OUTPUT_FILENAME);

        FileUtils.writeLines(output, payResultDTOList, false);

        System.out.println("type5 orderIdList:" + JSON.toJSONString(set));
    }

    @Test
    public void testParsePayNotify() {

    }

    @Test
    public void testGenPayNotify() throws IOException {
        List<OrderPayResultDTO> list = readPayNotifyList();

        List<OrderBasic> orderBasicList = readPayNotifyData(list);

        List<OrderPayResultDTO> payList = new ArrayList<>();
        orderBasicList.stream().forEach(e -> payList.addAll(e.getPayList()));
        System.out.println("payList.size:" + payList.size());
        System.out.println("payList:" + JSON.toJSONStringWithDateFormat(payList, DATE_FORMAT));

        String curl = "curl -X POST  http://localhost:8120/pay/notify  -H 'Content-Type: application/json'" + "  -d ";

        StringBuilder builder = new StringBuilder();
        for (OrderPayResultDTO e : payList) {
            builder.append(curl).append("'").append(JSON.toJSONStringWithDateFormat(e, "yyyy-MM-dd HH:mm:ss")).append("'").append("\r\n");
        }

        File output = new File(System.getProperty("user.dir") + "/src/test/resources/" + OUTPUT_FILENAME);
        System.out.println(output.getPath());
        FileUtils.writeStringToFile(output, builder.toString(), Charset.defaultCharset(), false);
        System.out.println(FileUtils.readFileToString(output, Charset.defaultCharset()));
    }

    @Test
    public void testCountRefund() throws IOException {
        ImportParams importParams = new ImportParams();
        importParams.setStartRows(1);
        List<RefundOrder> list = ExcelImportUtil.importExcel(new File("/TS/wx-pay-fix/20191029refunds-2.xlsx"), RefundOrder.class, importParams);
        // System.out.println(JSON.toJSONStringWithDateFormat(list, "yyyy-MM-dd HH:mm:ss"));

        Map<Long, List<RefundOrder>> pesRefundListMap = list.stream().collect(Collectors.groupingBy(ref -> ref.getOrderno()));
        Map<Long, Integer> pesRefundCountMap = new HashMap<>(4000);
        pesRefundListMap.forEach((k, v) -> pesRefundCountMap.put(k, v.size()));
        Map<Long, Integer> ordRefundCountMap = readOrderRefundCountMap();
        MapDifference<Long, Integer> difference = Maps.difference(ordRefundCountMap, pesRefundCountMap);
        Map<Long, MapDifference.ValueDifference<Integer>> entriesDiffering = difference.entriesDiffering();
        System.out.println(JSON.toJSONString(entriesDiffering, SerializerFeature.WriteNonStringKeyAsString));

        System.out.println();
        entriesDiffering.forEach((k, v) -> {
            System.out.println(k+ "\t pes:" + pesRefundCountMap.get(k) + "\t ord:" + ordRefundCountMap.get(k));
        });

    }

    @Test
    public void testCompareCollRefundCount() throws IOException {
        Map<Long, Integer> refundCountMap = readOrderRefundCountMap();
        Map<Long, Integer> collCountMap = readOrderCollCountMap();
        Map<Long, CollRefundCount> countMap = new HashMap<>();
        collCountMap.forEach((k, v) -> {
            CollRefundCount count = new CollRefundCount();
            count.setCollCount(v);
            Integer rc = refundCountMap.get(k);
            count.setRefCount(rc == null ? 0 : rc);
            if(count.getCollCount().intValue() - count.getRefCount().intValue() > 0) {
            // if(true) {
                System.out.println(k);
                // System.out.println(k + "," + count.getCollCount() + "," + count.getRefCount());
                countMap.put(k, count);
            }
        });

        System.out.println(countMap.size());

        System.out.println(JSON.toJSONString(countMap));

    }

    private List<OrderPayResultDTO> readPayNotifyList() throws IOException {
        File input = new File(System.getProperty("user.dir") + "/src/test/resources/" + INPUT_FILENAME);
        // System.out.println(FileUtils.readFileToString(input, Charset.defaultCharset()));

        List<OrderPayResultDTO> list = new ArrayList<>(4000);
        List<String> lines = FileUtils.readLines(input, "utf-8");
        lines.forEach(line -> list.add(JSON.parseObject(line, OrderPayResultDTO.class)));

        System.out.println(list.size());
        Assert.assertEquals(2482, list.size());
        System.out.println(JSON.toJSONStringWithDateFormat(list.get(0), DATE_FORMAT));

        Set<Long> orderIdSet = new HashSet<>();
        list.forEach(pay -> orderIdSet.add(pay.getOrderId()));
        System.out.println("orderIdSet:" + orderIdSet.size() + "\t" + orderIdSet);
        return list;
    }

    private List<OrderBasic> readPayNotifyData(List<OrderPayResultDTO> list) throws IOException {
        List<OrderBasic> orderBasicList = new ArrayList<>(4000);
        Map<Long, OrderBasic> orderBasicMap = readOrderBasicMap();
        Map<Long, List<OrderPayResultDTO>> orderPayListMap = list.stream().collect(Collectors.groupingBy(pay -> pay.getOrderId()));
        Set<Long> idSet = new HashSet<>();
        orderPayListMap.forEach((k, v) -> {
            OrderBasic basic = orderBasicMap.get(k);
            basic.setPayList(v);
            if (basic.getOrderType().intValue() == 5) {
                orderBasicList.add(basic);
                idSet.add(k);
            }
        });

        Collections.sort(orderBasicList);
        System.out.println("idSize:" + orderBasicList.size() + ", idSet\t" + idSet);
        System.out.println("orderBasicList:" + JSON.toJSONStringWithDateFormat(orderBasicList, DATE_FORMAT));
        return orderBasicList;
    }

    private Map<Long, OrderBasic> readOrderBasicMap() throws IOException {
        Map<Long, OrderBasic> map = new HashMap<>(4000);
        File input = new File(System.getProperty("user.dir") + "/src/test/resources/" + ID_FILENAME);
        List<String> lines = FileUtils.readLines(input, "utf-8");
        lines.stream().forEach(line -> {
            String[] arr = line.split(",");
            OrderBasic basic = new OrderBasic();
            basic.setOrderId(Long.valueOf(arr[0]));
            basic.setOrderType(Integer.valueOf(arr[1]));
            basic.setStatus(Integer.valueOf(arr[2]));
            map.put(basic.getOrderId(), basic);
        });

        return map;
    }

    private Map<Long, Integer> readOrderRefundCountMap() throws IOException {
        Map<Long, Integer> map = new HashMap<>(4000);
        File input = new File(System.getProperty("user.dir") + "/src/test/resources/" + ID_FILENAME);
        List<String> lines = FileUtils.readLines(input, "utf-8");
        lines.stream().forEach(line -> {
            String[] arr = line.split("\\s+");
            map.put(Long.valueOf(arr[0]), Integer.valueOf(arr[1]));
        });

        return map;
    }

    private Map<Long, Integer> readOrderCollCountMap() throws IOException {
        Map<Long, Integer> map = new HashMap<>(4000);
        File input = new File(System.getProperty("user.dir") + "/src/test/resources/" + INPUT_FILENAME);
        List<String> lines = FileUtils.readLines(input, "utf-8");
        lines.stream().forEach(line -> {
            String[] arr = line.split("\\s+");
            map.put(Long.valueOf(arr[0]), Integer.valueOf(arr[1]));
        });

        return map;
    }

}
