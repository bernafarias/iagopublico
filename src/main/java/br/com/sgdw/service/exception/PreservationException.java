package br.com.sgdw.service.exception;

import org.apache.http.HttpStatus;

public class PreservationException extends IagoException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1477581814297410349L;
	private static final int STATUS = HttpStatus.SC_LOCKED;
	private static final String MSG = "Este conjunto de dados está preservado. ";
	
	public PreservationException(String motivo){
		super(MSG + "Motivo: " +motivo, STATUS);
	}
	
}
