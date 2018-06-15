package io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions;

public class FileException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5728734920647256158L;

	public FileException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public FileException(String message) {
		super(message);
	}
}
