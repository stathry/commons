package em.order.count.daypay;

import lombok.Data;

import java.util.Date;

/**
 * TODO
 *
 * @author dongdaiming(董代明)
 * @date 2019-09-07
 */
@Data
public class TimeOrder {


    private Long orderId;
    private Date useTime;
    private Date toPayTime;
    private Date finTime;
}
