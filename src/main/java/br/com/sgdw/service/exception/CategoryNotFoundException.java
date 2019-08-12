package br.com.sgdw.service.exception;


public class CategoryNotFoundException extends ItemNotFoundException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1505224474607291489L;
	private static final String MSG = "Categoria não encontrada!";
	
	public CategoryNotFoundException(){
		super(MSG);
	}
}
