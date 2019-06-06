package em;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.util.ResourceUtils;
import org.stathry.commons.utils.DistributeIdUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * IdTest
 *
 * @author dongdaiming(董代明)
 * @date 2019-04-17 18:09
 */
public class IdTest {

    @Test
    public void testIdGroup() throws IOException {
        Map<Integer, List<Long>> idGroupMap = readAndGroupId("classpath:ids.txt");

        idGroupMap.forEach((k, v) -> System.out.println(k + "\n (" + StringUtils.join(v, ',') + ")"));
    }

    @Test
    public void testIdsToList() throws IOException {
        List<Long> list = readIdList("classpath:ids.txt");
        System.out.println(list.size());
//        System.out.println("" + StringUtils.join(list, '\n') + "");
        System.out.println("(" + StringUtils.join(list, ',') + ")");
    }

    @Test
    public void testTimeToId() {
        LocalDateTime td = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        long nowMaxId = DistributeIdUtils.getMinOrderId(LocalDateTime.now());
        System.out.println("当前id:" + DistributeIdUtils.getMinOrderId(LocalDateTime.now()));
        System.out.println("当天id:" + DistributeIdUtils.getMinOrderId(td));
        System.out.println("最近10天id范围:" + DistributeIdUtils.getMinOrderId(td.minusDays(10)) + ", " + nowMaxId);
        System.out.println("最近30天id范围:" + DistributeIdUtils.getMinOrderId(td.minusDays(30)) + ", " + nowMaxId);
        System.out.println("指定日期id;" + DistributeIdUtils.getMinOrderId(LocalDateTime.parse("20190201_000000", DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))));

        long oid = 253908959101262L;
        System.out.println(DistributeIdUtils.getTime(oid));
        System.out.println(oid % 8L);
    }

    /**
     * 提取文本中的订单号
     *
     * @throws Exception
     */
    @Test
    public void testFindId() throws Exception {
        File file = ResourceUtils.getFile("classpath:input.txt");
        String input = FileUtils.readFileToString(file, "utf-8");
        Pattern pattern = Pattern.compile("\"orderId\":\"(\\d+)\"");
        Matcher m = pattern.matcher(input);
        Set<Long> orders = new HashSet<>(100);
        while (m.find()) {
            orders.add(Long.valueOf(m.group(1)));
        }

        System.out.println(orders.size());
        System.out.println(orders);
    }

    public static Map<Integer, List<Long>> readAndGroupId(String resourceLocation) throws IOException {
        return readIdList(resourceLocation).stream().collect(Collectors.groupingBy(id -> (int) (Long.parseLong(String.valueOf(id)) % 8)));
    }

    public static List<Long> readIdList(String resourceLocation) throws IOException {
        File file = ResourceUtils.getFile(resourceLocation);
        System.out.println(file);
        List<String> list = FileUtils.readLines(file, "utf-8");
        List<Long> ids = new ArrayList<>(list.size());
        for (String id : list) {
            id = id.trim().replaceAll("\\D+", "");
            if(StringUtils.isNotBlank(id)) {
                ids.add(Long.parseLong(id));
            }
        }
        return ids;
    }
}
