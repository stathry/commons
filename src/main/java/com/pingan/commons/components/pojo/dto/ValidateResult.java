/*
 * Copyright © PING AN INSURANCE (GROUP) COMPANY OF CHINA ，LTD. All Rights Reserved
 */
package com.pingan.commons.components.pojo.dto;

import java.util.Map;

/**
 * @author Demon
 * @date 2017年1月5日
 */
public class ValidateResult {

	private boolean success;
	private String invalidFields;
	private Map<String, String> failedItems;
	
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

	public Map<String, String> getFailedItems() {
		return failedItems;
	}

	public void setFailedItems(Map<String, String> failedItems) {
		this.failedItems = failedItems;
	}

}
