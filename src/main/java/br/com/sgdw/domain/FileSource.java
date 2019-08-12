package br.com.sgdw.domain;

import br.com.sgdw.domain.dto.SourceConfig;
import br.com.sgdw.util.constantes.FileType;
import br.com.sgdw.util.constantes.SourceType;

public class FileSource {

	private String id;
	
	private String url;
	
	private String description;
	
	private SourceType sourceType;
	
	private FileType fileType;
	
	private String sourceIdName;
	
	public FileSource(SourceConfig config){
		this.url = config.getUrl();
		this.description = config.getDescription();
		this.sourceType = config.getSourceType();
		this.fileType = FileType.CSV; 
		this.sourceIdName = config.getSourceIdName();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public FileType getFileType() {
		return fileType;
	}

	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}

	public String getSourceIdName() {
		return sourceIdName;
	}

	public void setSourceIdName(String sourceIdName) {
		this.sourceIdName = sourceIdName;
	}
}
