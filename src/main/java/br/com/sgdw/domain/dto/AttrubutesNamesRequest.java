package br.com.sgdw.domain.dto;

import org.hibernate.validator.constraints.NotBlank;

public class AttrubutesNamesRequest {

	@NotBlank
	private String datasetName;
	
	@NotBlank
	private String version;
	
	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
}
