package em.dto;


import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

/**
 * PayNotifyResult
 *
 * @author dongdaiming(董代明)
 * @date 2019-04-22 12:14
 */
@Data
public class RefundNotifyResult {

    @Excel(name = "payRefundNo")
    private String payRefundNo;

    @Excel(name = "orderId")
    private Long orderId;

    @Excel(name = "amountRefundable")
    private BigDecimal amountRefundable;

    @Excel(name = "amountRefunded")
    private BigDecimal amountRefunded;

    @Excel(name = "refundNo")
    private String refundNo = "-";

}
