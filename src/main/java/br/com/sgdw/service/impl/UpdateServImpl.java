package br.com.sgdw.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sgdw.domain.dto.NovaCollection;
import br.com.sgdw.domain.dto.NovaVersao;
import br.com.sgdw.domain.dto.SourceConfig;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.repository.source.FileRep;
import br.com.sgdw.repository.source.SQLRep;
import br.com.sgdw.service.CreateServ;
import br.com.sgdw.service.UpdateServ;
import br.com.sgdw.service.VersionServ;
import br.com.sgdw.util.constantes.CollectionConfVariables;
import br.com.sgdw.util.constantes.Frequency;
import br.com.sgdw.util.constantes.MongoVariables;
import br.com.sgdw.util.constantes.SystemMsg;

@Service
public class UpdateServImpl implements UpdateServ{
	
	static final Logger log = Logger.getLogger(UpdateServImpl.class); 

	@Autowired
	private MongoRep mongoRep;
	
	@Autowired
	private SQLRep sqlRep;
	
	@Autowired
	private FileRep fileRep;
	
	@Autowired
	private CreateServ createServ;
	
	@Autowired
	private VersionServ versionServ;
	
	private static final int SQLLIMIT = 500;
	private static final int SOMASEMESTRE = 6;

	@Override
	public void atualizarDatasetsAuto() throws ParseException{
		List<Map<String, Object>> conf = this.mongoRep.readCollection(MongoVariables.CONF_COLLECTION.valor(), null);
		String collectionName = null;
		String query = null;
		SourceConfig dbConfig;
		String codVersa = this.getCodVersao();
		String novaDataAtualizacao;
		String preservacao;
		
		log.info("Atualizando dados...");
				
		for(Map<String, Object> i : conf){	
			preservacao = i.get(CollectionConfVariables.COLLECTION_PRESERVE.valor()).toString();
			if (preservacao.equals(CollectionConfVariables.PRESERVACAO_DEFAULT.valor())
					&& verificarDataAtualizacao(i.get(CollectionConfVariables.COLLECTION_NEXT_UPDATE.valor()).toString())) {

				log.info("Atualizando "+ i.get(CollectionConfVariables.COLLECTION_NAME.valor())+ "...");

				dbConfig = this.mongoRep.getDbConfig(i.get(CollectionConfVariables.COLLECTION_ID_DB.valor()).toString());
				collectionName = i.get(CollectionConfVariables.COLLECTION_NAME.valor()).toString();

				switch(dbConfig.getSourceType()){

				case RELACIONAL_DATABASE:
						updateSQL(i, dbConfig, codVersa);
						break;

				case FILE:
						updateFile(i, dbConfig, codVersa);
						break;

				default:
						break;
				}

				novaDataAtualizacao = this.getAtualizacaoData(Frequency.getFrequency(i.get(CollectionConfVariables.COLLECTION_UPDATE_FREQUENCY.valor())
						.toString()));
				NovaVersao newVersion = new NovaVersao(collectionName, new Date().toString(), SystemMsg.HISTORY_AUTO_UPDATE.valor(),
						codVersa, novaDataAtualizacao, query, SystemMsg.HISTORY_AUTO_UPDATE.valor()); 
				this.mongoRep.setOldVersion(collectionName);
				this.versionServ.insertNewVersion(newVersion);


			}
		}
	}
	
	private void updateFile(Map<String, Object> conf, SourceConfig dbConfig, String codVersa){
		String filename = conf.get(CollectionConfVariables.COLLECTION_QUERY.valor()).toString();
		String collectionName = conf.get(CollectionConfVariables.COLLECTION_NAME.valor()).toString();
		String separator = conf.get(CollectionConfVariables.COLLECTION_FILE_SEPARATOR.valor()).toString();
		String idName = conf.get(CollectionConfVariables.COLLECTION_ID_NAME.valor()).toString();
		String path = dbConfig.getUrl()+"\\"+filename;
		
		List<Map<String, Object>> dados = this.fileRep.getDataFromFile(path, 0, separator);
		
		NovaCollection novaCollection = new NovaCollection(collectionName, idName, dados, codVersa);
		
		this.createServ.insertCollection(novaCollection);
	}
	
	
	private void updateSQL(Map<String, Object> conf, SourceConfig dbConfig, String codVersa){
		String query = conf.get(CollectionConfVariables.COLLECTION_QUERY.valor()).toString();
		String collectionName = conf.get(CollectionConfVariables.COLLECTION_NAME.valor()).toString();
		String idName = conf.get(CollectionConfVariables.COLLECTION_ID_NAME.valor()).toString();
			
		Integer numLinhas = this.sqlRep.contarLinhas(query, dbConfig);
		Integer count = 0;
		NovaCollection newCollection;
		
		do{
			List<Map<String, Object>> dadosOrigem = this.sqlRep.executarSql(query, count, dbConfig);
			newCollection = new NovaCollection(collectionName, idName, dadosOrigem, codVersa);				
			this.createServ.insertCollection(newCollection);
			count += SQLLIMIT;
		}
		while(count < numLinhas);
	}
		
	private String getCodVersao(){
		Date date = new Date();		
		return new SimpleDateFormat("yyyyMMddHHmmss").format(date);

	}
	
	private Boolean verificarDataAtualizacao(String dataStr) throws ParseException{
		if(dataStr.isEmpty()) {
			return false;
		}
			
		Boolean resposta = false;
		Date dataAtual = new Date();
		SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy h", Locale.US);
		
		String dataAtualStr = format.format(dataAtual);
		Date dataCollection = format.parse(dataStr);
		dataAtual = format.parse(dataAtualStr);
		if(dataCollection.compareTo(dataAtual) <=0){
			resposta = true;
		}
		return resposta;
	}
	
	@Override
	public String getAtualizacaoData(Frequency frequency){
		
		SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy h", Locale.US);
		Calendar calentar = Calendar.getInstance();
		calentar.setTime(new Date());
		
		switch (frequency) {
		case POR_HORA:
				calentar.set(Calendar.HOUR_OF_DAY, calentar.get(Calendar.HOUR_OF_DAY)+1);
				break;

		case DIARIO:
				calentar.set(Calendar.DAY_OF_MONTH, calentar.get(Calendar.DAY_OF_MONTH)+1);
				break;	

		case SEMANAL:
				calentar.set(Calendar.WEEK_OF_MONTH, calentar.get(Calendar.WEEK_OF_MONTH)+1);
				break;	

		case MENSAL:
				calentar.set(Calendar.MONTH, calentar.get(Calendar.MONTH)+1);
				break;

		case SEMESTRAL:
				calentar.set(Calendar.MONTH, calentar.get(Calendar.MONTH)+SOMASEMESTRE);
				break;

		case ANUAL:
				calentar.set(Calendar.YEAR, calentar.get(Calendar.YEAR)+1);
				break;

		case STATIC:
				return "";

		default:
				break;
		}
		
		return format.format(calentar.getTime());
	}
	
}
