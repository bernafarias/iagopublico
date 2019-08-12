package br.com.sgdw.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sgdw.domain.Category;
import br.com.sgdw.domain.FieldDescription;
import br.com.sgdw.domain.History;
import br.com.sgdw.domain.Metadata;
import br.com.sgdw.domain.dto.AttributesDescriptionRequest;
import br.com.sgdw.domain.dto.NovaCollection;
import br.com.sgdw.domain.dto.NovoDataset;
import br.com.sgdw.domain.dto.SourceConfig;
import br.com.sgdw.repository.archive.ArchiveRep;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.repository.source.FileRep;
import br.com.sgdw.repository.source.SQLRep;
import br.com.sgdw.service.AccessServ;
import br.com.sgdw.service.CreateServ;
import br.com.sgdw.service.UpdateServ;
import br.com.sgdw.service.exception.CategoryAlreadyExistsException;
import br.com.sgdw.service.exception.CategoryNotFoundException;
import br.com.sgdw.service.exception.DatasetAlreadyExistsException;
import br.com.sgdw.service.exception.DatasetNotFoundException;
import br.com.sgdw.service.exception.EmptyDataException;
import br.com.sgdw.service.runnable.DataTransformer;
import br.com.sgdw.util.constantes.CollectionConfVariables;
import br.com.sgdw.util.constantes.MongoVariables;
import br.com.sgdw.util.constantes.SystemMsg;

@Service
public class CreateServImpl implements CreateServ{
	
	static final Logger log = Logger.getLogger(CreateServImpl.class); 
	
	@Autowired
	private MongoRep mongoRep;
	
	@Autowired
	private SQLRep sqlRep;
	
	@Autowired
	private FileRep fileRep;
	
	@Autowired
	private UpdateServ updateServ;
	
	@Autowired
	private AccessServ accessServ;
	
	@Autowired
	private ArchiveRep archiveRep;
	
	private static final int SOMA = 500;
	
	@Override
	public void createDataset(NovoDataset novo) throws DatasetAlreadyExistsException, CategoryNotFoundException{
		if(this.mongoRep.exists(novo.getDatasetTitle(), MongoVariables.CONF_COLLECTION.valor(), CollectionConfVariables.COLLECTION_TITLE.valor())) {
			throw new DatasetAlreadyExistsException();
		}
		if(!this.mongoRep.exists(novo.getCategoryName(), MongoVariables.CATEGORY.valor(), "name")) {
			throw new CategoryNotFoundException();
		}
		
		SourceConfig cfg = this.mongoRep.getDbConfig(novo.getIdSource());

		try {
			switch (cfg.getSourceType()) {

			case RELACIONAL_DATABASE:
					createDatasetFromSQL(cfg, novo);
					break;	

			case FILE:
					createDatasetFromFile(cfg, novo);
					break;
					
			default:
					break;
			}
		}catch (EmptyDataException e) {
			log.error(e);
		}
	
		new Thread(new DataTransformer(novo.getDatasetTitle(), this.mongoRep, this.archiveRep)).start();
	}
	
	private void createDatasetFromSQL(SourceConfig cfg, NovoDataset novo) throws EmptyDataException{
		Integer numeroDeLinhas = this.sqlRep.contarLinhas(novo.getQueryData(), cfg);

		if(numeroDeLinhas == 0) {
			throw new EmptyDataException();
		}
		
		Integer itemInicial = 0;	
		List<Map<String, Object>> dados = null;
		NovaCollection newCollection;
		String codVersa = this.getCodVersao();

		do{
			dados = this.sqlRep.executarSql(novo.getQueryData(), itemInicial, cfg);
			newCollection = new NovaCollection(novo.getDatasetTitle(), novo.getCollectionIdColumnName(), dados, codVersa);
			this.insertCollection(newCollection);

			itemInicial += SOMA;
		}
		while(itemInicial <= numeroDeLinhas+1);

		createMetadata(novo, codVersa);

	}
	
	private void createDatasetFromFile(SourceConfig cfg, NovoDataset novo){
		String path = cfg.getUrl()+"/"+novo.getQueryData();
			
		NovaCollection novaCollection;
		String codVersa = getCodVersao();
		List<Map<String, Object>> dados = null;
		Integer startPoint = 1;

		dados = this.fileRep.getDataFromFile(path, startPoint, novo.getSeparator());
		novaCollection = new NovaCollection(novo.getDatasetTitle(), novo.getCollectionIdColumnName(), dados, codVersa);
		this.insertCollection(novaCollection);

		createMetadata(novo, codVersa);
	}
	
	private void createMetadata(NovoDataset novo, String codVersa){
		Integer rowsNum = this.mongoRep.countData(novo.getDatasetTitle(), codVersa);
		
		Metadata newMetadata = new Metadata(novo, codVersa, this.updateServ.getAtualizacaoData(novo.getUpdateFrequency()), rowsNum);
		History history = new History(codVersa, SystemMsg.HISTORY_FIRST_INSERT.valor());
		
		this.mongoRep.insertObj(newMetadata, MongoVariables.CONF_COLLECTION.valor());
		this.mongoRep.insertObj(history, novo.getDatasetTitle() + MongoVariables.HISTORY_COLLECTION.valor());
	}
	
	/**
	 * @deprecated(Substituir. Esta função pula algumas regras de negócio)
	 */
	@Deprecated
	@Override
	public void insertCollection(NovaCollection newCollection){
		
		log.info("Atualizando "+newCollection.getCollectionName()+"...");
		String versaAux = null;
		
		for(Map<String, Object> j : newCollection.getDados()){			
			versaAux = j.get(newCollection.getIdName()).toString()+"_"+newCollection.getCodVersa();
			
			j.put(MongoVariables.ID_COLLECTION.valor(), versaAux);
			j.remove(newCollection.getIdName());
			j.put("version", newCollection.getCodVersa());
			
			this.mongoRep.insert(j, newCollection.getCollectionName());
		}
	}
	
	@Override
	public void createCategory(Category category) throws CategoryAlreadyExistsException{
		if(this.mongoRep.exists(category.getName(), MongoVariables.CATEGORY.valor(), "name")) {
			throw new CategoryAlreadyExistsException();
		}
		this.mongoRep.insertObj(category, MongoVariables.CATEGORY.valor());
	}
	
	@Override
	public void insertFieldDescription(AttributesDescriptionRequest descriptionRequest) throws DatasetNotFoundException{
		Metadata metadata = mongoRep.getMetadata(descriptionRequest.getDatasetName());
		List<String> atributos = new ArrayList<>();
		try {
			atributos = this.accessServ.getDatasetFiels(descriptionRequest.getDatasetName(), metadata.getLastVersion()); 
		}catch (NullPointerException e) {
			log.error(e);
			throw new DatasetNotFoundException();
		}
		
		List<FieldDescription> fieldDescription = new ArrayList<>();
		Map<String, FieldDescription> descriptionMap = descriptionRequest.getAttributesDescription();
		
		for(String att : atributos){
			FieldDescription description = descriptionMap.get(att);
			
			if(description != null) {
				fieldDescription.add(description);
			}		
			descriptionMap.remove(att);
		}
		
		metadata.setFieldDescriptions(fieldDescription);
		
		this.mongoRep.updateMetadata(metadata);
	}
	
	private String getCodVersao(){
		Date date = new Date();
		return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
	}
}
