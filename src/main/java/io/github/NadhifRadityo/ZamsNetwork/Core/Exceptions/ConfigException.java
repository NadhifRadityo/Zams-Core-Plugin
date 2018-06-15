package io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions;

public class ConfigException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6514540709231304476L;

	public ConfigException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ConfigException(String message) {
		super(message);
	}
}
