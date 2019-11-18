package em.order.count.daypay;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * TODO
 *
 * @author dongdaiming(董代明)
 * @date 2019-09-07
 */
@Data
public class OrderDetail {

    private Long orderId;
    private Date useTime0;
    private String batterySn;
    private String mac;
    private BigDecimal amount;
    private Long strategyId;
}
