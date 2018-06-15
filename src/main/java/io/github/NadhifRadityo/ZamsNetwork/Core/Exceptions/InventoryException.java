package io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions;

public class InventoryException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5504874015194622480L;

	public InventoryException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InventoryException(String message) {
		super(message);
	}
}
