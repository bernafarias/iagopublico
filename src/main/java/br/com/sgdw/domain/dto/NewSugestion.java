package br.com.sgdw.domain.dto;

import org.hibernate.validator.constraints.NotBlank;

public class NewSugestion {

	@NotBlank
	private String name;
	
	@NotBlank
	private String email;
	
	@NotBlank
	private String description;
	
	public NewSugestion() {
	
	}
	
	public NewSugestion(String name, String email, String description) {
		this.name = name;
		this.email = email;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
}
