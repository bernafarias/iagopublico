package br.com.sgdw.domain.dto;


import org.hibernate.validator.constraints.NotBlank;

import com.mongodb.DBObject;

import br.com.sgdw.util.constantes.SourceType;

public class SourceConfig {
	 
	private String id;
	
	@NotBlank
	private String sourceIdName;
	
	@NotBlank
	private String url;
	
	private String user;

	private String password;
	
	private String bdName;
	
	@NotBlank
	private String description;
	
	private SourceType sourceType;
	
	public SourceConfig(){ }
	
	public SourceConfig(DBObject dbObject) {
		
		this.sourceIdName = (String) dbObject.get("sourceIdName");
		this.url = (String) dbObject.get("url");
		this.user = (String) dbObject.get("user");
		this.password = (String) dbObject.get("password");
		this.sourceType = SourceType.getSourceType((String) dbObject.get("sourceType"));
		this.description = (String) dbObject.get("description");
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSourceIdName() {
		return sourceIdName;
	}

	public void setSourceIdName(String sourceIdName) {
		this.sourceIdName = sourceIdName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBdName() {
		return bdName;
	}

	public void setBdName(String bdName) {
		this.bdName = bdName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}
	
}
