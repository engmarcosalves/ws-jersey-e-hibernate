package br.com.geekcode.webservice.exceptions;

public class DAOException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8474052563741461935L;
	
	private int code;
	
	public DAOException(String message, int code) {
		super(message);
		this.code = code;
	}
	
	public int getCode( ) {
		return code;
	}

}
