package br.com.sgdw.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.jcraft.jsch.SftpException;

import br.com.sgdw.service.VersionServ;
import br.com.sgdw.service.exception.DatasetNotFoundException;
import br.com.sgdw.service.runnable.DataTransformer;
import br.com.sgdw.domain.History;
import br.com.sgdw.domain.Metadata;
import br.com.sgdw.domain.dto.NovaVersao;
import br.com.sgdw.repository.archive.ArchiveRep;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.util.constantes.CollectionHistory;
import br.com.sgdw.util.constantes.Formats;
import br.com.sgdw.util.constantes.MongoVariables;
import br.com.sgdw.util.constantes.SystemMsg;

@Service
public class VersionServImpl implements VersionServ{

	static final Logger log = Logger.getLogger(VersionServImpl.class);
	
	@Autowired
	private MongoRep mongoRep;
	
	@Autowired
	private ArchiveRep archiveRep;
	
	@Override
	public String listVersionsDataset(String collectionName){
		
		List<Map<String, Object>> dados = this.mongoRep.readCollection(collectionName + MongoVariables.HISTORY_COLLECTION.valor(), null);
		List<Map<String, Object>> listaDeVersoes = new ArrayList<>();
				
		if(dados != null){
			for(Map<String, Object> versao : dados){
				Map<String, Object> map = new HashMap<>();
				map.put(CollectionHistory.VERSION.valor(), versao.get(CollectionHistory.VERSION.valor()));
				map.put(CollectionHistory.DATE.valor(), versao.get(CollectionHistory.DATE.valor()));
				map.put(CollectionHistory.DESCRIPTION.valor(), versao.get(CollectionHistory.DESCRIPTION.valor()));
				
				listaDeVersoes.add(map);
			}
		}
		
		return new Gson().toJson(listaDeVersoes);
		
	}
	
	@Override
	public InputStream getDatasetByVersion(String datasetName, String version, String format) throws DatasetNotFoundException{
		Metadata metadata = this.mongoRep.getMetadata(datasetName);
		
		if(metadata == null) {
			throw new DatasetNotFoundException();
		}
			
		InputStream data = null;
		
		String filename = datasetName + "_" + version;
		String extencao = null;
		
		Formats formatEnum = Formats.getFormat(format);
		
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
		}
		
		filename+=extencao;
		try {
			data = this.archiveRep.readArchive(datasetName, filename);
		}catch (IOException e) {
			log.error(SystemMsg.ERRO_LEITURA_ARQUIVO.valor(), e);
		}catch (SftpException e) {
			log.error(e);
		}
		
		return data;
	}
	
	
	@Override
	public Map<String, Object> getMetadataVersion(String collectionName, String version){
		Map<String, Object> collection = this.mongoRep.getById(version, collectionName + MongoVariables.HISTORY_COLLECTION.valor(),
				CollectionHistory.VERSION.valor());
		
		if(collection != null){
			collection.remove(CollectionHistory.ID.valor());		
		}
		
		return collection;
	}
	
	/**
	 * @deprecated(Refatorar. Pula algumas regras de negócio)
	 */
	@Override
	@Deprecated
	public void insertNewVersion(NovaVersao newVersion){
		
		History history = new History(newVersion.getVersion(), newVersion.getMotivo());
		
		this.mongoRep.insertObj(history, newVersion.getCollectionName()+MongoVariables.HISTORY_COLLECTION.valor());
		this.mongoRep.updateData(newVersion.getCollectionName(),newVersion.getNovaDataAtualizacao(), newVersion.getVersion(), newVersion.getQuery());
		
		new Thread(new DataTransformer(newVersion.getCollectionName(), this.mongoRep, this.archiveRep)).start();
		
	}
	
	
	
}
