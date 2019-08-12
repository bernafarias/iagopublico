package br.com.sgdw.service.impl;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sgdw.domain.Suggestion;
import br.com.sgdw.domain.dto.NewSugestion;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.service.FeedbackServ;
import br.com.sgdw.util.constantes.MongoVariables;

@Service
public class FeedbackServImpl implements FeedbackServ{

	static final Logger log = Logger.getLogger(FeedbackServImpl.class); 

	@Autowired
	private MongoRep mongoRep;

	@Override
	public void insertNewSugestion(NewSugestion newSugestion){
		Suggestion sugestion = new Suggestion(newSugestion);
		this.mongoRep.insertObj(sugestion, MongoVariables.SUGESTION_COLLECTION.valor());
	}

	@Override
	public List<Suggestion> listSuggestions(){	
		return this.mongoRep.listSuggestions();
	}
}
