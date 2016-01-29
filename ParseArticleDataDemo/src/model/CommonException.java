package model;

public class CommonException extends Exception {

	public CommonException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param message
	 * @param cause 上一层抛出的异常
	 */
	public CommonException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public CommonException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public CommonException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	

}
