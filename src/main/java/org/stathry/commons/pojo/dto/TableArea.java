package org.stathry.commons.pojo.dto;

import java.util.Date;

/**
 * DB数据范围
 * @param <K>
 */
public class TableArea<K extends Comparable<K>> {

    private String primaryKey = "id";
    private K beginKey;
    private K endKey;
    private String timeColumn = "create_time";
    private Date beginTime;
    private Date endTime;
    private int limit = 5000;

    public TableArea() {
    }

    public TableArea(String primaryKey, K beginKey, K endKey) {
        this.primaryKey = primaryKey;
        this.beginKey = beginKey;
        this.endKey = endKey;
    }

    public TableArea(String timeColumn, Date beginTime, Date endTime) {
        this.timeColumn = timeColumn;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public TableArea(String primaryKey, K beginKey, K endKey, String timeColumn, Date beginTime, Date endTime) {
        this.primaryKey = primaryKey;
        this.beginKey = beginKey;
        this.endKey = endKey;
        this.timeColumn = timeColumn;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "TableArea{" +
                "primaryKey='" + primaryKey + '\'' +
                ", beginKey=" + beginKey +
                ", endKey=" + endKey +
                ", timeColumn='" + timeColumn + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", limit=" + limit +
                '}';
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getTimeColumn() {
        return timeColumn;
    }

    public void setTimeColumn(String timeColumn) {
        this.timeColumn = timeColumn;
    }

    public K getBeginKey() {
        return beginKey;
    }

    public void setBeginKey(K beginKey) {
        this.beginKey = beginKey;
    }

    public K getEndKey() {
        return endKey;
    }

    public void setEndKey(K endKey) {
        this.endKey = endKey;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
