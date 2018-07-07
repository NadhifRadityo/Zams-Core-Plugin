package io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions;

public class ClassException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4953538239960949989L;

	public ClassException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ClassException(String message) {
		super(message);
	}
}
