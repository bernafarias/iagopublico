package br.com.sgdw.util.constantes;

public enum SugestionStatus {

	EVALUATION(1),
	ACCEPTED(2),
	NOT_ACCEPTED(3);
	
	private Integer valor;
	
	SugestionStatus(Integer valor){
		this.valor = valor;
	}
	
	public Integer getValor(){
		return this.valor;
	}
}
