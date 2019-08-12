package br.com.sgdw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sgdw.domain.Metadata;
import br.com.sgdw.domain.dto.PreservarDataset;
import br.com.sgdw.repository.archive.ArchiveRep;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.service.PreservationServ;
import br.com.sgdw.service.exception.DatasetNotFoundException;
import br.com.sgdw.util.constantes.CollectionConfVariables;
import br.com.sgdw.util.constantes.MongoVariables;

@Service
public class PreservationServImpl implements PreservationServ {
	
	@Autowired
	private MongoRep mongoRep;
	
	@Autowired
	private ArchiveRep archiveRep;
		
	@Override
	public void preservarDataset(PreservarDataset preservacao) throws DatasetNotFoundException{
		Metadata metadata = this.mongoRep.getMetadata(preservacao.getDatasetUri());
		
		if(metadata == null) {
			throw new DatasetNotFoundException();
		}
		metadata.setPreservation(preservacao.getType());
		metadata.setPreservationDescription(preservacao.getDescription());
		
		this.mongoRep.updateMetadata(metadata);
		
	}

	@Override
	public Boolean verificarPreservacao(String datasetTitle) throws DatasetNotFoundException{
		Boolean preservado = false; 
		Metadata metadata = this.mongoRep.getMetadata(datasetTitle);
		
		if(metadata == null) {
			throw new DatasetNotFoundException();
		}
	
		if(!metadata.getPreservation().equals(CollectionConfVariables.PRESERVACAO_DEFAULT.valor())) {
			preservado = true;
		}
		return preservado;
	}
	
	@Override
	public String motivoPreservacao(String datasetTitle) throws DatasetNotFoundException{

		Metadata metadata = this.mongoRep.getMetadata(datasetTitle);
		
		if(metadata == null) {
			throw new DatasetNotFoundException();
		}
		
		return metadata.getPreservationDescription();
	}
	
	@Override
	public void removeDataset(String datasetName) throws DatasetNotFoundException{
		Metadata metadata = mongoRep.getMetadata(datasetName);
		
		if(metadata == null) {
			throw new DatasetNotFoundException();
		}
		
		this.mongoRep.removeCollection(datasetName);
		this.mongoRep.removeCollection(datasetName+MongoVariables.HISTORY_COLLECTION.valor());
		this.mongoRep.removeResister(MongoVariables.CONF_COLLECTION.valor(), CollectionConfVariables.COLLECTION_TITLE.valor(),
				datasetName);
		this.archiveRep.removeFolder(datasetName);
	}
}
