package org.stathry.commons.model.dto;

/**
 * DataItemHandlers
 * Created by dongdaiming on 2018-12-18 17:18
 */
public class DataItemHandlers {

    public static class DefaultDataItemHandler implements DataItemHandler {

        @Override
        public Object handle(DataItem item, Object value) {
            return value;
        }

    }

    public static class DataItemMappingHandler implements DataItemHandler {

        @Override
        public Object handle(DataItem item, Object value) {
            return value == null ? null : item.getValueMap().get(value);
        }

    }

    public static class SYOverdueStatusHandler implements DataItemHandler {

        @Override
        public Object handle(DataItem item, Object value) {
            if(value == null) {
                return null;
            }
            int d = ((Number)value).intValue();
            String v;
            if(d <= 30) {
                v = "M1";
            } else if(d <= 60) {
                v = "M2";
            } else if(d <= 90) {
                v = "M3";
            } else if(d <= 120) {
                v = "M4";
            }else if(d <= 150) {
                v = "M5";
            }else if(d <= 180) {
                v = "M6";
            } else {
                v= "MN";
            }
            return v;
        }

    }
}
