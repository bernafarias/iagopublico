package br.com.sgdw.service.exception;

public abstract class IagoException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3082881621384546067L;
	private final int status;
	
	public IagoException(String msg, int status) {
		super(msg);
		this.status = status;
	}
	
	public int getStatus(){
		return this.status;
	}
	
}
