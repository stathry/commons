/**
 * Copyright © PING AN INSURANCE (GROUP) COMPANY OF CHINA ，LTD. All Rights Reserved
 */
package org.stathry.commons.convert;

import java.io.Serializable;

import org.stathry.commons.annotation.validation.VDate;

/**
 * @author Demon
 * @date 2016年12月12日
 */
public class Bean1 implements Serializable {
	/**  */
	private static final long serialVersionUID = -444778988918263265L;
	private Integer age;
	private String money;
	private String bitch;
	@VDate(pattern="yyyy-MM-dd HH:mm:ss")
	private String birth;

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getBitch() {
		return bitch;
	}

	public void setBitch(String bitch) {
		this.bitch = bitch;
	}
}
