package em.dto;


import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelIgnore;

import java.util.Date;

/**
 * PayNotifyResult
 *
 * @author dongdaiming(董代明)
 * @date 2019-04-22 12:14
 */
@Data
public class Refund {

    @Excel(name = "orderId")
    private Long orderId;

    @Excel(name = "refundNo")
    private String refundNo;


    @Excel(name = "collectionNo")
    private String collectionNo;

    @Excel(name = "payRefundNo")
    private String payRefundNo;

    @ExcelIgnore
    private Double amountRefundable;

    @Excel(name = "amountRefunded")
    private Double amountRefunded;

    @Excel(name = "paySubChannelType")
    private Integer paySubChannelType;

    @Excel(name = "gmtPayFeedBack", importFormat = "yyyy-MM-dd HH:mm")
    private Date gmtPayFeedBack;

    @ExcelIgnore
    private Integer status = 1;



}
