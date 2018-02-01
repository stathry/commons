package org.bryadong.commons.enums;

import java.io.Serializable;

/**
 * 数据状态枚举
 * @author dongdaiming911
 */
public enum DataStateEnums implements Serializable {

	DELETED(0, "已废弃"),
	NORMAL(1, "正常"),
	;
	
	private DataStateEnums(int state, String desc) {
		this.state = state;
		this.desc = desc;
	}
	
	private int state;
	private String desc;
	
	public int state() {
		return state;
	}
	public String desc() {
		return desc;
	}
	
}
