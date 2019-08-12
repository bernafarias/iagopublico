package br.com.sgdw.service.exception;

public class DatasetPageNotFound extends ItemNotFoundException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 793892486046014208L;
	private static final String MSG = "Esta página do Dataset não existe!!";
			
	public DatasetPageNotFound(){
		super(MSG);
	}

}
