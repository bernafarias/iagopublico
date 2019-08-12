package br.com.sgdw.service.runnable;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.scheduling.annotation.Async;

import com.google.gson.JsonObject;
import com.jcraft.jsch.SftpException;

import br.com.sgdw.repository.archive.ArchiveRep;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.util.constantes.CollectionConfVariables;
import br.com.sgdw.util.constantes.MongoVariables;
import br.com.sgdw.util.constantes.SystemMsg;

public class DataTransformer implements Runnable{
	
	static final Logger log = Logger.getLogger(DataTransformer.class); 

	private MongoRep mongoRep;
	private ArchiveRep archiveRep;
	
	private String datasetName;
	private String version;
	private Integer countLinhas;
	private Integer contador = 0;
	
	private List<Map<String, Object>> dados;
	
	private List<String> filenames;
	
	
	private Document doc;
	
	private List<JsonObject> listJson;
	
	private Map<String, Object> firstElement;
	private StringBuilder csv;
	private String delimiter = ";";
	private String lineSeparator = "\n";
	private static final Integer ROWLIMIT = 100;
	
	public DataTransformer(String datasetName, MongoRep rep, ArchiveRep archiveRep) {
		super();
		this.datasetName = datasetName;
		this.mongoRep = rep;
		this.archiveRep = archiveRep;
	}

	@Override
	@Async
	public void run() {
		
		this.filenames = new ArrayList<>();
		
		Map<String, Object> conf = this.mongoRep.getById(this.datasetName, MongoVariables.CONF_COLLECTION.valor(), 
				CollectionConfVariables.COLLECTION_NAME.valor());
		
		this.version = conf.get(CollectionConfVariables.COLLECTION_LAST_VERSION.valor()).toString();
		this.countLinhas = this.mongoRep.countData(this.datasetName, this.version);
		
		this.loadCSV();
		this.loadJSON();
		this.loadXML();
		
		try {
			this.archiveRep.writeArchive(this.datasetName, this.filenames);
		} catch (SftpException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(SystemMsg.WRITE_FILE_ERROR.valor(), e);
		}
	}
	
	private void loadXML(){	
		this.contador = 0;
		this.doc = new Document();
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		
		
		String xmlString;
		String filename = this.datasetName+"_"+this.version+".xml";
		
		this.archiveRep.createTempFiles(filename, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		this.archiveRep.createTempFiles(filename,"<XML>");
		
		do{
			log.info("Carregando XML...");
			
			this.dados = this.mongoRep.getDataPage(this.datasetName, this.version, this.contador, ROWLIMIT);
			
			this.doc.setRootElement(new Element("XML"));
			for(Map<String, Object> map : this.dados){
				Element element = new Element(this.datasetName);
				
				for(Entry<String, Object> entry : map.entrySet()){
					element.addContent(new Element(entry.getKey()).setText(entry.getValue().toString()));
				}
				this.doc.getRootElement().addContent(element);
			}
			xmlString = xmlOutputter.outputString(this.doc);
			xmlString = xmlString.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
			xmlString = xmlString.replace("<XML>", "");
			xmlString = xmlString.replace("</XML>", "");
						
			this.archiveRep.createTempFiles(filename, xmlString);
			this.contador +=ROWLIMIT;
			this.doc.removeContent();
		}while(this.contador <= this.countLinhas);
		
		this.archiveRep.createTempFiles(filename, "</XML>");
		this.filenames.add(filename);
	}
		
	private void loadJSON(){
		this.contador = 0;
		
		this.initJSON();
		JsonObject obj = null;
		String jsonString = "";
		Boolean isFirstElement = true;
		
		String filename = this.datasetName+"_"+this.version+".json";
		
		this.archiveRep.createTempFiles(filename, "[");
		do{
			log.info("Carregando JSON...");
					
			this.dados = this.mongoRep.getDataPage(this.datasetName, this.version, this.contador, ROWLIMIT);
			
			this.listJson = new ArrayList<>();
			for(Map<String, Object> map : this.dados){
				
				obj = new JsonObject();
				for(Entry<String, Object> entry : map.entrySet()){
					String[] coluna = entry.getKey().split(":");
					obj.addProperty(coluna[0], entry.getValue().toString());
				}
				this.listJson.add(obj);
			}
			
			if(!this.listJson.isEmpty()){
				jsonString = this.listJson.toString();	
				jsonString = jsonString.replace("[", "");
				jsonString = jsonString.replace("]", "");
				
				if(isFirstElement){
					isFirstElement = false;
				}else{
					jsonString = ", "+ jsonString;
				}
			}
			this.contador +=ROWLIMIT;
			this.archiveRep.createTempFiles(filename, jsonString);
		}
		while(this.contador < this.countLinhas);
		
		this.archiveRep.createTempFiles(filename, "]");
		
		this.filenames.add(filename);
	}
	
	private void loadCSV(){
		this.contador = 0;
		
		String filename = this.datasetName+"_"+this.version+".csv";
		
		do{
			log.info("Carregando CSV...");
			
			this.dados = this.mongoRep.getDataPage(this.datasetName, this.version, this.contador, ROWLIMIT);
			this.csv = new StringBuilder();
			
			if(this.firstElement == null){
				this.firstElement = dados.get(0);
				
				for(Entry<String, Object> entry : firstElement.entrySet()){
					String[] coluna = entry.getKey().split(":");
					this.csv.append(coluna[0]);
					this.csv.append(this.delimiter);
				}
				this.csv.append(this.lineSeparator);
			}
			
			for(Map<String, Object> map : this.dados){
				for(Entry<String, Object> entry : map.entrySet()){
					this.csv.append(entry.getValue()+"");
					this.csv.append(this.delimiter);
				}
				this.csv.append(this.lineSeparator);
			}
			
			this.contador += ROWLIMIT;
			
			this.archiveRep.createTempFiles(filename, this.csv.toString());
		}while(this.contador <= this.countLinhas);
		
		this.filenames.add(filename);
	}
		
	private void initJSON(){
		if(this.listJson == null){
			this.listJson = new ArrayList<>();
		}
	}
}
