package em.order.count;

// import com.alibaba.excel.EasyExcel;
/**
 * CurDayToPayTest
 *
 * @author dongdaiming(董代明)
 * @date 2019-09-07
 */
public class CurDayToPayTest {
/*
    private static final BigDecimal MAX_FEE = new BigDecimal("99");
    private static final LocalDateTime GMT_USE_END = LocalDate.of(2019, 9, 12).atStartOfDay();

    @Test
    public void testParseStrategyAndCalc() throws Exception {
        Map<Long, StrategyDetailDTO> strategyMap = new HashMap<>(100_0000);

        EasyExcel.read("/TS/数据统计/待收款订单明细/911/stg.xlsx",
                StrategyDetailDTO.class, new ReadListener.StrategyDetailListener(strategyMap)).sheet().doRead();

        List<OrderInfo> list = new ArrayList<>(100_0000);
        EasyExcel.read("/TS/数据统计/待收款订单明细/911/detail.xlsx",
                OrderInfo.class, new ReadListener.OrderInfoListener(list)).sheet().doRead();

        StrategyDetailDTO strategy;
        for (OrderInfo info : list) {
            if (info.getStatus().intValue() == 3) {
                strategy = strategyMap.get(info.getStrategyId());
                if (strategy == null) {
                    continue;
                }
                Integer useMins = calculateUseSpan(info.getGmtUseStart(), strategy);
                info.setAmountPayable(calculate(strategy, useMins));
            }
        }
        EasyExcel.write("/TS/数据统计/待收款订单明细/911/911-result.xlsx", OrderInfo.class).sheet("111").doWrite(list);

    }

    @Test
    public void testReadToPayOrderStrategy() throws Exception {
        Set<Long> strategies = new HashSet<>(100_0000);

        EasyExcel.read("/TS/数据统计/当日未收款订单明细/902-906-detailtime.xlsx", OrderInfo.class, new ReadListener.OrderStrategyListener(strategies)).sheet().doRead();

        FileUtils.writeLines(new File("/TS/数据统计/当日未收款订单明细/902-906-strategy2.txt"), strategies, false);
    }

    public Integer calculateUseSpan(Date gmtUseStart0, StrategyDetailDTO strategyDetailDTO) {
        LocalDateTime gmtUseStart = Instant.ofEpochMilli(gmtUseStart0.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();


        //X小时=Y小时，当充电时长每满足X小时时取Y小时进行收费
        int x = 24;
        int y = strategyDetailDTO.getExpandY();

        //不足一分钟算一分钟,如果出现负数，直接按0计算
        long seconds = Duration.between(gmtUseStart, GMT_USE_END).getSeconds();
        if (seconds <= 0) {
            seconds = 0L;
        }

        Long betweenMinutes = (seconds / 60 + (seconds % 60 == 0 ? 0 : 1));

        //不足一小时情况
        if (betweenMinutes / 60 == 0) {
            Long betweenSeconds = betweenMinutes * 60;

            //转换为秒
            return betweenSeconds.intValue();
        }
        //超过一个小时
        else {
            Long betweenHours = betweenMinutes / 60;

            //通过小时数计算，每x(24)小时算y小时，如果余下的小时数大于y小时则统一为y小时
            Long useSpan = ((betweenHours / x * y) + ((betweenHours % x) >= y ? y : betweenHours % x)) * 60;

            //小于10小时则计算分钟
            if (betweenHours % x < y) {
                useSpan = useSpan + (betweenMinutes % 60) * 60;
            }

            //转换为秒
            return useSpan.intValue();
        }
    }

    public BigDecimal calculate(StrategyDetailDTO strategy, Integer useTime) {

        //使用时长为空的时候默认价格都为0
        if (useTime == null) {

            return BigDecimal.ZERO;
        }

        //计算数量(统一根据规格单位换算为分钟，计算数量)
        Integer specificationMinutes = strategy.getSpecification().multiply(new BigDecimal("60")).intValue();
        Integer count = (useTime / (specificationMinutes)) + (useTime % specificationMinutes == 0 ? 0 : 1);

        //计算免费属性
        if (strategy.getFreeTime() != null && useTime.compareTo(strategy.getFreeTime()) <= 0) {
            return BigDecimal.ZERO;
        }

        //计算实际价格
        BigDecimal price = strategy.getPrice();
        BigDecimal totalPrice = price.multiply(new BigDecimal(count));

        totalPrice = totalPrice.compareTo(MAX_FEE) > 0 ? MAX_FEE : totalPrice;
        return totalPrice;
    }*/

}
