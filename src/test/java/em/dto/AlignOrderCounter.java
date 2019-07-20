package em.dto;


import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

/**
 * AlignOrderCounter
 *
 * @author dongdaiming(董代明)
 * @date 2019-04-22 12:14
 */
@Data
@ExcelTarget("AlignOrderCounter")
public class AlignOrderCounter {

    @Excel(name = "date", type = 1, orderNum = "1")
    private String date;

    @Excel(name = "支付分订单数", type = 10, orderNum = "2")
    private Integer o13;

    @Excel(name = "芝麻信用", type = 10, orderNum = "3")
    private Integer o11;

    @Excel(name = "支付分一天内支付完成数", type = 10, orderNum = "4")
    private Integer o23;

    @Excel(name = "芝麻信用一天内支付完成数", type = 10, orderNum = "5")
    private Integer o21;

    @Excel(name = "支付分10分钟内支付", type = 10, orderNum = "6")
    private Integer o33;

    @Excel(name = "芝麻信用10分钟内支付", type = 10, orderNum = "7")
    private Integer o31;

}
