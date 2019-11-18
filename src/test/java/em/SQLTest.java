package em;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
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
    public void test() {
        System.out.println(LocalDate.now().minusDays(5));
        System.out.println(LocalDate.now().minusDays(10));
    }

    @Test
    public void testBuildSQL8IdInList() throws Exception {
        Map<Integer, List<Long>> idGroupMap = IdTest.readAndGroupId("classpath:ids.txt");
        String sql = "select * from ord_0.t_ord_order where id in   ";
        sql = sql.trim().toLowerCase().endsWith("in") ? sql : sql + " in " ;
        sql = sql + " ";
        checkSql(sql);
        boolean unionSql = unionSql(sql);
        // boolean unionSql = false;

        StringBuilder builder = new StringBuilder(1024 * 1024 * 1024);
        for (int i = 0; i < 8; i++) {
            if (CollectionUtils.isNotEmpty(idGroupMap.get(i))) {
                if (i != 0) {
                    builder.append("\n").append(unionSql ? "union all " : ";");
                }
                builder.append(sql.replaceAll("ord_n", "ord_" + i));
                builder.append('(').append(StringUtils.join(idGroupMap.get(i), ',')).append(')');
            }
        }

        String sqls = builder.toString();
        int iSep = sqls.indexOf(";");
        sqls = (iSep < 5 && iSep > 0) ? sqls.substring(iSep+1) : sqls;
        System.out.println(sqls);
        File output = new File(System.getProperty("user.dir") + "/src/test/resources/" + "output.txt");
        System.out.println(output.getAbsolutePath());
        FileUtils.writeStringToFile(output, sqls, Charset.defaultCharset(), false);
    }



    @Test
    public void testBuildSQL8() {
        String sql = "ALTER  TABLE ord_n.t_ord_order RENAME TO ord_n.t_ord_order_bak_191018  ";
        sql = sql + " ";
        checkSql(sql);
        // boolean unionSql = unionSql(sql);
        boolean unionSql = false;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            if (i != 0) {
                builder.append("\n").append(unionSql ? "union all " : ";");
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
        checkSql(sql);
        boolean unionSql = unionSql(sql);

        for (int i = 0; i < 8; i++) {
            StringBuilder builder = new StringBuilder();
            if (i != 0) {
                builder.append(unionSql ? "union all " : ";");
            }
            builder.append(sql.replaceAll("ord_n", "ord_" + i));
            builder.append(phones);
            System.out.println(builder.toString());
            FileUtils.writeStringToFile(file, "\n" + builder.toString(), "utf-8", true);
        }
    }

    @Test
    public void testBuildSQL8InStrList() throws IOException {
        List<String> list = IdTest.readStringList("classpath:input.txt");
        String strs = "('" + StringUtils.join(list, "','") + "')";
        File file = new File("/temp/sql8.sql");
        System.out.println(strs);
        FileUtils.writeStringToFile(file, strs + "\n", "utf-8", false);
        String sql = "SELECT charge_battery_sn, id, gmt_create from ord_n.t_ord_order where id > 317451770265600 and charge_battery_sn in ";
        sql = sql.trim().endsWith("in") ? sql : sql + " in " ;
        checkSql(sql);
        boolean unionSql = unionSql(sql);

        for (int i = 0; i < 8; i++) {
            StringBuilder builder = new StringBuilder();
            if (i != 0) {
                builder.append(unionSql ? "union all " : ";");
            }
            builder.append(sql.replaceAll("ord_n", "ord_" + i));
            builder.append(strs);
            System.out.println(builder.toString());
            FileUtils.writeStringToFile(file, "\n" + builder.toString(), "utf-8", true);
        }
    }

    private void checkSql(String sql) {
        if (!sql.contains("ord_n") || sql.contains("ord_0")) {
            throw new IllegalArgumentException(sql);
        }
        if(sql.contains("join") && !sql.contains("on")) {
            throw new IllegalArgumentException(sql);
        }
    }

    private boolean unionSql(String sql) {
        String curSql = sql.trim().toLowerCase();
        if(curSql.startsWith("delete") || curSql.startsWith("truncate") || !curSql.startsWith("select")) {
            return false;
        }
        return true;
    }

}
