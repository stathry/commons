package org.stathry.commons.model;

import java.util.Date;

/**
 * 统一外部数据共享文件信息
 * 
 * @author dongdaiming
 */
public class DataShareDoc {

    private Long id;

    /** 数据分组 */
    private String dataGroup;
    
    /** 文件名称 */
    private String docName;

    /** 状态（0-待处理, 1-处理中, 9-完成） */
    private Integer status;
    
    /** 失败次数  */
    private Integer failCount;
    
    /** 文件所含记录数  */
    private Integer records;
    
    /** 文件大小 */
    private Integer docSize;
    
    /** 文件全路径 */
    private String docPath;
    
    /** 创建时间 */
    private Date addTime;
    
    /** 更新时间 */
    private Date updateTime;
    
    /** 业务日期 */
    private Date bizDate;

    @Override
    public String toString() {
        return "DataShareDoc{" +
                "id=" + id +
                ", dataGroup='" + dataGroup + '\'' +
                ", docName='" + docName + '\'' +
                ", status=" + status +
                ", failCount=" + failCount +
                ", records=" + records +
                ", docSize=" + docSize +
                ", docPath='" + docPath + '\'' +
                ", addTime=" + addTime +
                ", updateTime=" + updateTime +
                ", bizDate=" + bizDate +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocPath() {
        return docPath;
    }

    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getRecords() {
        return records;
    }

    public void setRecords(Integer records) {
        this.records = records;
    }

    public Integer getDocSize() {
        return docSize;
    }

    public void setDocSize(Integer docSize) {
        this.docSize = docSize;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public Date getBizDate() {
        return bizDate;
    }

    public void setBizDate(Date bizDate) {
        this.bizDate = bizDate;
    }

    public String getDataGroup() {
        return dataGroup;
    }

    public void setDataGroup(String dataGroup) {
        this.dataGroup = dataGroup;
    }
}
