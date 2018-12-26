package org.stathry.commons.model.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stathry.commons.utils.DataFormatter;

import java.util.List;
import java.util.Map;

/**
 * TODO
 * Created by dongdaiming on 2018-12-21 09:19
 */
public class DataItemResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataItemHandlers.class);

    public static JSONArray resolveToJSONArray(DataSegment segment, List<Map<String, Object>> segmentDataList, DataFormatter formatter) {
        JSONArray array = new JSONArray(segmentDataList.size());
        String key = "";
        List<DataItem> items = segment.getDataItems();
        Map<String, DataFormatter.DataFormat> formatMap = segment.getFormatMap();

        try {
            for (Map<String, Object> data : segmentDataList) {
                key = data.get(segment.getKeyColumn()).toString();

                array.add(resolveToJSON(items, data, formatMap, formatter));
            }
        } catch (Exception e) {
            LOGGER.error("generate data error, dataGroup {}, cause {}.", key, e.getMessage());
            LOGGER.error("generate data error.", e);
        }
        return array;
    }

    public static JSONObject resolveToJSON(List<DataItem> items, Map<String, Object> data, Map<String, DataFormatter.DataFormat> formatMap, DataFormatter formatter) {
        JSONObject json;
        String column;
        String value;
        Object objValue;
        json = new JSONObject(items.size() * 2);
        for (DataItem item : items) {
            column = item.getColumn();
            if ("FIX_VALUE".equals(column.toUpperCase())) {
                value = item.getValue();
                json.put(item.getField(), castDataType(value, item.getType()));
            } else {
                objValue = data.get(column);
                objValue = item.getDataItemHandler().handle(item, objValue);
                value = formatter.format(objValue, formatMap.get(column), item.getType());
                objValue = castDataType(value, item.getType());
                json.put(item.getField(), objValue);
                if(item.isOptional() && (StringUtils.isBlank(value) || objValue == null)) {
//                            LOGGER.warn("optional field {} is null or empty.", item.getField());
                }
            }
        }
        return json;
    }

    public static Object castDataType(String value, int type) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        Object obj;
        switch (type) {
            case DataFormatter.TYPE_STR:
                obj = value;
                break;
            case DataFormatter.TYPE_INT:
                obj = Integer.valueOf(value);
                break;
            case DataFormatter.TYPE_FLOAT:
                obj = Double.valueOf(value);
                break;
            case DataFormatter.TYPE_BOOL:
                obj = Boolean.valueOf(value);
                break;
            default:
                obj = value;
        }
        return obj;
    }

}
