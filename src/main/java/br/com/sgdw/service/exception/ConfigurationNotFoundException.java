package br.com.sgdw.service.exception;

import org.apache.http.HttpStatus;

public class ConfigurationNotFoundException extends ItemNotFoundException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6917933407995032208L;
	public static final int STATUS = HttpStatus.SC_BAD_REQUEST;
	private static final String MSG = "Configuração de Conjunto de Dados não encontrado!!!";
	
	public ConfigurationNotFoundException(){
		super(MSG);
	}
}
