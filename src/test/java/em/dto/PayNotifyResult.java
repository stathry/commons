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

    @Excel(name = "paymentNo")
    private String paymentNo;

    @Excel(name = "orderCollectionNo")
    private String orderCollectionNo;

    @Excel(name = "orderId")
    private Long orderId;

    @Excel(name = "payAmount")
    private BigDecimal payAmount;

    @Excel(name = "acutalPayAmount")
    private BigDecimal acutalPayAmount;

    @Excel(name = "payTimeEnd")
    private Date payTimeEnd;

}
