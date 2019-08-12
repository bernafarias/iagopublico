package br.com.sgdw.domain.dto;

import org.hibernate.validator.constraints.NotBlank;

import br.com.sgdw.util.constantes.Frequency;

public class NovoDataset {
	
	@NotBlank
	private String datasetTitle;
	
	@NotBlank
	private String datasetRealName;
	
	@NotBlank
	private String collectionIdColumnName;
	
	@NotBlank
	private String queryData;
	
	private Frequency updateFrequency;
	
	@NotBlank
	private String creator;
	
	private String idCollection;
	
	@NotBlank
	private String keywords;
	
	@NotBlank
	private String publisher;
	
	@NotBlank
	private String contactPoint;
	
	@NotBlank
	private String period;
	
	@NotBlank
	private String spatialCoverage;
	
	@NotBlank
	private String language;
	
	@NotBlank
	private String dateTimeFormats = "YYYYMMDDHHMMSS";
	
	@NotBlank
	private String description;
	
	@NotBlank
	private String idSource;
	
	@NotBlank
	private String license;
	
	@NotBlank
	private String categoryName;
	
	@NotBlank
	private String descriptionLine;
	
	private String separator; // em caso de ARQUIVO
	
	public String getDatasetTitle() {
		return datasetTitle;
	}

	public void setDatasetTitle(String datasetTitle) {
		this.datasetTitle = datasetTitle;
	}
	
	public String getDatasetRealName() {
		return datasetRealName;
	}

	public void setDatasetRealName(String datasetRealName) {
		this.datasetRealName = datasetRealName;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getContactPoint() {
		return contactPoint;
	}

	public void setContactPoint(String contactPoint) {
		this.contactPoint = contactPoint;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getSpatialCoverage() {
		return spatialCoverage;
	}

	public void setSpatialCoverage(String spatialCoverage) {
		this.spatialCoverage = spatialCoverage;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDateTimeFormats() {
		return dateTimeFormats;
	}

	public void setDateTimeFormats(String dateTimeFormats) {
		this.dateTimeFormats = dateTimeFormats;
	}

	public String getCollectionIdColumnName() {
		return collectionIdColumnName;
	}

	public void setCollectionIdColumnName(String collectionIdColumnName) {
		this.collectionIdColumnName = collectionIdColumnName;
	}

	public String getQueryData() {
		return queryData;
	}

	public void setQueryData(String queryData) {
		this.queryData = queryData;
	}

	public Frequency getUpdateFrequency() {
		return updateFrequency;
	}

	public void setUpdateFrequency(Frequency updateFrequency) {
		this.updateFrequency = updateFrequency;
	}

	public String getIdCollection() {
		return idCollection;
	}

	public void setIdCollection(String idCollection) {
		this.idCollection = idCollection;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdSource() {
		return idSource;
	}

	public void setIdSource(String idDatabase) {
		this.idSource = idDatabase;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getSeparator() {
		
		if(separator == null){
			return "";
		}
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public String getDescriptionLine() {
		return descriptionLine;
	}

	public void setDescriptionLine(String descriptionLine) {
		this.descriptionLine = descriptionLine;
	}
	
}
