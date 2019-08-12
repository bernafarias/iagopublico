package br.com.sgdw.service.exception;

public class UserNotFoundException extends ItemNotFoundException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String MSG = "Usuário não encontrado!!!";
	
	public UserNotFoundException(){
		super(MSG);
	}
	
	
}
