package br.com.sgdw.util.constantes;

public enum CollectionConfVariables {

	COLLECTION_NAME("collectorDataset"),
	COLLECTION_ID_NAME("idSource"),
	COLLECTION_QUERY("query"),
	COLLECTION_NEXT_UPDATE("nextUpdate"),
	COLLECTION_AUTOR("creator"),
	COLLECTION_UPDATE_FREQUENCY("frequency"),
	COLLECTION_LAST_VERSION("lastVersion"),
	COLLECTION_KEYWORDS("keywords"),
	COLLECTION_PUBLISHER("publisher"),
	COLLECTION_CONTACT_POINT("contactPoint"),
	COLLECTION_PERIOD("period"),
	COLLECTION_SPATIAL_COVERAGE("spatialCoverage"),
	COLLECTION_THEME("theme"),
	COLLECTION_LANGUAGE("language"),
	COLLECTION_DATETIME_FORMATS("datetimeFormats"),
	COLLECTION_DESCRIPTION("description"),
	COLLECTION_ID_DB("idDb"),
	COLLECTION_LICENSE("license"),
	COLLECTION_IDENTIFIER_URI("identifierUri"),
	COLLECTION_TITLE("datasetTitle"),
	COLLECTION_PRESERVE("preservation"),
	COLLECTION_PRESERVE_DESCRIPTION("presevationDescription"),
	COLLECTION_FILE_SEPARATOR("separator"),
	PRESERVACAO_DEFAULT("Publicado"),
	COLLECTION_CATEGORY("category"),
	COLLECTION_VIEWS("views"),
	DATASET_REAL_NAME("datasetRealName"),
	ATTRIBUTES_DESCRIPTION("attributesDescription");
	
	private String valor;
	
	CollectionConfVariables(String valor) {
		this.valor = valor;
	}
	
	public String valor(){
		return valor;
	}
}
