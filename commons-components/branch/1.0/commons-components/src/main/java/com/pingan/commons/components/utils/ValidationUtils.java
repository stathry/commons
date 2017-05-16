/*
 * Copyright © PING AN INSURANCE (GROUP) COMPANY OF CHINA ，LTD. All Rights Reserved
 */
package com.pingan.commons.components.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;

import com.pingan.commons.components.pojo.dto.ValidateResult;

/**
 * @author Demon
 * @date 2017年1月5日
 */
public class ValidationUtils {
	
	private static Validator validator;
	private static final String ITEM_SEP = ";";
	private static final String FIELD_SEP = ",";
	
	static {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	public static ValidateResult validate(Object bean) {
		if(bean == null) {
			return ValidateResult.DEFAULT;
		}
		
		Set<ConstraintViolation<Object>> resultSet = validator.validate(bean, bean.getClass());
		if(resultSet == null || resultSet.isEmpty()) {
			return ValidateResult.SUCCESS;
		}
		
		Map<String, String> items = new HashMap<>();
		ValidateResult result = new ValidateResult(false);
		for (ConstraintViolation<Object> c : resultSet) {
			String field = String.valueOf(c.getPropertyPath());
			if(items.containsKey(field)) {
				items.put(field, items.get(field) + ITEM_SEP + c.getMessage());
			} else {
				items.put(field, c.getMessage());
			}
		}
		result.setFailedItems(items);
		result.setInvalidFields(StringUtils.join(items.keySet(), FIELD_SEP));
		return result;
	}

}
