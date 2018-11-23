/**
 *
 */
package org.stathry.commons.exception;

/**
 * 远程调用异常
 *
 * @author Demon
 * @date 2016年12月3日
 */
public class RPCException extends Exception {

    private static final long serialVersionUID = -3381143198384297878L;

    public RPCException() {
        super();
    }

    public RPCException(String message) {
        super(message);
    }

    public RPCException(Throwable cause) {
        super(cause);
    }

    public RPCException(String message, Throwable cause) {
        super(message, cause);
    }

}
