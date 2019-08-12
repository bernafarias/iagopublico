package br.com.sgdw.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jcraft.jsch.SftpException;

import br.com.sgdw.domain.Category;
import br.com.sgdw.domain.Metadata;
import br.com.sgdw.domain.dto.Contact;
import br.com.sgdw.domain.dto.DatasetQuery;
import br.com.sgdw.domain.dto.DatasetQueryResult;
import br.com.sgdw.repository.archive.ArchiveRep;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.service.AccessServ;
import br.com.sgdw.service.exception.DatasetListNotFoundException;
import br.com.sgdw.service.exception.DatasetNotFoundException;
import br.com.sgdw.service.exception.DatasetPageNotFound;
import br.com.sgdw.util.MailCfg;
import br.com.sgdw.util.constantes.CollectionConfVariables;
import br.com.sgdw.util.constantes.Formats;
import br.com.sgdw.util.constantes.Frequency;
import br.com.sgdw.util.constantes.MongoVariables;

import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class AccessServImpl implements AccessServ{

	static final Logger log = Logger.getLogger(AccessServImpl.class); 

	@Autowired
	private MongoRep mongoRep;

	@Autowired
	private ArchiveRep archiveRep;

	@Autowired
	JavaMailSender mailSender;

	private static final double LIMIT = 10000;
	private static final double AUX = 0.5;


	@Override
	public InputStream getCompleteDataset(String datasetName, String format){
		Formats formatEnum = null;
		InputStream data = null;
		String lastVersion = null;
		String fileName = null;
		String extencao = null;

		Metadata metadata = this.mongoRep.getMetadata(datasetName);

		lastVersion = metadata.getLastVersion();
		fileName = datasetName+"_"+lastVersion;
		formatEnum = Formats.getFormat(format);

		switch(formatEnum){

		case JSON: 
				extencao = ".json";
				break;	

		case XML: 
				extencao = ".xml";
				break;	

		case CSV: 
				extencao = ".csv";
				break;

		default: 
				extencao = ".json";
				break;
		}
		fileName += extencao;

		try {

			data = archiveRep.readArchive(datasetName, fileName);
		} catch (IOException|SftpException e) {
			log.error(e);
		} 
		this.addView(datasetName);

		return data;
	}

	@Override
	public List<Map<String, Object>> getDatasetByPage(String datasetName, String version, Integer page) throws DatasetPageNotFound{
		Metadata metadata = this.mongoRep.getMetadata(datasetName);

		if(metadata == null || page > (Integer) metadata.getPagesNumber() || page < 1) {
			throw new DatasetPageNotFound();
		}
		if(version == null || version.isEmpty()) {
			version = metadata.getLastVersion();
		}

		Integer initialItem = (int) ((page - 1) * LIMIT);
		Integer finalItem = (int) (initialItem + LIMIT);

		return this.mongoRep.getDataPage(datasetName, version, initialItem, finalItem);
	}

	public List<Map<String, Object>> preVisualizeData(String datasetName, String version, Integer limit) throws DatasetNotFoundException{
		Metadata metadata = this.mongoRep.getMetadata(datasetName);

		if(metadata == null) {
			throw new DatasetNotFoundException();
		}
		if(version == null || version.isEmpty()) {
			version = metadata.getLastVersion();
		}
		if(limit > LIMIT) {
			limit = (int) LIMIT;
		}

		return this.mongoRep.getDataPage(datasetName, version, 0, limit);
	}

	@Override
	public String listDatasetsAdmin() throws DatasetListNotFoundException{

		List<Map<String, Object>> lista = this.mongoRep.readCollection(MongoVariables.CONF_COLLECTION.valor(), null);

		if(lista == null || lista.isEmpty()) {
			throw new DatasetListNotFoundException();
		}

		return new Gson().toJson(lista);
	}

	@Override
	public String getById(String collectionName, String id, String format){

		Formats formatEnum = Formats.getFormat(format);

		String retorno = null;

		Map<String, Object> dado = null;

		if(!id.contains("_")){
			Metadata metadata = this.mongoRep.getMetadata(collectionName);
			String version = metadata.getLastVersion();
			dado = this.mongoRep.getById(id+"_"+version, collectionName, MongoVariables.ID_COLLECTION.valor());
		}else{
			dado = this.mongoRep.getById(id, collectionName, MongoVariables.ID_COLLECTION.valor());
		}

		if(dado != null){

			List<Map<String, Object>> listaDados = new ArrayList<>();
			listaDados.add(dado);

			switch (formatEnum) {

			case JSON:
					retorno = getJson(listaDados);
					break;

			case XML:
					retorno = getXml(listaDados, collectionName);	
					break;	

			case CSV:
					retorno = getCsv(listaDados);
					break;

			default:
					retorno = getJson(listaDados);
			}
		}
		return retorno;
	}

	@Override
	public DatasetQueryResult getDatasetByQuery(DatasetQuery query) throws DatasetPageNotFound{

		Metadata metadata = this.mongoRep.getMetadata(query.getDatasetName());
		
		if(metadata == null) {
			throw new DatasetPageNotFound();
		}

		String version = query.getVersion();

		if(version == null || version.isEmpty()) {
			version = metadata.getLastVersion();
		}

		Integer page = query.getPage();
		if(page == null) {
			page = 1;
		}

		Integer count = this.mongoRep.countQueryData(query.getParameters(), query.getDatasetName());
		Integer pages = (int) Math.round((double) count/LIMIT+AUX);

		Integer initialItem = (int) ((page - 1) * LIMIT);
		Integer finalItem = initialItem + query.getLimit();

		List<Map<String, Object>> data = this.mongoRep.getByQuery(query.getParameters(), query.getDatasetName(), initialItem, finalItem, version);

		return new DatasetQueryResult(query.getDatasetName(), pages, count, data, page);
	}

	@Override
	public String getDatasetsNames(){
		List<Map<String, Object>> dados = this.mongoRep.readCollection(MongoVariables.CONF_COLLECTION.valor(), null);
		List<Map<String, Object>> listaDeDatasets = new ArrayList<>();
		String preservado;

		if(dados != null && !dados.isEmpty()){
			for(Map<String, Object> dataset : dados){	
				preservado = dataset.get(CollectionConfVariables.COLLECTION_PRESERVE.valor()).toString();
				if (preservado.equals(CollectionConfVariables.PRESERVACAO_DEFAULT.valor())) {
					Map<String, Object> map = new HashMap<>();
					map.put(CollectionConfVariables.COLLECTION_NAME.valor(), dataset.get(CollectionConfVariables.COLLECTION_NAME.valor()));
					map.put(CollectionConfVariables.COLLECTION_TITLE.valor(), dataset.get(CollectionConfVariables.COLLECTION_TITLE.valor()));
					map.put(CollectionConfVariables.COLLECTION_DESCRIPTION.valor(), dataset.get(CollectionConfVariables.COLLECTION_DESCRIPTION.valor()));
					map.put(CollectionConfVariables.COLLECTION_IDENTIFIER_URI.valor(), dataset.get(CollectionConfVariables.COLLECTION_IDENTIFIER_URI.valor()));
					map.put(CollectionConfVariables.COLLECTION_CATEGORY.valor(), dataset.get(CollectionConfVariables.COLLECTION_CATEGORY.valor()));
					map.put(CollectionConfVariables.COLLECTION_KEYWORDS.valor(), dataset.get(CollectionConfVariables.COLLECTION_KEYWORDS.valor()));
					map.put(CollectionConfVariables.COLLECTION_PUBLISHER.valor(), dataset.get(CollectionConfVariables.COLLECTION_PUBLISHER.valor()));
					map.put(CollectionConfVariables.COLLECTION_VIEWS.valor(), dataset.get(CollectionConfVariables.COLLECTION_VIEWS.valor()));
					map.put(CollectionConfVariables.DATASET_REAL_NAME.valor(), dataset.get(CollectionConfVariables.DATASET_REAL_NAME.valor()));
					listaDeDatasets.add(map);
				}
			}
		}

		return new Gson().toJson(listaDeDatasets);
	}
	/**
	 * @deprecated(Reescrever)
	 */
	@Deprecated
	@Override
	public Map<String, Object> getMetadata(String collectionName) throws DatasetNotFoundException{

		Map<String, Object> collection = this.mongoRep.getById(collectionName, MongoVariables.CONF_COLLECTION.valor(), 
				CollectionConfVariables.COLLECTION_TITLE.valor());

		if(collection != null && !collection.isEmpty()){
			collection.remove(CollectionConfVariables.COLLECTION_QUERY.valor());
			collection.remove(CollectionConfVariables.COLLECTION_ID_DB.valor());
			collection.remove(CollectionConfVariables.COLLECTION_ID_NAME.valor());
			collection.remove(MongoVariables.ID_COLLECTION.valor());
			collection.remove(CollectionConfVariables.COLLECTION_NAME.valor());
			String preservacao = Frequency.getFrequency(collection.get(CollectionConfVariables.COLLECTION_UPDATE_FREQUENCY.valor()).toString()).toString();
			if (preservacao == CollectionConfVariables.PRESERVACAO_DEFAULT.valor()){
				collection.remove(CollectionConfVariables.COLLECTION_PRESERVE_DESCRIPTION.valor());
				collection.remove(CollectionConfVariables.COLLECTION_PRESERVE.valor());			
			}					
		}else{
			throw new DatasetNotFoundException();
		}

		return collection;
	}

	@Override
	public Map<String, Object> getMetadataDcat(String datasetName){
		Metadata metadata = this.mongoRep.getMetadata(datasetName);		
		return Metadata.getDcat(metadata);
	}

	@Override
	public List<Category> listCategories(){	
		return this.mongoRep.listCategories();
	}

	@Override
	public List<Map<String, Object>> listFormats(){
		return Formats.getListaFormats();
	}

	public String makeUrl() {        
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
		return servletRequest.toString();
	}

	@Override
	public List<String> getDatasetFiels(String datasetName, String version) throws DatasetNotFoundException{

		List<String> atributos = new ArrayList<>();
		
		try {
			Map<String, Object> dados = this.mongoRep.getByQuery(null, datasetName, 0, 1, version).get(0);

			atributos = new ArrayList<>();

			for(Entry<String, Object> e : dados.entrySet()){
				atributos.add(e.getKey());
			}
		}catch (NullPointerException | IndexOutOfBoundsException e) {
			log.error(e);
			throw new DatasetNotFoundException();
		}


		return atributos;
	}

	@Override
	public void sendContact(Contact contact) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(MailCfg.getToEmail());
		message.setSubject(contact.getSubject());
		message.setText(contact.getMsg());
		this.mailSender.send(message);
	}

	private String getJson(List<Map<String, Object>> dados){

		List<JsonObject> listaJson = new ArrayList<>();
		JsonObject obj = null;

		for(Map<String, Object> map : dados){
			obj = new JsonObject();
			for(Entry<String, Object> entry : map.entrySet()){
				String[] coluna = entry.getKey().split(":");
				obj.addProperty(coluna[0], entry.getValue().toString());
			}
			listaJson.add(obj);
		}

		return new Gson().toJson(listaJson);
	}

	private String getXml(List<Map<String, Object>> dados, String collectionName){
		Element collection = new Element("XML");
		Document doc = new Document(collection);
		doc.setRootElement(collection);

		for(Map<String, Object> map : dados){
			Element elemento = new Element(collectionName);

			for(Entry<String, Object> entry : map.entrySet()){
				String[] coluna = entry.getKey().split(":");
				elemento.addContent(new Element(coluna[0]).setText(entry.getValue()+""));
			}
			doc.getRootElement().addContent(elemento);
		}

		XMLOutputter xmlOutputter = new XMLOutputter();
		xmlOutputter.setFormat(Format.getPrettyFormat());

		return xmlOutputter.outputString(doc);
	}

	private String getCsv(List<Map<String, Object>> dados){
		String delimiter = ";";
		String lineSeparator = "\n";

		StringBuilder csv = new StringBuilder();

		Map<String, Object> firstElement = dados.get(0);

		for(Entry<String, Object> entry : firstElement.entrySet()){
			String[] coluna = entry.getKey().split(":");
			csv.append(coluna[0]);
			csv.append(delimiter);
		}

		csv.append(lineSeparator);

		for(Map<String, Object> map : dados){
			for(Entry<String, Object> entry : map.entrySet()){
				csv.append(entry.getValue()+"");
				csv.append(delimiter);
			}
			csv.append(lineSeparator);
		}
		return csv.toString();

	}

	private void addView(String datasetName){
		Metadata metadata = this.mongoRep.getMetadata(datasetName);
		metadata.setViews(metadata.getViews()+1);
		this.mongoRep.updateMetadata(metadata);
	}
}


