package em.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * PayNotifyResult
 *
 * @author dongdaiming(董代明)
 * @date 2019-04-22 12:14
 */
@Data
public class PayNotifyData {

    private String paymentNo;

    private String orderCollectionNo;

    private Long orderId;

    private BigDecimal payAmount;

    private BigDecimal acutalPayAmount;

    private Date payTimeEnd;

}
