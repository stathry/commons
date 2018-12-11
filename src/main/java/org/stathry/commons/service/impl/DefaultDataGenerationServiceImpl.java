package org.stathry.commons.service.impl;

import com.google.common.io.Files;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stathry.commons.model.dto.DataMap;
import org.stathry.commons.service.DataGenerationService;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 通用数据生成接口默认实现
 * Created by dongdaiming on 2018-12-06 14:34
 */
public class DefaultDataGenerationServiceImpl extends AbstractDataGenerationService implements DataGenerationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDataGenerationServiceImpl.class);

    @Override
    protected RowDataWrapper newDataWrapper(DataMap dataMap) {
        RowDataWrapper wrapper = new RowDataWrapper();
        String fileType = dataMap.getFileType();
        if("json".equalsIgnoreCase(fileType) || "jsonStr".equalsIgnoreCase(fileType)) {
            wrapper.setFieldSeparator("");
            wrapper.setRowBegin("");
            wrapper.setRowEnd(",");
        } else if("txt".equalsIgnoreCase(fileType)) {
            wrapper.setFieldSeparator("|");
            wrapper.setRowBegin("");
            wrapper.setRowEnd(System.getProperty("line.separator"));
        } else {
            throw new UnsupportedOperationException("dataGroup " + dataGroup + ", fileType " + fileType);
        }
        return wrapper;
    }

    @Override
    protected void closeAndSaveDataFile(File file, DataMap dataMap, int fileRecords) {
        writeDataFileTail(file, dataMap);

        dataShareDocDAO.insert(newDocInfo(dataGroup, file, fileRecords));
        LOGGER.info("write data file success, dataGroup {}, filename {}.", dataGroup, file.getName());
    }

    @Override
    protected File createNewDataFile(DataMap dataMap) {
        StringBuilder path = new StringBuilder(baseFilePath).append(dataGroup).append('/')
                .append(DateFormatUtils.format(new Date(), "yyyyMMdd")).append('/');
        newDataFileName(path, dataMap);
        File file = new File(path.toString());

        try {
            Files.createParentDirs(file);
            file.createNewFile();
        } catch (IOException e) {
            throw new IllegalArgumentException("create dataFile error, " + path.toString(), e);
        }

        writeDataFileHead(file, dataMap);

        return file;
    }

    @Override
    protected String newDataFileName(StringBuilder path, DataMap dataMap) {
        path.append(baseFileName).append('_').append(dataGroup.toLowerCase());
        path.append('_').append(dataMap.getRequestCounter().incrementAndGet()).append('_')
                .append(System.currentTimeMillis()).append('.').append(dataMap.getFileType().toLowerCase());
        return path.toString();
    }
    @Override
    protected void writeDataFileTail(File file, DataMap dataMap) {

    }

    @Override
    protected void writeDataFileHead(File file, DataMap dataMap) {

    }
}
