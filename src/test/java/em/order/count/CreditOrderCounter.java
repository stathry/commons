package em.order.count;


import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * CreditOrderCounter
 *
 * @author dongdaiming(董代明)
 * @date 2019-04-22 12:14
 */
@Data
public class CreditOrderCounter {

    @Excel(name="p1")
    private Integer p1;

    @Excel(name="d1", format = "yyyy-MM-dd")
    private String d1;

    @Excel(name="o1")
    private Integer o1;

    @Excel(name="p2")
    private Integer p2;

    @Excel(name="d2")
    private String d2;

    @Excel(name="o2")
    private Integer o2;

    @Excel(name="p3")
    private Integer p3;

    @Excel(name="d3")
    private String d3;

    @Excel(name="o3")
    private Integer o3;

}
