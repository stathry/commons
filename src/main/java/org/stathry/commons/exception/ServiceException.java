/**
 * 
 */
package org.stathry.commons.exception;

import java.text.MessageFormat;

import org.stathry.commons.enums.ErrorEnum;

/**
 * @author Demon
 * @date 2016年12月3日
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = -5308631704209507793L;
	
	private String code;
	
	public ServiceException() {
		super();
	}
	
	public ServiceException(String message) {
		super(message);
	}
	
	public ServiceException(String code, String message) {
		super(message);
		this.code = code;
	}
	
	public ServiceException(ErrorEnum e) {
		super(e.msg());
		this.code = e.code();
	}
	
	public ServiceException(ErrorEnum e, String params) {
		super(new MessageFormat(e.msg()).format(new Object[]{params}));
		this.code = e.code();
	}
	
	public ServiceException(Throwable cause) {
		super(cause);
	}
	
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public String getCode() {
		return code;
	}
	
}
