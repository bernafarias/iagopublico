package br.com.sgdw.domain;

import java.util.Date;

public class History {

	private String date;
	
	private String description;
	
	private String version;
	
	public History(String codVersion, String description) {
		this.date = new Date().toString();
		this.description = description;
		this.version = codVersion;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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
}
