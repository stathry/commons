package em;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
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
        String sql = "SELECT ref.order_id, off.offset_order_id, ref.amount_refunded, ord.amount_paid from ord_n.t_ord_refund ref left join ord_n.t_ord_order_offset off on ref.order_id = off.order_id left join ord_n.t_ord_order ord on off.offset_order_id = ord.id left join ord_n.t_ord_order_flow flow on ref.order_id = flow.order_id where ref.order_id in ";
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
        File output = new File(System.getProperty("user.dir") + "/src/teststandard/resources/" + "output.txt");
        FileUtils.writeStringToFile(output, builder.toString(), Charset.defaultCharset(), false);
    }

    @Test
    public void testBuildSQL8() {
        String sql = "select distinct order_id from ord_n.t_ord_order_flow flow join ord_n.t_ord_order ord on flow.order_id = ord.id where flow.gmt_create >= '2019-05-01 00:00:00' and flow.gmt_create < '2019-06-01 00:00:00' and flow.next_status in (6, 8) and flow.pre_status != flow.next_status and ord.order_type = 5 ";
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



}
