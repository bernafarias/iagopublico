package br.com.sgdw.service.exception;

import org.apache.http.HttpStatus;

public class InvalidSourceTypeException extends IagoException{

	/**
	 * @author Wilker Santos
	 * 
	 * Exceção para inserção de Tipo de fonte de dados inválido. Ver "SourceType.java"
	 */
	private static final long serialVersionUID = 1121541662680922526L;
	public static final int STATUS = HttpStatus.SC_BAD_REQUEST;  
	private static final String MSG = "Tipo de origem de dados inválido!!! Utilize os valores: \n"
									+ "0 - Banco de Dados Relacional\n"
									+ "1 - Banco de Dados Não Relacional\n"
									+ "2 - Arquivo\n"
									+ "3 - API externa";
	
	public InvalidSourceTypeException (){
		super(MSG, STATUS);
	}
}
