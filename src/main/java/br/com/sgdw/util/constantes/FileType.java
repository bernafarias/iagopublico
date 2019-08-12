package br.com.sgdw.util.constantes;

public enum FileType {

	CSV(0);
	
	private Integer valor;
	
	FileType(Integer valor){
		this.valor = valor;
	}
	
	public Integer valor(){
		return this.valor;
	}
}
