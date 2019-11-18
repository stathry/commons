package em;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SpiderTest
 *
 * @author dongdaiming(董代明)
 * @date 2019-07-30
 */
public class SpiderTest {

    /**
     * 提取文本中的订单号
     *
     * @throws Exception
     */
    @Test
    public void testFindOrderId() throws Exception {
        File file = ResourceUtils.getFile("classpath:input.txt");
        String input = FileUtils.readFileToString(file, "utf-8");
        System.out.println(StringUtils.abbreviate(input, 200));
        // {\"orderId\":376197214110193
        // String regex = "(?<=(\"" + "orderId" + "\":\")).*?(?=(\"))";
        String regex = "\"orderId\":\"(.*?)\\\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(input);
        Set<Long> orders = new HashSet<>(100);
        while (m.find()) {
            System.out.println(1);
            orders.add(Long.valueOf(m.group(1)));
        }

        System.out.println(orders.size());
        System.out.println(orders);
    }

    @Test
    public void testFindAndCountText() throws Exception {
        File file = ResourceUtils.getFile("classpath:input.txt");
        String input = FileUtils.readFileToString(file, "utf-8");
        // 提取文中汉字字符串
//        Pattern pattern = Pattern.compile("(([\u3400-\u9FFF]|，|:){3,50})");
        Pattern pattern = Pattern.compile("(\\{\\S+\\}\\})");
        Matcher m = pattern.matcher(input);
        Map<String, LongAdder> wordCountMap = new HashMap<>(1000);
        LongAdder adder;
        LongAdder total = new LongAdder();
        String word;
        while (m.find()) {
            word = m.group(1);
            adder = wordCountMap.get(word);
            if (adder == null) {
                adder = new LongAdder();
                wordCountMap.put(word, adder);
            }
            adder.increment();
            total.increment();
        }

        System.out.println(total);
        System.out.println(wordCountMap.size());
        System.out.println(JSON.toJSONString(wordCountMap));
    }

    @Test
    public void testFindAndFilterText() throws Exception {
        LocalDate startDate = LocalDate.of(2019, 8, 2);
        for (int i = 0; i < 14; i++) {
            String daeteStr = startDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            findAndKeyWordFromFile("/TS/支付分问题/log-" + daeteStr + ".json", daeteStr);
            startDate = startDate.minusDays(1);
        }
    }

    private void findAndKeyWordFromFile(String filePath, String dateStr) throws IOException {
        String startFlag = "机柜弹宝处理中,请稍后重试 ";
        String endFlag = ",\"level\"";
        File file = new File(filePath);
        List<String> lines = FileUtils.readLines(file, "utf-8");
        List<String> targetLines = new ArrayList<>(lines.size());
        String json;

        for (String line : lines) {
            json = parseLineToJson(startFlag, endFlag, line);
            targetLines.add(json);
        }
        String data = StringUtils.join(targetLines, "\n");
        File targetFile = new File("/TS/支付分问题/keyWord." + dateStr + ".json");
        FileUtils.writeStringToFile(targetFile, data, "utf-8", false);
    }

    private String parseLineToJson(String startFlag, String endFlag, String line) {
        line = line.substring(line.indexOf(startFlag) + startFlag.length(), line.indexOf(endFlag) - 1);
        line = StringEscapeUtils.unescapeJava(line);
        String json = line.substring(0, line.indexOf("}}") + 2);
        String uuid = line.substring(line.indexOf("}}")).replaceAll("}", "");
        JSONObject src, target;
        src = JSON.parseObject(json);
        target = new JSONObject();
        target.put("id", src.get("id"));
        target.put("mac", src.get("mac"));
        target.put("gmtCreate", src.get("gmtCreate"));
        target.put("uuid", uuid);
        return target.toJSONString();
    }

}
