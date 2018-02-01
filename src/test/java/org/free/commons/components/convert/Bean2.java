/**
 * Copyright © PING AN INSURANCE (GROUP) COMPANY OF CHINA ，LTD. All Rights Reserved
 */
package org.free.commons.components.convert;

import org.bryadong.commons.annotation.convert.CNum;

/**
 * @author Demon
 * @date 2016年12月12日
 */
public class Bean2 {
	@CNum
	private String age;
	@CNum
	private Double money;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

}
