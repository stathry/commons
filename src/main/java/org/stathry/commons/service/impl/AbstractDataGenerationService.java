package org.stathry.commons.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.stathry.commons.dao.impl.DataShareDocDAO;
import org.stathry.commons.dao.impl.DataSharingDAO;
import org.stathry.commons.model.DataShareDoc;
import org.stathry.commons.model.dto.DataItem;
import org.stathry.commons.model.dto.DataMap;
import org.stathry.commons.model.dto.DataRange;
import org.stathry.commons.model.dto.DataSegment;
import org.stathry.commons.service.DataGenerationService;
import org.stathry.commons.service.DataMappingService;
import org.stathry.commons.utils.DataFormatter;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 通用数据生成接口骨架
 * Created by dongdaiming on 2018-12-04 18:11
 */
public abstract class AbstractDataGenerationService implements DataGenerationService,InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDataGenerationService.class);

    @Autowired
    DataSharingDAO dataSharingDAO;

    @Autowired
    DataShareDocDAO dataShareDocDAO;

    @Autowired
    DataMappingService dataMappingService;

    /** 数据文件路径 */
    @Value("${sharing.file.basePath}")
    String baseFilePath;
    @Value("${sharing.file.baseName}")
    String baseFileName;

    String dataGroup;
    DataMap dataMap;

    @Override
    public void afterPropertiesSet() {
        Assert.hasText(dataGroup, "dataGroup is empty.");
        dataMap = dataMappingService.dataMap(dataGroup);
        Assert.notNull(dataMap, "not found dataMap, dataGroup " + dataGroup);
    }

    @Override
    public void generateData() {
        mainGenerateDataFile();
    }

    protected abstract RowDataWrapper newDataWrapper(DataMap dataMap);


    protected DataFormatter newDataFormatter(DataMap dataMap) {
        return new DataFormatter(dataMap.getFloatScale(), RoundingMode.HALF_UP, dataMap.getDatePattern());
    }

    protected void mainGenerateDataFile() {
        DataMap dataMap0 = dataMap;
        DataRange<Long> keyRange = dataMappingService.keyRange(null, dataMap);
        RowDataWrapper dataWrapper = newDataWrapper(dataMap);
        DataFormatter formatter = newDataFormatter(dataMap);

        Long min = keyRange.getMin(), max = keyRange.getMax(), pageSie = dataMap0.getPageSize().longValue();
        long id1 = min;
        long id2 = min + dataMap0.getPageSize() - 1;
        id2 = id2 > max ? max : id2;
        int fileRecords = 0;
        int curRecords;
        boolean isLastBatchOfFile;

        List<List<Map<String, Object>>> segmentsDataList = new ArrayList<>(dataMap0.getDataSegments().size());
        List<Map<String, Object>> firstSegmentDataList;
        List<Map<String, Object>> segmentDataList;
        File file = createNewDataFile(dataMap0);
        boolean isJson = dataMap.getFileType().equalsIgnoreCase("json") || dataMap.getFileType().equalsIgnoreCase("jsonStr");

        for (; id1 <= max; ) {
            for (DataSegment segment : dataMap0.getDataSegments()) {
                segmentDataList = dataSharingDAO.dynamicQueryDataList(null, segment.getSelectColumns(), segment.getTableName(), dataMap0.getKeyColumn(), id1, id2);
                LOGGER.info("queryDataList, dataGroup {}, tableName {}, id between {} and {}.", dataGroup, segment.getTableName(), id1, id2);

                if(segmentDataList == null || segmentDataList.isEmpty()) {
                    break;
                }

                segmentsDataList.add(segmentDataList);
            }

            firstSegmentDataList = segmentsDataList.isEmpty() ? null : segmentsDataList.get(0);
            curRecords = firstSegmentDataList == null ? 0 : firstSegmentDataList.size();
            LOGGER.info("queryDataList, dataGroup {}, id between {} and {}, size {}.", dataGroup, id1, id2, curRecords);
            if(curRecords == 0) {
                segmentsDataList.clear();
                id1 = id2 + 1;
                id2 = id2 + pageSie;
                continue;
            }
            
            fileRecords += curRecords;

            long lastKey = ((Number)firstSegmentDataList.get(firstSegmentDataList.size() - 1).get(dataMap0.getKeyColumn())).longValue();
            isLastBatchOfFile = lastKey == max || (fileRecords + pageSie) > dataMap0.getPerRequestMaxRecords();

            if(isJson) {
                writeDataToJsonFile(file, segmentsDataList, dataMap0, formatter, dataWrapper, isLastBatchOfFile);
            } else {
                writeDataToTxtFile(file, segmentsDataList, dataMap0, formatter, dataWrapper, isLastBatchOfFile);
            }

            if (isLastBatchOfFile) {
                closeAndSaveDataFile(file, dataMap0, fileRecords);
                
                fileRecords = 0;
                if(lastKey < max) {
                    file = createNewDataFile(dataMap0);
                }
            }

            segmentsDataList.clear();
            id1 = id2 + 1;
            id2 = id2 + pageSie;
        }
    }

    private void writeDataToJsonFile(File file, List<List<Map<String, Object>>> segmentsDataList, DataMap dataMap,
                                     DataFormatter dataFormatter, RowDataWrapper dataWrapper, boolean isLastBatchOfFile) {
        int rowSize = segmentsDataList.get(0).size(), segSize = segmentsDataList.size();
        StringBuilder rowsStr = new StringBuilder(rowSize * 200);

        Map<String,Object> record;
        DataSegment seg;
        boolean isAppendLastRowSep;
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < segSize; j++) {
                record = segmentsDataList.get(j).get(i);
                seg = dataMap.getDataSegments().get(j);

                isAppendLastRowSep = isLastBatchOfFile && (i == rowSize - 1);
                rowsStr.append(formatRecordOfJson(record, seg, dataFormatter, dataWrapper, isAppendLastRowSep));
            }
        }
        try {
            FileUtils.writeStringToFile(file, rowsStr.toString(), dataMap.getEncoding(), true);
        } catch (IOException e) {
            LOGGER.error("write data to file error, dataGroup {}, filePath {}.", dataMap.getDataGroup(), file.getPath());
            throw new IllegalStateException("write data to file error.", e);
        }
    }

    private String formatRecordOfJson(Map<String, Object> row, DataSegment seg, DataFormatter dataFormatter, RowDataWrapper dataWrapper, boolean isLastRecSep) {
        StringBuilder recordStr = new StringBuilder(200);
        JSONObject json = new JSONObject();
        Object value;
        DataItem item;
        List<DataItem> items = seg.getDataItems();
        Map<String, DataFormatter.DataFormat> formatMap = seg.getFormatMap();
        DataFormatter.DataFormat format;
        String column;
        for (int i = 0, size = items.size(); i < size; i++) {
            item = items.get(i);
            column = item.getColumn();
            if(column.equalsIgnoreCase("FIX_VALUE")) {
                json.put(item.getField(), item.getValue());
            } else {
                value = row.get(column);
                format = formatMap.get(column);
                json.put(item.getField(), dataFormatter.format(value, format, item.getType()));
            }
        }
        recordStr.append(json.toJSONString());
        if(!isLastRecSep) {
            recordStr.append(dataWrapper.getRowEnd());
        }
        return recordStr.toString();
    }

    private void writeDataToTxtFile(File file, List<List<Map<String, Object>>> segmentsDataList, DataMap dataMap,
                                    DataFormatter dataFormatter, RowDataWrapper dataWrapper, boolean isLastBatchOfFile) {
        int rowSize = segmentsDataList.get(0).size(), segSize = segmentsDataList.size();
        StringBuilder rowsStr = new StringBuilder(rowSize * 200);

        Map<String,Object> record;
        DataSegment seg;
        boolean isAppendLastRowSep;
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < segSize; j++) {
                record = segmentsDataList.get(j).get(i);
                seg = dataMap.getDataSegments().get(j);

                isAppendLastRowSep = isLastBatchOfFile && (i == rowSize - 1);
                rowsStr.append(formatRecordOfTxt(record, seg, dataFormatter, dataWrapper, isAppendLastRowSep));
            }
        }
        try {
            FileUtils.writeStringToFile(file, rowsStr.toString(), dataMap.getEncoding(), true);
        } catch (IOException e) {
            LOGGER.error("write data to file error, dataGroup {}, filePath {}.", dataMap.getDataGroup(), file.getPath());
            throw new IllegalStateException("write data to file error.", e);
        }
    }

    private String formatRecordOfTxt(Map<String, Object> row, DataSegment seg, DataFormatter dataFormatter, RowDataWrapper dataWrapper, boolean isLastRecSep) {
        StringBuilder recordStr = new StringBuilder(200);
        recordStr.append(dataWrapper.getRowBegin());
        Object value;
        DataItem item;
        Map<String, DataFormatter.DataFormat> formatMap = seg.getFormatMap();
        DataFormatter.DataFormat format;
        List<DataItem> items = seg.getDataItems();
        String column;
        for (int i = 0, size = items.size(), last = size - 1; i < size; i++) {
            item = items.get(i);
            column = item.getColumn();
            if(column.equalsIgnoreCase("FIX_VALUE")) {
                recordStr.append(item.getValue());
            } else {
                format = formatMap.get(column);
                value = row.get(column);
                appendFieldOfTxt(recordStr, value, item, dataFormatter, format, dataWrapper);
            }
            if(i != last) {
                recordStr.append(dataWrapper.getFieldSeparator());
            }
        }
        if(!isLastRecSep) {
            recordStr.append(dataWrapper.getRowEnd());
        }
        return recordStr.toString();
    }

    private void appendFieldOfTxt(StringBuilder rowStr, Object value, DataItem item, DataFormatter dataFormatter, DataFormatter.DataFormat format, RowDataWrapper dataWrapper) {
        rowStr.append(dataWrapper.getRowBegin()).append(dataFormatter.format(value, format, item.getType()));
    }

    protected abstract void closeAndSaveDataFile(File file, DataMap dataMap, int fileRecords);


    protected abstract void writeDataFileTail(File file, DataMap dataMap);

    protected DataShareDoc newDocInfo(String dataGroup, File file, int fileRecords) {
        DataShareDoc doc = new DataShareDoc();
        Date time = new Date();
        doc.setDataGroup(dataGroup);
        doc.setDocName(file.getName());
        doc.setDocPath(file.getName());
        doc.setStatus(0);
        doc.setDocSize((int)file.length());
        doc.setRecords(fileRecords);
        doc.setFailCount(0);
        doc.setBizDate(DateUtils.truncate(new Date(), Calendar.DATE));
        doc.setAddTime(time);
        doc.setUpdateTime(time);
        return doc;
    }

    protected abstract File createNewDataFile(DataMap dataMap);


    protected abstract void writeDataFileHead(File file, DataMap dataMap);

    protected abstract String newDataFileName(StringBuilder path, DataMap dataMap);


    public String getDataGroup() {
        return dataGroup;
    }

    public void setDataGroup(String dataGroup) {
        this.dataGroup = dataGroup;
    }

    public static class RowDataWrapper {
        private String fieldSeparator;
        private String rowBegin;
        private String rowEnd;

        public String getFieldSeparator() {
            return fieldSeparator;
        }

        public void setFieldSeparator(String fieldSeparator) {
            this.fieldSeparator = fieldSeparator;
        }

        public String getRowBegin() {
            return rowBegin;
        }

        public void setRowBegin(String rowBegin) {
            this.rowBegin = rowBegin;
        }

        public String getRowEnd() {
            return rowEnd;
        }

        public void setRowEnd(String rowEnd) {
            this.rowEnd = rowEnd;
        }
    }
}
