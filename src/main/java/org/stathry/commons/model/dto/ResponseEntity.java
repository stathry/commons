package org.stathry.commons.model.dto;

import java.io.Serializable;

/**
 * ResponseEntity
 */
public class ResponseEntity<T> implements Serializable {

    /**
     * 请求标识
     */
    private String transId;
    /**
     * 请求处理标识
     */
    private String bizNo;
    private boolean success;
    private String code;
    private String msg;
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }
}
