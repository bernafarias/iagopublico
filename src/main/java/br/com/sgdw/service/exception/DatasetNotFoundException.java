package br.com.sgdw.service.exception;

public class DatasetNotFoundException extends ItemNotFoundException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String MSG = "Conjunto de dados não encontrado!!";
	
	public DatasetNotFoundException(){
		super(MSG);
	}
}
