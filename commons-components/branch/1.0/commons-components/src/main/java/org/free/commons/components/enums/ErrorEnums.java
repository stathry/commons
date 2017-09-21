/**
 * Copyright © PING AN INSURANCE (GROUP) COMPANY OF CHINA ，LTD. All Rights Reserved
 */
package org.free.commons.components.enums;

/**
 * @author Demon
 * @date 2016年12月11日
 */
public enum ErrorEnums {
	
	SUCCESS(0, "success.", "成功。"),
	
	
	ERR_PARAMS(1000, "invalid params.", "参数无效。"),
	ERR_PARAMS_LIST(1001, "invalid params list:[{0}].", "无效参数列表:[{0}]。"),
	
	ERR_DAO(7000, "data access error.", "数据访问异常。"),
	ERR_DAO_SQL(7001, "sql error.", "SQL异常。"),
	
	ERR_RPC(8000, "remote call error.", "远程调用异常。"),
	ERR_RPC_TIMEOUT(8001, "remote call timeout.", "远程调用超时。"),
	
	ERR_SYS(9000, "system error.", "系统异常。"),
	ERR_SYS_TIMEOUT(9001, "system timeout.", "系统繁忙。"),
	
	;
	
	private int code;
	private String msg;
	private String desc;
	
	/**
	 * @param code
	 * @param msg
	 */
	private ErrorEnums(int code, String msg, String desc) {
		this.code = code;
		this.msg = msg;
		this.desc = desc;
	}

	public int code() {
		return code;
	}

	public String msg() {
		return msg;
	}

	public String desc() {
		return desc;
	}
}
