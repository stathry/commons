package org.stathry.commons.model.dto;

import org.stathry.commons.utils.DataFormatter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;
import java.util.Map;

/**
 * 数据段配置
 * 
 * @author dongdaiming
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DataSegment {

	/** 数据段编号 */
	@XmlAttribute(name = "no")
	private Integer no;

	/** 表名称 */
	@XmlAttribute(name = "tableName")
	private String tableName;
	
	/** 数据段名称 */
	@XmlAttribute(name = "segmentDesc")
	private String segmentDesc;
	
	/** 数据段选项（R,O,C） */
	@XmlAttribute(name = "optional")
	private String optional;

    /** 分页依赖字段 */
    @XmlAttribute(name = "keyColumn")
    private String keyColumn;

	@XmlElementWrapper(name = "DataItems")
	@XmlElement(name = "DataItem")
	private List<DataItem> dataItems;
	private Map<String, DataFormatter.DataFormat> formatMap;

    private String selectColumns;

    @Override
    public String toString() {
        return "DataSegment{" +
                "no=" + no +
                ", tableName='" + tableName + '\'' +
                ", segmentDesc='" + segmentDesc + '\'' +
                ", optional='" + optional + '\'' +
                ", dataItems=" + dataItems +
                '}';
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSegmentDesc() {
        return segmentDesc;
    }

    public void setSegmentDesc(String segmentDesc) {
        this.segmentDesc = segmentDesc;
    }

    public String getOptional() {
        return optional;
    }

    public void setOptional(String optional) {
        this.optional = optional;
    }

    public List<DataItem> getDataItems() {
        return dataItems;
    }

    public void setDataItems(List<DataItem> dataItems) {
        this.dataItems = dataItems;
    }

    public String getKeyColumn() {
        return keyColumn;
    }

    public void setKeyColumn(String keyColumn) {
        this.keyColumn = keyColumn;
    }

    public String getSelectColumns() {
        return selectColumns;
    }

    public void setSelectColumns(String selectColumns) {
        this.selectColumns = selectColumns;
    }

    public Map<String, DataFormatter.DataFormat> getFormatMap() {
        return formatMap;
    }

    public void setFormatMap(Map<String, DataFormatter.DataFormat> formatMap) {
        this.formatMap = formatMap;
    }
}
