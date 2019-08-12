package br.com.sgdw.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sgdw.domain.FileSource;
import br.com.sgdw.domain.dto.SourceConfig;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.repository.source.FileRep;
import br.com.sgdw.repository.source.SQLRep;
import br.com.sgdw.service.DataSourceServ;
import br.com.sgdw.service.exception.SourceConnectionException;
import br.com.sgdw.service.exception.SourceListNotFound;
import br.com.sgdw.util.constantes.CollectionSourceConf;
import br.com.sgdw.util.constantes.MongoVariables;
import br.com.sgdw.util.constantes.SGBDs;

@Service
public class DataSourceServImpl implements DataSourceServ {

	@Autowired
	private MongoRep mongoRep;
	
	@Autowired
	private SQLRep sqlRep;
	
	@Autowired
	private FileRep fileRep;
	
	@Override
	public void createSourceConfig(SourceConfig cfg) throws SourceConnectionException{	
		
		switch (cfg.getSourceType()) {

		case RELACIONAL_DATABASE:
				createSqlSource(cfg);
				break;

		case FILE:
				createFileOrigin(cfg);
				break;                   
				
		default:
				break;
		}
		
	}
	
	private void createSqlSource(SourceConfig cfg) throws SourceConnectionException{
		if(!this.sqlRep.checarConexaoSQL(cfg)) {
			throw new SourceConnectionException();
		}
			
		cfg.setId(getSourceNewId());
		this.mongoRep.insertObj(cfg, MongoVariables.SOURCE_CONF_COLLECTION.valor());

	}
	
	private void createFileOrigin(SourceConfig cfg) throws SourceConnectionException{
		if(!this.fileRep.checkFileConnection(cfg)) {
			throw new SourceConnectionException();
		}
			
		FileSource fileSource = new FileSource(cfg);
		fileSource.setId(getSourceNewId());
		this.mongoRep.insertObj(fileSource, MongoVariables.SOURCE_CONF_COLLECTION.valor());

	}
	
	private String getSourceNewId(){
		return this.getNextSequence(MongoVariables.SOURCE_CONF_COLLECTION.valor()).toString();
	}

	@Override
	public List<Map<String, Object>> listSGBDs(){
		return SGBDs.getListaSGBDs();
	}	
	
	@Override
	public List<Map<String, Object>> lisRepSGBDs() throws SourceListNotFound{
		List<Map<String, Object>> dados = this.mongoRep.readCollection(MongoVariables.SOURCE_CONF_COLLECTION.valor(), null);
		List<Map<String, Object>> listaDeSGBDs = new ArrayList<>();

		if(dados == null) {
			throw new SourceListNotFound();
		}
			
		for(Map<String, Object> sgbd : dados){
			Map<String, Object> map = new HashMap<>();
			map.put(CollectionSourceConf.ID.valor(), sgbd.get(CollectionSourceConf.ID.valor()));
			map.put(CollectionSourceConf.SOURCE_IDNAME.valor(), sgbd.get(CollectionSourceConf.SOURCE_IDNAME.valor()));
			map.put(CollectionSourceConf.URL.valor(), sgbd.get(CollectionSourceConf.URL.valor()));
			map.put(CollectionSourceConf.SOURCE_TYPE.valor(), sgbd.get(CollectionSourceConf.SOURCE_TYPE.valor()));
			listaDeSGBDs.add(map);
		}
		return listaDeSGBDs;
	}
	

	
	private Integer getNextSequence(String collectionName){
		return this.mongoRep.countData(collectionName, null) + 1;
	}
}
