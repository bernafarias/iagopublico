package br.com.sgdw.service.exception;

public abstract class InvalidValueException extends Exception{

	/**
	 * @author Wilker
	 * 
	 * Exceção abstrata para campos enumerados inseridos de forma inválida
	 */
	private static final long serialVersionUID = -9064594078148864632L;
	
	public InvalidValueException(String msg){
		super(msg);
	}
	
}
