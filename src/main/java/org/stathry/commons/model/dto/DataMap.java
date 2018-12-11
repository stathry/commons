package org.stathry.commons.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 数据配置
 * 
 * @author dongdaiming
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DataMap {

	/** 数据名 */
	@XmlAttribute(name = "dataDesc")
	private String dataDesc;

	/** 数据编码 */
	@XmlAttribute(name = "dataGroup")
	private String dataGroup;
	
	/** 数据编号 */
	@XmlAttribute(name = "pageSize")
	private Integer pageSize;

    /** 查询类型:all/byDate */
    @XmlAttribute(name = "queryType")
    private String queryType;

    /** 主表表名,依据主表 */
    @XmlAttribute(name = "mainTableName")
    private String mainTableName;

    /** 文件类型:json/txt/csv/jsonStr,为jsonStr时不生成文件并且实时发送报文 */
    @XmlAttribute(name = "fileType")
    private String fileType;

    /** 查询最近多少天或多少月 */
    @XmlAttribute(name = "queryLastDaysOrMonths")
    private String queryLastDaysOrMonths;

    /** 分页依赖字段 */
    @XmlAttribute(name = "keyColumn")
    private String keyColumn = "id";

    @XmlAttribute(name = "dateColumn")
    private String dateColumn;

    /** 单次请求最大记录数 */
    @XmlAttribute(name = "perRequestMaxRecords")
    private int perRequestMaxRecords;

    @XmlAttribute(name = "encoding")
    private String encoding;

    @XmlAttribute(name = "floatScale")
    private int floatScale = 2;

    @XmlAttribute(name = "datePattern")
    private String datePattern = "yyyy-MM-dd HH:mm:ss";

	/** 数据段 */
	@XmlElementWrapper(name = "DataSegments")
	@XmlElement(name = "DataSegment")
	private List<DataSegment> dataSegments;

	private AtomicLong requestCounter = new AtomicLong();

    public String getDataDesc() {
        return dataDesc;
    }

    public void setDataDesc(String dataDesc) {
        this.dataDesc = dataDesc;
    }

    public String getDataGroup() {
        return dataGroup;
    }

    public void setDataGroup(String dataGroup) {
        this.dataGroup = dataGroup;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getMainTableName() {
        return mainTableName;
    }

    public void setMainTableName(String mainTableName) {
        this.mainTableName = mainTableName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getQueryLastDaysOrMonths() {
        return queryLastDaysOrMonths;
    }

    public void setQueryLastDaysOrMonths(String queryLastDaysOrMonths) {
        this.queryLastDaysOrMonths = queryLastDaysOrMonths;
    }

    public String getKeyColumn() {
        return keyColumn;
    }

    public void setKeyColumn(String keyColumn) {
        this.keyColumn = keyColumn;
    }

    public String getDateColumn() {
        return dateColumn;
    }

    public void setDateColumn(String dateColumn) {
        this.dateColumn = dateColumn;
    }

    public int getPerRequestMaxRecords() {
        return perRequestMaxRecords;
    }

    public void setPerRequestMaxRecords(int perRequestMaxRecords) {
        this.perRequestMaxRecords = perRequestMaxRecords;
    }

    public int getFloatScale() {
        return floatScale;
    }

    public void setFloatScale(int floatScale) {
        this.floatScale = floatScale;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    public List<DataSegment> getDataSegments() {
        return dataSegments;
    }

    public void setDataSegments(List<DataSegment> dataSegments) {
        this.dataSegments = dataSegments;
    }

    public AtomicLong getRequestCounter() {
        return requestCounter;
    }

    public void setRequestCounter(AtomicLong requestCounter) {
        this.requestCounter = requestCounter;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public String toString() {
        return "DataMap{" +
                "dataDesc='" + dataDesc + '\'' +
                ", dataGroup='" + dataGroup + '\'' +
                ", pageSize=" + pageSize +
                ", queryType='" + queryType + '\'' +
                ", mainTableName='" + mainTableName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", queryLastDaysOrMonths='" + queryLastDaysOrMonths + '\'' +
                ", keyColumn='" + keyColumn + '\'' +
                ", dateColumn='" + dateColumn + '\'' +
                ", perRequestMaxRecords=" + perRequestMaxRecords +
                ", encoding='" + encoding + '\'' +
                ", floatScale=" + floatScale +
                ", datePattern='" + datePattern + '\'' +
                ", dataSegments=" + dataSegments +
                ", requestCounter=" + requestCounter +
                '}';
    }
}
