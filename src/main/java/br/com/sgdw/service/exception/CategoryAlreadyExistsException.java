package br.com.sgdw.service.exception;

import org.apache.http.HttpStatus;

public class CategoryAlreadyExistsException extends ItemAlreadyExistsException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8020076552649523099L;
	public static final int STATUS = HttpStatus.SC_CONFLICT;
	private static final String MSG = "Categoria já cadastrada!!";
	
	public CategoryAlreadyExistsException(){
		super(MSG);
	}
	

}
