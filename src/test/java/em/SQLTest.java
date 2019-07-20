package em;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * SQLTest
 *
 * @author dongdaiming(董代明)
 * @date 2019-04-25 08:44
 */
public class SQLTest {

    @Test
    public void testBuildSQL8IdInList() throws Exception {
        Map<Integer, List<Long>> idGroupMap = IdTest.readAndGroupId("classpath:ids.txt");
        System.out.println(JSON.toJSONString(idGroupMap));
        String sql = "select * from ord_n.t_ord_collection where status = 1 and order_id in ";
        sql = sql.trim().endsWith("in") ? sql : sql + " in " ;
        sql = sql + " ";
        if(!sql.contains("ord_n")) {
            throw new IllegalArgumentException(sql);
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            if (CollectionUtils.isNotEmpty(idGroupMap.get(i))) {
                if (i != 0) {
                    builder.append("\n").append("union all ");
                }
                builder.append(sql.replaceAll("ord_n", "ord_" + i));
                builder.append('(').append(StringUtils.join(idGroupMap.get(i), ',')).append(')');
            }
        }

        System.out.println(builder.toString());
        File output = new File(System.getProperty("user.dir") + "/src/testframework/resources/" + "output.txt");
        FileUtils.writeStringToFile(output, builder.toString(), Charset.defaultCharset(), false);
    }

    @Test
    public void testBuildSQL8() {
        String sql = "select * from ord_n.t_ord_collection where order_id = 336252768616721";
        sql = sql + " ";
        if(!sql.contains("ord_n")) {
            throw new IllegalArgumentException(sql);
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            if (i != 0) {
                builder.append("\n").append("union all ");
            }
            builder.append(sql.replaceAll("ord_n", "ord_" + i));
        }

        System.out.println(builder.toString());
    }

    @Test
    public void testBuildSQL8PhoneInList() throws IOException {
        List<Long> list = IdTest.readIdList("classpath:ids.txt");
        String phones = "(" + StringUtils.join(list, ",") + ")";
        File file = new File("/temp/sql8.sql");
        System.out.println(phones);
        FileUtils.writeStringToFile(file, phones + "\n", "utf-8", false);
        String sql = "SELECT od.user_phone, od.amount_paid from ord_n.t_ord_order od where od.id >= 319263709593600 and od.id < 333759224217600 and od.order_type = 5 and od.status = 8 and od.user_phone in ";
        sql = sql.trim().endsWith("in") ? sql : sql + " in " ;
        if(!sql.contains("ord_n")) {
            throw new IllegalArgumentException(sql);
        }

        for (int i = 0; i < 8; i++) {
            StringBuilder builder = new StringBuilder();
            if (i != 0) {
                builder.append("union all ");
            }
            builder.append(sql.replaceAll("ord_n", "ord_" + i));
            builder.append(phones);
            System.out.println(builder.toString());
            FileUtils.writeStringToFile(file, "\n" + builder.toString(), "utf-8", true);
        }

    }

}
