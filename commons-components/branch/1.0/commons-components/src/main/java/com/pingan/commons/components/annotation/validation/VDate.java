/*
 * Copyright © PING AN INSURANCE (GROUP) COMPANY OF CHINA ，LTD. All Rights Reserved
 */
package com.pingan.commons.components.annotation.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.pingan.commons.components.core.validation.DateValidator;

/**
 * @author dongdaiming911@pingan.com
 * @date 2017年5月21日
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
public @interface VDate {
	
	Class<?>[] groups() default {};

	String message() default "{com.pingan.commons.components.annotation.validation.VDate.message}";

	String pattern() default "yyyy-MM-dd HH:mm:ss";

	boolean lenient() default false;
	
	Class<? extends Payload>[] payload() default { };
}
