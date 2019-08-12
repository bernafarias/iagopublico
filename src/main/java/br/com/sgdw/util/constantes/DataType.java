package br.com.sgdw.util.constantes;

public enum DataType {

	NUMBER(0),
	TEXT(1),
	DATE_AND_TIME(2),
	DATE(3),
	TIME(4),
	LOCATION(5),
	BOOLEAN(6);
	
	private Integer valor;
	
	DataType(Integer valor){
		this.valor = valor;
	}
	
	public Integer valor(){
		return this.valor;
	}
}
