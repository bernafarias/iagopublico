package br.com.sgdw.service.exception;

public class DatasetAlreadyExistsException extends ItemAlreadyExistsException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4033165002153277304L;
	private static final String MSG = "Este Conjunto de Dados já existe!";
	
	public DatasetAlreadyExistsException(){
		super(MSG);
	}
	
}
