/*
 * Copyright © stathry@126.com All Rights Reserved
 */
package org.stathry.commons.convert;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.stathry.commons.annotation.validation.VDate;

/**
 * @author stathry@126.com
 * @date 2017年5月21日
 */
public class Bean3 {
	@VDate
	@Email
	private String birth;
	
	@NotEmpty
	private String name;

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
