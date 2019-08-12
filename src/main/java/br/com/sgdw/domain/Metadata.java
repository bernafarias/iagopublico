package br.com.sgdw.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

import br.com.sgdw.domain.dto.NovoDataset;
import br.com.sgdw.util.constantes.CollectionConfVariables;
import br.com.sgdw.util.constantes.Formats;
import br.com.sgdw.util.constantes.Frequency;

public class Metadata {

	private Object id;
	
	private String datasetRealName;
	
	private String creator;
	
	private String period;
	
	private String keywords;
	
	private String query;
	
	private String description;
	
	private String language;
	
	private String separator;
	
	private Frequency frequency;
	
	private String lastVersion;
	
	private String license;
	
	private String preservation;
	
	private String preservationDescription;
	
	private String idDb;
	
	private String datetimeFormats;
	
	private String collectorDataset;
	
	private String idSource;
	
	private String spacialCoverage;
	
	private String publisher;
		
	private String identifierUri;
	
	private String datasetTitle;
	
	private String nextUpdate;
	
	private String contactPoint;
	
	private String category;

	private int views;
	
	private Date creationDate;
	
	private Integer rowsNumber;
	
	private Integer pagesNumber;
	
	private Integer likes;
	
	private Integer dislikes;
	
	private List<FieldDescription> fieldDescriptions;
	
	private static final double MAXNUMBER = 10000;
	private static final double AUX = 0.5; 
	
	private static final String DCTTITLE = "dct:title";
	private static final String DCTDESCRIPTION = "dct:description";
	private static final String DCATDOWNLOADURL = "dcat:downloadURL";
	
	private static final String OPENURL = "/open/";
	
	public Metadata(NovoDataset novo, String codVersion, String nextUpdate, Integer rowsNumber){
		this.id = generateId(novo.getDatasetTitle());
		this.query = novo.getQueryData();
		this.collectorDataset = novo.getDatasetTitle();
		this.datasetRealName = novo.getDatasetRealName();
		this.idSource = novo.getCollectionIdColumnName();
		this.frequency = novo.getUpdateFrequency();
		this.creator = novo.getCreator();
		this.nextUpdate = nextUpdate;
		this.lastVersion = codVersion;
		this.keywords = novo.getKeywords();
		this.publisher = novo.getPublisher();
		this.contactPoint = novo.getContactPoint();
		this.period = novo.getPeriod();
		this.spacialCoverage = novo.getSpatialCoverage();
		this.language = novo.getLanguage();
		this.datetimeFormats = novo.getDateTimeFormats();
		this.description = novo.getDescription();
		this.idDb = novo.getIdSource();
		this.identifierUri = novo.getDatasetTitle();
		this.license = novo.getLicense();
		this.datasetTitle = novo.getDatasetTitle();
		this.preservation = CollectionConfVariables.PRESERVACAO_DEFAULT.valor();
		this.category =novo.getCategoryName();
		this.creationDate = new Date();
		this.rowsNumber = rowsNumber;
		this.pagesNumber = (int) Math.round((double) this.rowsNumber/MAXNUMBER+AUX);
		this.likes = 0;
		this.dislikes= 0;
		
		if(!novo.getSeparator().isEmpty()){
			this.separator = novo.getSeparator();
		}
	}
	
