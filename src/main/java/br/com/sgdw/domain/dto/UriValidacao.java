package br.com.sgdw.domain.dto;

import org.hibernate.validator.constraints.NotBlank;

public class UriValidacao {
	
	@NotBlank
	private String uri;
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	
}
