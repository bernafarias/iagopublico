package br.com.sgdw.service.exception;

public class FileNotSupportedException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -59486038593476585L;
	private static final String MSG = "Formato de arquivo não suportado. Utilize o formato CSV";
	
	public FileNotSupportedException(){
		super(MSG);
	}
	
}
