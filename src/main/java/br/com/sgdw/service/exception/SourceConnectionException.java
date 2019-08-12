package br.com.sgdw.service.exception;

import org.apache.http.HttpStatus;

public class SourceConnectionException extends IagoException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7413072609492210505L;
	public static final int STATUS = HttpStatus.SC_INTERNAL_SERVER_ERROR; 
	private static final String MSG = "Ocorreu um erro na conexão com a fonte de dados";
	
	public SourceConnectionException(){
		super(MSG, STATUS);
	}

}