	public static Map<String, Object> getDcat(Metadata metadata){	
		Map<String, Object> map = new HashMap<>();
		map.put("dcterms:title", metadata.getDatasetRealName());
		map.put("dcat:keyword", metadata.getKeywords());
		map.put("dcat:contactPoint", metadata.getContactPoint());
		map.put("dcterms:temporal", metadata.getCreationDate());
		map.put("dcterms:spatial", metadata.getSpacialCoverage());
		map.put("dcterms:publisher", metadata.getPublisher());
		map.put("dcterms:accrualPeriodicity", metadata.getPeriod());
		map.put("dcat:theme", metadata.getCategory());
		map.put("dcterms:language", metadata.getLanguage());
		map.put("dcterms:conformsTo", metadata.getLicense());
		map.put("dcterms:creator", metadata.getCreator());
		
		List<Map<String, Object>> distributions = new ArrayList<>();
		
		Map<String, Object> distribution = new HashMap<>();
		distribution.put(DCTTITLE, Formats.JSON.getValor());
		distribution.put(DCTDESCRIPTION, "Distribuição em JSON do Conjunto de Dados "+metadata.getDatasetRealName());
		distribution.put(DCATDOWNLOADURL, OPENURL +metadata.getDatasetTitle()+"/format/json/download");
		distributions.add(distribution);
		
		distribution = new HashMap<>();
		distribution.put(DCTTITLE, Formats.XML.getValor());
		distribution.put(DCTDESCRIPTION, "Distribuição em XML do Conjunto de Dados "+metadata.getDatasetRealName());
		distribution.put(DCATDOWNLOADURL, OPENURL +metadata.getDatasetTitle()+"/format/xml/download");
		distributions.add(distribution);
		
		distribution = new HashMap<>();
		distribution.put(DCTTITLE, Formats.CSV.getValor());
		distribution.put(DCTDESCRIPTION, "Distribuição em CSV do Conjunto de Dados "+metadata.getDatasetRealName());
		distribution.put(DCATDOWNLOADURL, OPENURL +metadata.getDatasetTitle()+"/format/csv/download");
		distributions.add(distribution);
		
		map.put("dcat:distribution", distributions);
		
		List<Map<String, Object>> fields = new ArrayList<>();
		
		for(FieldDescription desc : metadata.getFieldDescriptions()){
			Map<String, Object> field = new HashMap<>();
			field.put("dct:fieldName", desc.getFieldName());
			field.put(DCTDESCRIPTION, desc.getDescription());
			field.put("dcterms:title", desc.getTitle());
			field.put("dct:dataType", desc.getDataType().valor());
			
			fields.add(field);
		}
		
		map.put("dcat:fieldsDescriptions", fields);
		
		return map;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
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

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public Frequency getFrequency() {
		return frequency;
	}

	public void setFrequency(Frequency frequency) {
		this.frequency = frequency;
	}

	public String getLastVersion() {
		return lastVersion;
	}

	public void setLastVersion(String lastVersion) {
		this.lastVersion = lastVersion;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getPreservation() {
		return preservation;
	}

	public void setPreservation(String preservation) {
		this.preservation = preservation;
	}

	public String getPreservationDescription() {
		return preservationDescription;
	}

	public void setPreservationDescription(String preservationDescription) {
		this.preservationDescription = preservationDescription;
	}

	public String getIdDb() {
		return idDb;
	}

	public void setIdDb(String idDb) {
		this.idDb = idDb;
	}

	public String getDatetimeFormats() {
		return datetimeFormats;
	}

	public void setDatetimeFormats(String datetimeFormats) {
		this.datetimeFormats = datetimeFormats;
	}

	public String getCollectorDataset() {
		return collectorDataset;
	}

	public void setCollectorDataset(String collectorDataset) {
		this.collectorDataset = collectorDataset;
	}

	public String getIdSource() {
		return idSource;
	}

	public void setIdSource(String idSource) {
		this.idSource = idSource;
	}

	public String getSpacialCoverage() {
		return spacialCoverage;
	}

	public void setSpacialCoverage(String spacialCoverage) {
		this.spacialCoverage = spacialCoverage;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getIdentifierUri() {
		return identifierUri;
	}

	public void setIdentifierUri(String identifierUri) {
		this.identifierUri = identifierUri;
	}

	public String getDatasetTitle() {
		return datasetTitle;
	}

	public void setDatasetTitle(String datasetTitle) {
		this.datasetTitle = datasetTitle;
	}

	public String getNextUpdate() {
		return nextUpdate;
	}

	public void setNextUpdate(String nextUpdate) {
		this.nextUpdate = nextUpdate;
	}

	public String getContactPoint() {
		return contactPoint;
	}

	public void setContactPoint(String contactPoint) {
		this.contactPoint = contactPoint;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Integer getRowsNumber() {
		return rowsNumber;
	}

	public void setRowsNumber(Integer rowsNumber) {
		this.rowsNumber = rowsNumber;
	}

	public Integer getPagesNumber() {
		return pagesNumber;
	}

	public void setPagesNumber(Integer pagesNumber) {
		this.pagesNumber = pagesNumber;
	}
	
	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public Integer getDislikes() {
		return dislikes;
	}

	public void setDislikes(Integer dislikes) {
		this.dislikes = dislikes;
	}

	public List<FieldDescription> getFieldDescriptions() {
		return fieldDescriptions;
	}

	public void setFieldDescriptions(List<FieldDescription> fieldDescriptions) {
		this.fieldDescriptions = fieldDescriptions;
	}

	private String generateId(String collectionName){
		Object salt = null;
		MessageDigestPasswordEncoder digestPasswordEncoder = getInstanceMessageDisterPassword();
		return digestPasswordEncoder.encodePassword(collectionName, salt);
	}
	
	private MessageDigestPasswordEncoder getInstanceMessageDisterPassword() {
		return new MessageDigestPasswordEncoder("MD5");
	}
}
