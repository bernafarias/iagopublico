package br.com.sgdw.util.constantes;

public enum CollectorUserVariables {

	COLLECTOR_COLLECTION_NAME("users"),
	COLLECTOR_LOGIN("usuario"),
	COLLECTOR_PASSWORD("senha"),
	COLLECTOR_USERNAME("nome");
	
	private String valor;
	
	CollectorUserVariables(String valor) {
		this.valor = valor;
	}
	
	public String valor(){
		return valor;
	}
}
