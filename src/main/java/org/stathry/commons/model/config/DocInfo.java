/**
 * Copyright 2016-2100 free Co., Ltd.
 */
package org.stathry.commons.model.config;

import java.util.Date;

/**
 * @author dongdaiming@free.com
 * <p>
 * 2016年8月18日
 */
public class DocInfo {

    private String content;

    private String title;

    private String creator;

    private String lastModifiedBy;

    private String company;

    private Date createdDate;

    private Date lastModifiedDate;

    private int characters;

    private long size;

    @Override
    public String toString() {
        return "WordEntity [title=" + title + ", creator=" + creator
                + ", lastModifiedBy=" + lastModifiedBy + ", company=" + company
                + ", createdDate=" + createdDate + ", lastModifiedDate="
                + lastModifiedDate + ", characters=" + characters + ", size="
                + size + "]";
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public int getCharacters() {
        return characters;
    }

    public void setCharacters(int characters) {
        this.characters = characters;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
