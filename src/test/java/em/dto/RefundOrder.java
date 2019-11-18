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
public class RefundOrder {


    @Excel(name = "orderno")
    private Long orderno;

    @Excel(name = "gsp")
    private String payRefundNo;

    @Excel(name = "gsr")
    private String gsr;

    @Excel(name = "orderrefundno")
    private String orderrefundno;

    @Excel(name = "refundamount")
    private BigDecimal refundamount;

}
