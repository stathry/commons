package em.dto;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 支付结果通知接口
 *
 * @Author Wang Lin(王霖)
 * @Date 2017/9/2
 * @Time 10:51
 */
@Data
public class OrderPayResultDTO implements Serializable {


    private static final long serialVersionUID = -2815124692997361516L;
    /**
     * 订单id
     */
    @Excel(name = "orderId")
    private Long orderId;

    /**
     * 订单收款单号
     */
    @Excel(name = "orderCollectionNo")
    private String orderCollectionNo;

    /**
     * 支付单号
     */
    @Excel(name = "paymentNo")
    private String paymentNo;

    /**
     * 支付金额
     */
    @Excel(name = "payAmount")
    private Double payAmount;

    /**
     * 实际支付金额
     */
    @Excel(name = "acutalPayAmount")
    private Double acutalPayAmount;

    /**
     * 支付完成时间
     */
    @Excel(name = "payTimeEnd", importFormat = "yyyy/MM/dd HH:mm:ss")
    private Date payTimeEnd;

}
