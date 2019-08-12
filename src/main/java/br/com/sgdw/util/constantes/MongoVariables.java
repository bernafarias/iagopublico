package br.com.sgdw.util.constantes;

public enum MongoVariables {
	
	USUARIO_COLLECTION("users"),
	CONF_COLLECTION("datasets_conf"),
	@Deprecated
	TOKEN_COLLECTION("collector_token"),
	HISTORY_COLLECTION("_history"),
	SOURCE_CONF_COLLECTION("source_conf"),
	SUGESTION_COLLECTION("sugestions"),
	CATEGORY("categories"),
	REFINEMENT_REQUEST("refinement_request"),
	
	ADMIN_LOGIN("admin"),
	ADMIN_PASSWORD("password"),
	ADMIN_NAME("Administrador"),
	
	COLLECTOR_ID("iago_id"),
	LAST_VERSION("last_version"),
	ID_COLLECTION("_id"),
	COLLECTOR_USERNAME("usuario"),
	COLLECTOR_PASSWORD("senha"),
	VERSION("version"),
	REFINEMENT_DATASET("_ref"),
	
	
	SOURCE_TYPE("sourceType");
	
	private String valor;
	
	MongoVariables(String valor){
		this.valor = valor;
	}
	
	public String valor(){
		return valor;
	}
}
