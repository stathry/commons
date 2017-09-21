/**
 * 
 */
package org.free.commons.components.exception;

/**
 * @author Demon
 * @date 2016年12月3日
 */
public class DAOException extends Exception {

	private static final long serialVersionUID = -5308631704209507793L;
	
	public DAOException() {
		super();
	}
	
	public DAOException(String message) {
		super(message);
	}
	
	public DAOException(Throwable cause) {
		super(cause);
	}
	
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

}
