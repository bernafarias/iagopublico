package br.com.sgdw.domain;

import org.hibernate.validator.constraints.NotBlank;

import com.mongodb.DBObject;

public class Category {

	@NotBlank
	private String name;
	
	@NotBlank
	private String description;
	
	@NotBlank
    private String base64Image;
    
	public Category() {
		
	}
	
	public Category(DBObject dbObject){
		this.name = (String) dbObject.get("name");
		this.description = (String) dbObject.get("description");
		this.base64Image = (String) dbObject.get("base64Image");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBase64Image() {
		return base64Image;
	}

	public void setBase64Image(String base64Image) {
		this.base64Image = base64Image;
	}
}
