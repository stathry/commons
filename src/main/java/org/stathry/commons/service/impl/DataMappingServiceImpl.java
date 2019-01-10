package org.stathry.commons.service.impl;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.stathry.commons.dao.impl.DataSharingDAO;
import org.stathry.commons.model.dto.DataItem;
import org.stathry.commons.model.dto.DataItemHandlers;
import org.stathry.commons.model.dto.DataMap;
import org.stathry.commons.model.dto.DataMapConfig;
import org.stathry.commons.model.dto.DataRange;
import org.stathry.commons.model.dto.DataSegment;
import org.stathry.commons.service.DataMappingService;
import org.stathry.commons.utils.DataFormatter;
import org.stathry.commons.utils.JAXBUtils;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据映射接口
 * Created by dongdaiming on 2018-12-06 09:51
 */
@Service
public class DataMappingServiceImpl implements DataMappingService, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataMappingServiceImpl.class);
    private DataMapConfig dataMapConfig;
    private static final String CONFIG_FILENAME = "/DataMapConfig.xml";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    private DataSharingDAO dataSharingDAO;

    @Override
    public DataMap dataMap(String dataGroup) {
        DataMap dataMap = dataMapConfig.getDataMapMap().get(dataGroup);
        Assert.notNull(dataMap, "not found dataGroup " + dataGroup);
        Assert.notEmpty(dataMap.getDataSegments(), "dataGroup " + dataGroup + ", dataSegments is empty.");
        return dataMap;
    }

    @Override
    public DataRange<Long> keyRange(NamedParameterJdbcTemplate namedParameterJdbcTemplate, DataMap dataMap) {
        String dataGroup = dataMap.getDataGroup();
        String queryType = dataMap.getQueryType();
        Assert.hasText(queryType, "dataGroup " + dataGroup + ", queryType is empty.");
        queryType = queryType.trim();

        DataRange<Long> keyRange;
        if ("all".equalsIgnoreCase(queryType)) {
            keyRange = dataSharingDAO.queryKeyRange(namedParameterJdbcTemplate, dataMap.getMainTableName(), dataMap.getKeyColumn());
        } else if ("byDate".equalsIgnoreCase(queryType)) {
            String byDateStr = dataMap.getQueryLastDaysOrMonths();
            Assert.hasText(byDateStr, "dataGroup " + dataGroup + ", queryLastDaysOrMonths is empty.");
            byDateStr = byDateStr.trim().toUpperCase();

            int mi = byDateStr.indexOf('M'), di = byDateStr.indexOf('D');
            Date now = new Date(), beginDate, endDate;

            if (mi != -1) {
                int mons = Integer.parseInt(byDateStr.substring(0, mi));
                now = DateUtils.truncate(now, Calendar.MONTH);
                beginDate = DateUtils.addMonths(now, mons * -1);
                endDate = DateUtils.addMonths(now, (mons - 1) * -1);
            } else if (di != -1) {
                int days = Integer.parseInt(byDateStr.substring(0, di));
                now = DateUtils.truncate(now, Calendar.DAY_OF_MONTH);
                beginDate = DateUtils.addDays(now, days * -1);
                endDate = DateUtils.addDays(now, (days - 1) * -1);
            } else {
                throw new UnsupportedOperationException("dataGroup " + dataGroup + ", queryLastDaysOrMonths " + byDateStr);
            }
            keyRange = keyRange(namedParameterJdbcTemplate, dataMap.getMainTableName(), dataMap.getKeyColumn(), dataMap.getDateColumn(), DateFormatUtils.format(beginDate, DATETIME_FORMAT), DateFormatUtils.format(endDate, DATETIME_FORMAT));
        } else {
            throw new UnsupportedOperationException("dataGroup " + dataGroup + ", queryType " + queryType);
        }

        Assert.notNull(keyRange, "keyRange is null, tableName " + dataMap.getMainTableName());
        LOGGER.info("dataGroup {}, tableName {}, keyRange:{} - {}.", dataGroup, dataMap.getMainTableName(), keyRange.getMin(), keyRange.getMax());
        return keyRange;
    }

    @Override
    public DataRange<Long> keyRange(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String tableName, String keyColumn) {
        DataRange<Long> keyRange = dataSharingDAO.queryKeyRange(namedParameterJdbcTemplate, tableName, keyColumn);
        Assert.notNull(keyRange, "keyRange is null, tableName " + tableName);
        return keyRange;
    }

    @Override
    public DataRange<Long> keyRange(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String tableName, String keyColumn, String dateColumn, String beginDate, String endDate) {
        DataRange<Long> keyRange = dataSharingDAO.queryKeyRange(namedParameterJdbcTemplate, tableName, keyColumn, dateColumn, beginDate, endDate);
        Assert.notNull(keyRange, "keyRange is null, tableName " + tableName);
        return keyRange;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        init();
    }

    private void init() {
        InputStream in = DataMappingServiceImpl.class.getResourceAsStream(CONFIG_FILENAME);
        dataMapConfig = JAXBUtils.xmlFileToBean(DataMapConfig.class, in);
        IOUtils.closeQuietly(in);

        Assert.notNull(dataMapConfig, "dataMapConfig is null.");
        Assert.notEmpty(dataMapConfig.getDataMaps(),"dataMaps is empty.");

        sortMapConfigItems(dataMapConfig);
        LOGGER.info("load dataMapConfig success, size {}.", dataMapConfig.getDataMapMap().size());
    }

    private void sortMapConfigItems(DataMapConfig conf) {
        Map<String, DataMap> dataMapMap = new HashMap<>(conf.getDataMaps().size());
        DataItemComparator dataItemComparator = new DataItemComparator();
        DataSegmentComparator dataSegmentComparator = new DataSegmentComparator();
        for (DataMap dataMap : conf.getDataMaps()) {
            Assert.notEmpty(dataMap.getDataSegments(), "dataSegments is empty, dataGroup " + dataMap.getDataGroup());

            for (DataSegment segment : dataMap.getDataSegments()) {
                Assert.notEmpty(segment.getDataItems(), "dataItems is empty, dataGroup " + dataMap.getDataGroup() + ", segment " + segment.getNo());
                Collections.sort(segment.getDataItems(), dataItemComparator);

                buildItemMap(segment);
            }

            Collections.sort(dataMap.getDataSegments(), dataSegmentComparator);

            dataMapMap.put(dataMap.getDataGroup(), dataMap);
        }
        conf.setDataMapMap(dataMapMap);
    }

    private void buildItemMap(DataSegment segment) {
        StringBuilder selectColumns = new StringBuilder(segment.getKeyColumn()).append(',');
        Map<String, DataFormatter.DataFormat> formatMap = new HashMap<>();
        DataFormatter.DataFormat format;
        for (DataItem item : segment.getDataItems()) {
            if(!item.getColumn().equalsIgnoreCase("FIX_VALUE")) {
                selectColumns.append(item.getColumn()).append(',');
                switch (item.getType()) {
                    case DataFormatter.TYPE_STR : format = null; break;
                    case DataFormatter.TYPE_INT : format = item.getMultiply() == null ? null : new DataFormatter.DataFormat(item.getMultiply()); break;
                    case DataFormatter.TYPE_FLOAT :
                        format = new DataFormatter.DataFormat();
                        format.setScale(item.getFloatScale() == null ? -1 : item.getFloatScale());
                        format.setMultiply(item.getMultiply() == null ? 1.0 : item.getMultiply());break;
                    case DataFormatter.TYPE_DATE : format = item.getFormat() == null ? null : new DataFormatter.DataFormat(item.getSrcFormat(), item.getFormat()); break;
                    default: format = null;
                }
                formatMap.put(item.getColumn(), format);

                if(StringUtils.isNotBlank(item.getMapping())) {
                    item.setValueMap(JSON.parseObject(item.getMapping(), Map.class));

                }
                if("mapValue".equalsIgnoreCase(item.getHandler())) {
                    item.setDataItemHandler(new DataItemHandlers.DataItemMappingHandler());
                } else if("overdueStatus".equalsIgnoreCase(item.getHandler())) {
                    item.setDataItemHandler(new DataItemHandlers.SYOverdueStatusHandler());
                }
            }
        }
        selectColumns.deleteCharAt(selectColumns.length() - 1);
        segment.setSelectColumns(selectColumns.toString());
        segment.setFormatMap(formatMap);
    }

    private static class DataItemComparator implements Comparator<DataItem> {
        @Override
        public int compare(DataItem o1, DataItem o2) {
            return o1.getNo().compareTo(o2.getNo());
        }
    }

    private static class DataSegmentComparator implements Comparator<DataSegment> {
        @Override
        public int compare(DataSegment o1, DataSegment o2) {
            return o1.getNo().compareTo(o2.getNo());
        }
    }

}
