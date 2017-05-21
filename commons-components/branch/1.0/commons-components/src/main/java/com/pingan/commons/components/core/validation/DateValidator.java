/*
 * Copyright © PING AN INSURANCE (GROUP) COMPANY OF CHINA ，LTD. All Rights Reserved
 */
package com.pingan.commons.components.core.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.pingan.commons.components.annotation.validation.VDate;

/**
 * @author dongdaiming911@pingan.com
 * @date 2017年5月21日
 */
public class DateValidator implements ConstraintValidator<VDate, String> {

	private boolean lenient;
	private String pattern;

	@Override
	public void initialize(VDate an) {
		lenient = an.lenient();
		pattern = an.pattern();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		if (value.trim().equals("") || pattern == null || pattern.trim().equals("")) {
			return false;
		}

		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		formatter.setLenient(lenient);
		try {
			formatter.parse(value);
		} catch (ParseException e) {
			return false;
		}

		return true;
	}

}
