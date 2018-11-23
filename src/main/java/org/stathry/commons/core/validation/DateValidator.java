/*
 * Copyright © stathry@126.com All Rights Reserved
 */
package org.stathry.commons.core.validation;

import org.stathry.commons.annotation.validation.VDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author stathry@126.com
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
