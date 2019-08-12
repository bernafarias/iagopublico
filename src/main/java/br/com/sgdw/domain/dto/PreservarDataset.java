package br.com.sgdw.domain.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 * @author Lairson
 * @deprecated(refatorar classe)
 *
 */
@Deprecated
public class PreservarDataset{

	@NotNull
	@NotEmpty
	private String datasetUri;
	
	@NotNull
	@NotEmpty
	private String type;

	@NotNull
	@NotEmpty
	private String description;
	
	
	public String getDatasetUri() {
		return datasetUri;
	}

	public void setDatasetUri(String datasetUri) {
		this.datasetUri = datasetUri;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	
	
}
