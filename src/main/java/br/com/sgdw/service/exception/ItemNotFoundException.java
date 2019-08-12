package br.com.sgdw.service.exception;

import org.apache.http.HttpStatus;

public abstract class ItemNotFoundException extends IagoException{

	private static final long serialVersionUID = 708381313491260099L;
	public static final int STATUS = HttpStatus.SC_BAD_REQUEST;

	public ItemNotFoundException(String msg){
		super(msg, STATUS);
	}
}
