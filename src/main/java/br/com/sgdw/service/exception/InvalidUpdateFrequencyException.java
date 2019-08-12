package br.com.sgdw.service.exception;

public class InvalidUpdateFrequencyException extends InvalidValueException{

	/**
	 * @author Wilker Santos
	 * 
	 * Exceção para inserção de Frequencia de atualização do conjunto de dados inválida. Ver "Frequency.java"
	 */
	private static final long serialVersionUID = 8483500946654926301L;
	private static final String MSG = "Frequencia de atualização inválida!! Utilize os valores: \n"
									+ "0 - Por hora\n"
									+ "1 - Diário\n"
									+ "2 - Semanal\n"
									+ "3 - Mensal\n"
									+ "4 - Semestral\n"
									+ "5 - Anual";
	
	public InvalidUpdateFrequencyException (){
		super(MSG);
	}

}
