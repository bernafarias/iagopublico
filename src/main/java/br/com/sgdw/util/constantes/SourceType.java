package br.com.sgdw.util.constantes;

public enum SourceType {

	RELACIONAL_DATABASE(0),
	NONRELACIONAL_DATABASE(1),
	FILE(2),
	EXTERNAL_API(3);
	
	private Integer valor;
	
	SourceType(Integer valor){
		this.valor = valor;
	}
	
	public Integer valor(){
		return this.valor;
	}
	
	public static SourceType getSourceType(String st){
		
		switch(st){

		case "RELACIONAL_DATABASE":
				return SourceType.RELACIONAL_DATABASE;

		case "FILE":
				return SourceType.FILE;

		default:
				return null;
		}
	}
	
}
