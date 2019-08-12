package br.com.sgdw.domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.mongodb.DBObject;

import br.com.sgdw.domain.dto.NewSugestion;
import br.com.sgdw.util.constantes.SugestionStatus;
import br.com.sgdw.util.constantes.SystemMsg;

public class Suggestion extends NewSugestion{
	
	static final Logger log = Logger.getLogger(Suggestion.class); 
	
	private SugestionStatus status;
	
	private Date solicitationDate;
	
	
	public Suggestion() {}
	
	public Suggestion(NewSugestion newSugestion){
		super(newSugestion.getName(), newSugestion.getEmail(), newSugestion.getDescription());
		this.status = SugestionStatus.EVALUATION;
		this.solicitationDate = new Date();
	}
	
	public Suggestion(DBObject dbObject){
		super.setName(dbObject.get("name").toString());
		super.setEmail(dbObject.get("email").toString());
		super.setDescription(dbObject.get("description").toString());
		this.status = SugestionStatus.valueOf( dbObject.get("status").toString());
		try {
			DateFormat formatter = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
			this.solicitationDate = formatter.parse(dbObject.get("solicitationDate").toString());
		} catch (ParseException e) {
			log.error(SystemMsg.PARSE_ERROR.valor(), e);
		}
	}

	public SugestionStatus getStatus() {
		return status;
	}


	public void setStatus(SugestionStatus status) {
		this.status = status;
	}


	public Date getSolicitationDate() {
		return solicitationDate;
	}


	public void setSolicitationDate(Date solicitationDate) {
		this.solicitationDate = solicitationDate;
	}
}
