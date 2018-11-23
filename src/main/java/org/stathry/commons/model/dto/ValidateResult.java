/*
 * Copyright © stathry@126.com All Rights Reserved
 */
package org.stathry.commons.model.dto;

import java.util.Map;

/**
 * @author Demon
 * @date 2017年1月5日
 */
public class ValidateResult {

    private boolean success;
    private String invalidFields;
    private Map<String, Map<String, String>> failedDetailItems;
    private Map<String, StringBuffer> failedItems;

    public static final ValidateResult DEFAULT = new ValidateResult(false);
    public static final ValidateResult SUCCESS = new ValidateResult(true);

    public ValidateResult() {
    }

    public ValidateResult(boolean success) {
        this.success = success;
    }

    public ValidateResult(boolean success, String invalidFields) {
        this.success = success;
        this.invalidFields = invalidFields;
    }

    @Override
    public String toString() {
        return "ValidateResult [success=" + success + ", invalidFields=" + invalidFields + "]";
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getInvalidFields() {
        return invalidFields;
    }

    public void setInvalidFields(String invalidFields) {
        this.invalidFields = invalidFields;
    }

    public Map<String, Map<String, String>> getFailedDetailItems() {
        return failedDetailItems;
    }

    public void setFailedDetailItems(Map<String, Map<String, String>> failedDetailItems) {
        this.failedDetailItems = failedDetailItems;
    }

    public Map<String, StringBuffer> getFailedItems() {
        return failedItems;
    }

    public void setFailedItems(Map<String, StringBuffer> failedItems) {
        this.failedItems = failedItems;
    }
}
