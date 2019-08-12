package br.com.sgdw.service.impl;

import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.service.IdentifierServ;
import br.com.sgdw.util.constantes.CollectionConfVariables;
import br.com.sgdw.util.constantes.MongoVariables;

/**
 * @deprecated(Implementar as regras daqui em CreateServImpl)
 * @author Lairson
 *
 */
@Service
@Deprecated
public class IdentifierServImpl implements IdentifierServ {
	
	@Autowired
	private MongoRep mongoRep;
	
	@Deprecated
	public String criarIdentificador(String datasetTitle) {
		
		StringBuilder retorno = new StringBuilder(); 
		
		datasetTitle = Normalizer.normalize(datasetTitle, Normalizer.Form.NFD);
		datasetTitle = datasetTitle.replaceAll("[^\\p{ASCII}]", "");
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("."), "-");
		datasetTitle = datasetTitle.replaceAll("#","");
		datasetTitle = datasetTitle.replaceAll("\t \n \r", "");
		datasetTitle = datasetTitle.replaceAll("\u00A9","");
		datasetTitle = datasetTitle.replaceAll("[\"]", "");
		datasetTitle = datasetTitle.replaceAll("[/]", "");
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("["), "");
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("]"), "");
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("{"), "");
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("}"), "");	
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("~"), "");	
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("�"), "");
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("="), "");	
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("`"), "");	
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("'"), "");		
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("�"), "");	
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("&"), "-");
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("@"), "");		
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("%"), "");			
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("}"), "");			
		datasetTitle = datasetTitle.replaceAll(" ","-");
		
		retorno.append(datasetTitle);
		
		while (!validarIdentificador(retorno.toString())){
			retorno.append(retorno.toString() + "-");
		}
		
		return retorno.toString();
	}
	
	@Deprecated
	public Boolean validarIdentificador(String uri) {
		
		Boolean valida = true;
		List<Map<String, Object>> dados = this.mongoRep.readCollection(MongoVariables.CONF_COLLECTION.valor(), null);	
		if(dados != null){
			for(Map<String, Object> dataset : dados){
				if (dataset.get(CollectionConfVariables.COLLECTION_NAME.valor()).equals(uri)) {
					valida = false;
				}
			}
		
		}
		return valida;		
		
	}
		
}
