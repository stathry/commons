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
public class KFOfflineRefund {

    @Excel(name = "orderId")
    private Long orderId;

    @Excel(name = "userId")
    private Long userId;

    @Excel(name = "orderType")
    private Integer orderType;

    @Excel(name = "preStatus")
    private Integer preStatus;

    @Excel(name = "status")
    private Integer status;

    @Excel(name = "amountPaid")
    private BigDecimal amountPaid;

    @Excel(name = "userPhone")
    private String userPhone;

    @Excel(name = "gmtModify")
    private Date gmtModify;

    @Excel(name = "flowType")
    private Integer flowType;
    private String mac;
    private String chargeBatterySn;
    private Long shopId;
    private String shopVersionId;
    private String deviceType;
    private String deviceModel;
    private Integer depositPay;
    private Integer channelType;
    private Integer isCreditBorrow;
    private Integer creditProductType;

}
