package em.dto;

import lombok.Data;

/**
 * TODO
 *
 * @author dongdaiming(董代明)
 * @date 2019-10-31
 */
@Data
public class CollRefundCount {

    private Long orderId;
    private Integer collCount;
    private Integer refCount;
}
