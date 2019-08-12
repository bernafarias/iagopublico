package br.com.sgdw.service.exception;

public class SourceListNotFound extends ItemNotFoundException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3750266527171292914L;
	private static final String MSG = "Fontes de dados não encontradas!!!";
	
	public SourceListNotFound(){
		super(MSG);
	}

}
