package br.com.sgdw.service.exception;

public class DatasetListNotFoundException extends ItemNotFoundException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2782677174672682763L;
	private static final String MSG = "Nenhum conjunto de dados cadastrado!!!";
	
	public DatasetListNotFoundException(){
		super(MSG);
	}
}
