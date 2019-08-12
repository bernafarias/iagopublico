package br.com.sgdw.domain.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NovaCollection {

	private String collectionName;
	
	private String idName;
	
	private List<Map<String, Object>> dados;
	
	private String codVersa; 
	
	public NovaCollection(String collectionName, String idName, List<Map<String, Object>> dados, String codVersa){
		this.collectionName = collectionName;
		this.idName = idName;
		this.dados = new ArrayList<>();
		this.dados.addAll(dados);
		this.codVersa = codVersa;
	}
	
	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public List<Map<String, Object>> getDados() {
		List<Map<String, Object>> retorno = new ArrayList<>();
		retorno.addAll(this.dados);	
		return retorno;
	}

	public void setDados(List<Map<String, Object>> dados) {
		this.dados = new ArrayList<>();
		this.dados.addAll(dados);
	}

	public String getCodVersa() {
		return codVersa;
	}

	public void setCodVersa(String codVersa) {
		this.codVersa = codVersa;
	}
	
}
