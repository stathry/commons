package em;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

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
    public void testBuildSelfDunUrl() throws IOException {
        String urlPattern = "curl -X POST http://localhost:8120/page/selfCheck/dun?orderId=";

        List<Long> list = IdTest.readIdList("classpath:ids.txt");
        list.forEach(oid -> System.out.println(urlPattern + oid.toString()));
    }

    @Test
    public void testBuildGetUrl() throws IOException {
        String getUrlPattern = "curl -X POST http://localhost:8120/maintain/changeStatusToCheck/orderId";

        List<Long> list = IdTest.readIdList("classpath:ids.txt");
        list.forEach(oid -> System.out.println(getUrlPattern.replaceFirst("orderId", oid.toString())));
    }

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
    public void testGenRefundAuditNotify() throws Exception {
        List<Long> list = IdTest.readIdList("classpath:ids.txt");
        String curl = "curl -X POST \\\n" +
                "  http://localhost:8120/refund/audit/notify \\\n" +
                "  -H 'Content-Type: application/json' \\\n" +
                "  -H 'Postman-Token: 01951b42-8fd8-467b-a56d-b7fceca5efe6' \\\n" +
                "  -H 'cache-control: no-cache' \\\n" +
                "  -d ";
        String body = "{\n" +
                "\t\"orderId\":250592124731601,\n" +
                "\t\"pass\":true\n" +
                "}";

        StringBuilder builder = new StringBuilder();
        for (Long e : list) {
            builder.append(curl).append("'").append(body.replaceAll("250592124731601", e.toString())).append("'").append("\r\n\r\n");
        }

        File output = new File(System.getProperty("user.dir") + "/src/test/resources/" + OUTPUT_FILENAME);
        FileUtils.writeStringToFile(output, builder.toString(), Charset.defaultCharset(), false);
        System.out.println(FileUtils.readFileToString(output, Charset.defaultCharset()));
    }

}
