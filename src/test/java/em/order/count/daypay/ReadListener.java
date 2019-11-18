package em.order.count.daypay;

// import com.alibaba.excel.context.AnalysisContext;
// import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;

/**
 * ReadListener
 *
 * @author dongdaiming(董代明)
 * @date 2019-09-07
 */
@Slf4j
public class ReadListener {
/*
    public static class DetailOrderListener extends AnalysisEventListener<OrderDetail> {

        Map<Long, OrderInfo> infoMap;

        public DetailOrderListener(Map<Long, OrderInfo> infoMap) {
            this.infoMap = infoMap;
        }

        @Override
        public void invoke(OrderDetail detail, AnalysisContext analysisContext) {
            OrderInfo info = infoMap.get(detail.getOrderId());
            if (info == null) {
                info = new OrderInfo();
                info.setId(detail.getOrderId());
                infoMap.put(detail.getOrderId(), info);
            }
            info.setAmountPayable(detail.getAmount());
            info.setChargeBatterySn(detail.getBatterySn());
            info.setMac(detail.getMac());
            info.setStrategyId(detail.getStrategyId());
            info.setGmtUseStart(detail.getUseTime0());

            printRowIndex(analysisContext);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            log.info("detailOrder doAfterAllAnalysed");
        }
    }

    public static class OrderStrategyListener extends AnalysisEventListener<OrderInfo> {

        private Set<Long> strategies;
        private static final BigDecimal noAmt = new BigDecimal("6666");

        public OrderStrategyListener(Set<Long> strategies) {
            this.strategies = strategies;
        }

        @Override
        public void invoke(OrderInfo detail, AnalysisContext analysisContext) {
            if (detail.getAmountPayable().compareTo(noAmt) == 0) {
                strategies.add(detail.getStrategyId());
            }

            printRowIndex(analysisContext);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            log.info("InfoOrder doAfterAllAnalysed");
        }
    }

    public static class StrategyDetailListener extends AnalysisEventListener<StrategyDetailDTO> {

        Map<Long, StrategyDetailDTO> strategyMap;

        public StrategyDetailListener(Map<Long, StrategyDetailDTO> strategyMap) {
            this.strategyMap = strategyMap;
        }

        @Override
        public void invoke(StrategyDetailDTO detail, AnalysisContext analysisContext) {
            detail.setExpandY(detail.getExpandY() == null ? 10 : detail.getExpandY());
            detail.setFreeTime(detail.getFreeTime() == null ? 0: detail.getFreeTime());
            detail.setSpecification(detail.getSpecification() == null ? BigDecimal.ONE : detail.getSpecification());
            strategyMap.putIfAbsent(detail.getStrategyId(), detail);

            printRowIndex(analysisContext);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            log.info("StrategyDetail doAfterAllAnalysed");
        }
    }



    public static class OrderInfoListener extends AnalysisEventListener<OrderInfo> {

        private List<OrderInfo> list;
        private static final BigDecimal noAmt = new BigDecimal("6666");

        public OrderInfoListener(List<OrderInfo> list) {
            this.list = list;
        }

        @Override
        public void invoke(OrderInfo detail, AnalysisContext analysisContext) {
            list.add(detail);

            printRowIndex(analysisContext);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            log.info("OrderInfoListener doAfterAllAnalysed");
        }
    }

    private static void printRowIndex(AnalysisContext analysisContext) {
        int rowIndex = analysisContext.readRowHolder().getRowIndex();
        if (rowIndex % 1000 == 0) {
            log.info("rowIndex: {}", rowIndex);
        }
    }*/
}
