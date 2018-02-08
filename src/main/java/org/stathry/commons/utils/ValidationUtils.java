/*
 * Copyright © stathry@126.com All Rights Reserved
 */
package org.stathry.commons.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.stathry.commons.pojo.dto.ValidateResult;

/**
 * @author Demon
 * @date 2017年1月5日
 */
public class ValidationUtils {

	private static Validator validator;
	private static final String FIELD_SEP = ",";
	private static final String ITEM_SEP = " ~ ";

	static {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	public static ValidateResult validate(Object bean) {
		return validate(bean, false);
	}

	public static ValidateResult validate(Object bean, boolean isWithItemDetail) {
		if (bean == null) {
			return ValidateResult.DEFAULT;
		}

		Set<ConstraintViolation<Object>> conSet = validator.validate(bean);
		if (conSet == null || conSet.isEmpty()) {
			return ValidateResult.SUCCESS;
		}

		ValidateResult result = new ValidateResult(false);
		if (isWithItemDetail) {
			Map<String, Map<String, String>> detailItems = new HashMap<>();
			putItemDetail(conSet, detailItems);

			result.setFailedDetailItems(detailItems);
			result.setInvalidFields(StringUtils.join(detailItems.keySet(), FIELD_SEP));
		} else {
			Map<String, StringBuffer> items = new HashMap<>();
			putItem(conSet, items);

			result.setFailedItems(items);
			result.setInvalidFields(StringUtils.join(items.keySet(), FIELD_SEP));
		}

		return result;
	}

	private static void putItem(Set<ConstraintViolation<Object>> conSet, Map<String, StringBuffer> items) {
		String field;
		StringBuffer desc;
		for (ConstraintViolation<Object> c : conSet) {
			field = String.valueOf(c.getPropertyPath());
			if (items.containsKey(field)) {
				items.put(field, items.get(field).append(ITEM_SEP).append(c.getMessage()));
			} else {
				desc = new StringBuffer(c.getMessage());
				items.put(field, desc);
			}
		}
	}

	private static void putItemDetail(Set<ConstraintViolation<Object>> conSet, Map<String, Map<String, String>> items) {
		String field;
		Map<String, String> vmap;
		String vname;
		for (ConstraintViolation<Object> c : conSet) {
			field = String.valueOf(c.getPropertyPath());
			vname = c.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
			if (items.containsKey(field)) {
				items.get(field).put(vname, c.getMessage());
			} else {
				vmap = new HashMap<String, String>(3);
				vmap.put(vname, c.getMessage());
				items.put(field, vmap);
			}
		}
	}

}
