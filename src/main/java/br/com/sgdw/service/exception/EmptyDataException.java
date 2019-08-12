package br.com.sgdw.service.exception;

import org.apache.http.HttpStatus;

public class EmptyDataException extends IagoException{

	/**
	 * @author Wilker Santos
	 * 
	 * Classe abstrata para tratar inserção de conjuntos de dados vazios
	 */
	private static final long serialVersionUID = 7455678087140912149L;
	public static final int STATUS = HttpStatus.SC_BAD_REQUEST;
	private static final String MSG = "Esta consulta não retorna dado algum!";

	public EmptyDataException(){
		super(MSG, STATUS);
	}
}
