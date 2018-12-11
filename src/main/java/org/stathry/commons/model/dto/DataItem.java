package org.stathry.commons.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * 数据项配置
 * 
 * @author dongdaiming
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DataItem {

	/** 字段编号 */
	@XmlAttribute(name = "no")
	private Integer no;

	/** db字段 */
	@XmlAttribute(name = "column")
	private String column;

	/** 字段名称 */
	@XmlAttribute(name = "fieldDesc")
	private String fieldDesc;

	/** 字段 */
	@XmlAttribute(name = "field")
	private String field;

	/** 字段类型（0-str,1-int,2-float,3-date） */
	@XmlAttribute(name = "type")
	private int type;

	/** 字段选项（M,O,C） */
	@XmlAttribute(name = "optional")
	private String optional;

	/** 值(与column配合使用) */
	@XmlAttribute(name = "value")
	private String value;

    @XmlAttribute(name = "multiply")

    private Double multiply;

    @XmlAttribute(name = "floatScale")
    private Integer floatScale;

    /** 值(与column配合使用) */
    @XmlAttribute(name = "srcFormat")
    private String srcFormat;
    /** 值(与column配合使用) */
    @XmlAttribute(name = "format")
    private String format;

    @Override
    public String toString() {
        return "DataItem{" +
                "no=" + no +
                ", column='" + column + '\'' +
                ", fieldDesc='" + fieldDesc + '\'' +
                ", field='" + field + '\'' +
                ", type='" + type + '\'' +
                ", optional='" + optional + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOptional() {
        return optional;
    }

    public void setOptional(String optional) {
        this.optional = optional;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Double getMultiply() {
        return multiply;
    }

    public void setMultiply(Double multiply) {
        this.multiply = multiply;
    }

    public Integer getFloatScale() {
        return floatScale;
    }

    public void setFloatScale(Integer floatScale) {
        this.floatScale = floatScale;
    }

    public String getSrcFormat() {
        return srcFormat;
    }

    public void setSrcFormat(String srcFormat) {
        this.srcFormat = srcFormat;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
