package br.com.sgdw.domain.dto;

public class NovaVersao {
	
	private String collectionName;
	
	private String data;
	
	private String description;
	
	private String version;
	
	private String novaDataAtualizacao;
	
	private String query;
	
	private String motivo;
	
	public NovaVersao(String collectionName, String data, String description, String version, String novaDataAtualizacao, 
			String query, String motivo){
		this.collectionName = collectionName;
		this.data = data;
		this.description = description;
		this.version = version;
		this.novaDataAtualizacao = novaDataAtualizacao;
		this.query = query;
		this.motivo = motivo;
	}
	
	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getNovaDataAtualizacao() {
		return novaDataAtualizacao;
	}

	public void setNovaDataAtualizacao(String novaDataAtualizacao) {
		this.novaDataAtualizacao = novaDataAtualizacao;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
	
	
}
