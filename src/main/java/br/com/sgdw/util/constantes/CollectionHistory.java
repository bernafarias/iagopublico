package br.com.sgdw.util.constantes;

public enum CollectionHistory {
	
	ID("_id"),
	DATASET_NAME("dataset_name"),
	VERSION("version"),
	DATE("date"),
	DESCRIPTION("description"),
	MOTIVO("motivo");
	
	private String valor;
	
	CollectionHistory(String valor) {
		this.valor = valor;
	}
	
	public String valor(){
		return valor;
	}
	
}
