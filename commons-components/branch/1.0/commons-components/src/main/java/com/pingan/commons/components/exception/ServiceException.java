/**
 * 
 */
package com.pingan.commons.components.exception;

import java.text.MessageFormat;

import com.pingan.commons.components.enums.ErrorEnums;

/**
 * @author Demon
 * @date 2016年12月3日
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = -5308631704209507793L;
	
	private int code;
	
	public ServiceException() {
		super();
	}
	
	public ServiceException(String message) {
		super(message);
	}
	
	public ServiceException(int code, String message) {
		super(message);
		this.code = code;
	}
	
	public ServiceException(ErrorEnums e) {
		super(e.msg());
		this.code = e.code();
	}
	
	public ServiceException(ErrorEnums e, String params) {
		super(new MessageFormat(e.msg()).format(new Object[]{params}));
		this.code = e.code();
	}
	
	public ServiceException(Throwable cause) {
		super(cause);
	}
	
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public int getCode() {
		return code;
	}
	
}
