package br.com.sgdw.util.constantes;

public enum CollectionSourceConf {

	SOURCE_IDNAME("sourceIdName"),
	ID("_id"),
	URL("url"),
	SOURCE_TYPE("sourceType");
	
	private String valor;
	
	CollectionSourceConf(String valor) {
		this.valor = valor;
	}
	
	public String valor(){
		return valor;
	}
}
