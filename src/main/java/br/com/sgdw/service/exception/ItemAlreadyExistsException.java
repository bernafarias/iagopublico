package br.com.sgdw.service.exception;

import org.apache.http.HttpStatus;

public class ItemAlreadyExistsException extends IagoException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3814260508982391414L;
	public static final int STATUS = HttpStatus.SC_CONFLICT;
	
	public ItemAlreadyExistsException(String msg){
		super(msg, STATUS);
	}

}
