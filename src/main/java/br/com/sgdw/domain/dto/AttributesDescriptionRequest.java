package br.com.sgdw.domain.dto;

import java.util.Map;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.sgdw.domain.FieldDescription;

public class AttributesDescriptionRequest {

	@NotBlank
	private String datasetName;
	
	@NotEmpty
	private Map<String, FieldDescription> attributesDescription;
	
	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public Map<String, FieldDescription> getAttributesDescription() {
		return attributesDescription;
	}

	public void setAttributesDescription(Map<String, FieldDescription> attributesDescription) {
		this.attributesDescription = attributesDescription;
	}
}
