package br.com.sgdw.domain.dto;

import java.util.List;
import java.util.Map;

public class DatasetQueryResult {

	private String datasetName;
	
	private Integer pagesNumber;
	
	private Integer rowsNumber;
	
	private List<Map<String, Object>> data;
	
	private Integer atualPage;
	
	public DatasetQueryResult(String datasetName, Integer pagesNumber, Integer rowsNumber,
							  	List<Map<String, Object>> data, Integer atualPage){
		this.datasetName = datasetName;
		this.pagesNumber = pagesNumber;
		this.rowsNumber = rowsNumber;
		this.data = data;
		this.atualPage = atualPage;
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public Integer getPagesNumber() {
		return pagesNumber;
	}

	public void setPagesNumber(Integer pagesNumber) {
		this.pagesNumber = pagesNumber;
	}

	public Integer getRowsNumber() {
		return rowsNumber;
	}

	public void setRowsNumber(Integer rowsNumber) {
		this.rowsNumber = rowsNumber;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}

	public Integer getAtualPage() {
		return atualPage;
	}

	public void setAtualPage(Integer atualPage) {
		this.atualPage = atualPage;
	}
	
}
