package em.order.count.daypay;

// import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 策略详情
 *
 * @Author Wang Lin(王霖)
 * @Date 2017/9/5
 * @Time 21:47
 */
@Data
public class StrategyDetailDTO {
  /*  @ExcelProperty("strategyId")
    private Long strategyId;

    *//**
     * X小时=Y小时，当充电时长每满足X小时时取Y小时进行收费
     * 每周期封顶小时数
     *//*
    @ExcelProperty("expandY")
    private Integer expandY;

    *//**
     * 实际零售价
     *//*
    @ExcelProperty("price")
    private BigDecimal price;

    *//**
     * 规格
     *//*
    @ExcelProperty("specification")
    private BigDecimal specification;
    @ExcelProperty("freeTime")
    private Integer freeTime;*/


}
