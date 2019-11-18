package em.dto;

import lombok.Data;

import java.util.List;

/**
 * OrderBasic
 *
 * @author dongdaiming(董代明)
 * @date 2019-10-30
 */
@Data
public class OrderBasic implements Comparable<OrderBasic>{

    private Long orderId;

    private Integer orderType;

    private Integer status;

    private List<OrderPayResultDTO> payList;

    @Override
    public int compareTo(OrderBasic o) {
        return (this.getPayList().size()  - o.getPayList().size()) * 10000_0000 + this.getOrderId().compareTo(o.getOrderId());
    }
}
