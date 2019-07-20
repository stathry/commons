package em.dto;


import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * PayNotifyResult
 *
 * @author dongdaiming(董代明)
 * @date 2019-04-22 12:14
 */
@Data
public class PayNotifyResult {

    @Excel(name = "payment_no")
    private String payment_no;

    @Excel(name = "order_collection_no")
    private String order_collection_no;

    @Excel(name = "order_no")
    private Long order_no;

    @Excel(name = "pay_amount")
    private BigDecimal pay_amount;

    @Excel(name = "gmt_modify")
    private Date gmt_modify;

}
