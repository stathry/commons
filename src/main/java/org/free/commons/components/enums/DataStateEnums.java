package org.free.commons.components.enums;

/**
 * 数据状态枚举
 * @author dongdaiming911
 */
public enum DataStateEnums {

	DELETED(-1, "已废弃"),
	FROZEN(0, "已冻结"),
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
