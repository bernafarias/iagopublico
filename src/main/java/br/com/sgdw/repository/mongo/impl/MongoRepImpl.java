package br.com.sgdw.repository.mongo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

import br.com.sgdw.domain.Category;
import br.com.sgdw.domain.Metadata;
import br.com.sgdw.domain.Suggestion;
import br.com.sgdw.domain.dto.SourceConfig;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.util.MongoUtil;
import br.com.sgdw.util.constantes.CollectionConfVariables;
import br.com.sgdw.util.constantes.CollectorUserVariables;
import br.com.sgdw.util.constantes.MongoVariables;
import br.com.sgdw.util.constantes.SystemMsg;

@Repository
public class MongoRepImpl implements MongoRep{

	static final Logger log = Logger.getLogger(MongoRepImpl.class); 
	
	private static final String ID = "id";

	/**
	 * @deprecated(Usar o getByQuery)
	 */
	@Override
	@Deprecated
	public List<Map<String, Object>> readCollection(String collectionName, String version){
		List<Map<String, Object>> retorno = new ArrayList<>();

		log.info(SystemMsg.MONGO_GET_COLLECTION.valor()+collectionName);

		try{
			DBCollection collection = getCollection(collectionName);

			DBObject query = BasicDBObjectBuilder.start().get();

			if(version != null) {
				query = BasicDBObjectBuilder.start().add(MongoVariables.VERSION.valor(), version).get();
			}

			DBCursor cursor;

			if((collectionName.equals(MongoVariables.CONF_COLLECTION.valor()))||
					(collectionName.equals(MongoVariables.SOURCE_CONF_COLLECTION.valor()))||
					(collectionName.contains(MongoVariables.HISTORY_COLLECTION.valor()))){
				cursor = collection.find();
			}else{
				cursor = collection.find(query);
			}

			while(cursor.hasNext()){
				DBObject obj = cursor.next();
				retorno.add(obj.toMap());
			}

		}catch(MongoException e) {
			log.error(SystemMsg.MONGO_ERROR.valor(), e);
		}

		return retorno;
	}

	private DBCollection getCollection(String collectionName) throws MongoException{

		log.info(SystemMsg.MONGO_CONNECTING.valor());

		DBCollection collection;
		DB db = MongoUtil.getDB();

		collection = db.getCollection(collectionName);

		return collection;	
	}

	@Override
	public void insert(Map<String, Object> objeto, String collectionName){

		log.info(SystemMsg.MONGO_INSERT_COLLECTION.valor()+collectionName);

		try{
			DBCollection collection = this.getCollection(collectionName);

			BasicDBObjectBuilder docBuilder;
			docBuilder = BasicDBObjectBuilder.start();

			for(Entry<String, Object> entry : objeto.entrySet()){
				Object valor = entry.getValue()+"";
				docBuilder.append(entry.getKey(), valor);
			}
			collection.insert(docBuilder.get());


		}catch(MongoException e){
			log.error(SystemMsg.MONGO_ERROR.valor(), e);
		}
	}

	@Override
	public Map<String, Object> getById(String value, String collectionName, String keyName){

		Map<String, Object> retorno = null;

		log.info(SystemMsg.MONGO_GET_COLLECTION_BY_ID.valor()+"Collection: "+collectionName+", Field: "+keyName+ ", Value: "+value);

		try{

			DBCollection collection = getCollection(collectionName);

			BasicDBObject query = new BasicDBObject();
			query.put(keyName, value);


			DBCursor cursor = collection.find(query);

			if(cursor.hasNext()){
				DBObject obj = cursor.next();
				retorno = obj.toMap();
			}
		}catch(MongoException e){
			log.error(SystemMsg.MONGO_ERROR.valor(), e);
		}

		return retorno;
	}

	@Override
	public List<Map<String, Object>> getByQuery(Map<String, Object> parameters, String datasetName, Integer initial, Integer limit, String version){

		List<Map<String, Object>> retorno = null;

		log.info(SystemMsg.MONGO_GET_COLLECTION_BY_QUERY.valor() + datasetName);

		try{
			DBCollection collection = getCollection(datasetName);

			BasicDBObject query = new BasicDBObject();

			retorno = new ArrayList<>();

			query.put(MongoVariables.VERSION.valor(), version);

			if(parameters != null){
				for(Entry<String, Object> entry : parameters.entrySet()){
					query.put(entry.getKey(), java.util.regex.Pattern.compile(entry.getValue().toString()));
				}
			}

			DBCursor cursor = collection.find(query).skip(initial).limit(limit);

			while(cursor.hasNext()){
				DBObject obj = cursor.next();
				retorno.add(obj.toMap());
			}

		}catch(MongoException e){
			log.error(SystemMsg.MONGO_ERROR.valor(), e);
		}

		return retorno;

	}

	public Integer countQueryData(Map<String, Object> parameters, String datasetName){
		log.info(SystemMsg.MONGO_GET_COLLECTION_BY_QUERY.valor() + datasetName);

		Integer count = 0;

		try{
			DBCollection collection = getCollection(datasetName);
			BasicDBObject query = new BasicDBObject();
			for(Entry<String, Object> entry : parameters.entrySet()){
				query.put(entry.getKey(), java.util.regex.Pattern.compile(entry.getValue().toString()));
			}

			count = collection.find(query).count();
		}catch(MongoException e) {
			log.error(SystemMsg.MONGO_ERROR.valor(), e);
		}

		return count;
	}

	@Override
	public Integer countData(String collectionName, String version){
		Integer contagem = null;

		log.info(SystemMsg.MONGO_COUNTING_ROW_NUMBERS.valor()+collectionName);

		try{
			DBCollection collection = getCollection(collectionName);

			BasicDBObject query = new BasicDBObject();

			if(version != null) {
				query.put(MongoVariables.VERSION.valor(), version);
			}	
			contagem = (int) collection.count(query);

		}catch(MongoException e){
			log.error(SystemMsg.MONGO_ERROR.valor(), e);
		}

		return contagem;
	}

	@Override
	public void setOldVersion(String collectionName){
		log.info(SystemMsg.MONGO_UPDATING_VERSIONS.valor()+collectionName);

		try{
			DBCollection collection = getCollection(collectionName);

			DBCursor cursor = collection.find();

			while(cursor.hasNext()){
				DBObject oldVersion = cursor.next();
				Map<String, Object> aux = oldVersion.toMap();

				BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
				DBObject query = null;

				for(Entry<String, Object> entry : aux.entrySet()){
					if(entry.getKey().equals(MongoVariables.LAST_VERSION.valor())){
						builder.append(entry.getKey(), "false");
					}else{
						if(!entry.getKey().equals(MongoVariables.ID_COLLECTION.valor())){
							builder.append(entry.getKey(), entry.getValue()+"");
						}else{
							query = BasicDBObjectBuilder.start().add(MongoVariables.ID_COLLECTION.valor(), entry.getValue()+"").get();
						}
					}

				}

				collection.update((DBObject) query, builder.get());
			}


		}catch(MongoException e) {
			log.error(SystemMsg.MONGO_ERROR.valor(), e);
		}	
	}


	@Override
	public void updatePassword(String newPassword, String login){
		log.info(SystemMsg.MONGO_CHANGING_PASSWORD.valor()+login);

		try{
			DBCollection collection = getCollection(CollectorUserVariables.COLLECTOR_COLLECTION_NAME.valor());

			BasicDBObject newDocument = new BasicDBObject();
			newDocument.append("$set", new BasicDBObject().append(CollectorUserVariables.COLLECTOR_PASSWORD.valor(), newPassword));

			BasicDBObject searchQuery = new BasicDBObject().append(CollectorUserVariables.COLLECTOR_LOGIN.valor(), login);

			collection.update(searchQuery, newDocument);
		}catch(MongoException e) {
			log.error(SystemMsg.MONGO_ERROR.valor(), e);
		}
	}

	@Override
	public void updateData(String collecionName, String newDate, String lastVersion, String newQuery){
		log.info(SystemMsg.MONGO_UPDATING_UPDATE_DATE.valor()+collecionName);

		try{
			DBCollection collection = getCollection(MongoVariables.CONF_COLLECTION.valor());

			BasicDBObject query = new BasicDBObject();
			query.put(CollectionConfVariables.COLLECTION_NAME.valor(), collecionName);
			DBCursor cursor = collection.find(query);

			while(cursor.hasNext()){
				BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();

				Map<String, Object> map = cursor.next().toMap();

				for(Entry<String, Object> entry : map.entrySet()){
					if(entry.getKey().equals(CollectionConfVariables.COLLECTION_NEXT_UPDATE.valor())){
						builder.append(CollectionConfVariables.COLLECTION_NEXT_UPDATE.valor(), newDate);
					}else{
						if(entry.getKey().equals(CollectionConfVariables.COLLECTION_LAST_VERSION.valor())){
							builder.append(CollectionConfVariables.COLLECTION_LAST_VERSION.valor(), lastVersion);
						}else{
							if(entry.getKey().equals(CollectionConfVariables.COLLECTION_QUERY.valor())){
								if(newQuery != null){
									builder.append(CollectionConfVariables.COLLECTION_QUERY.valor(), newQuery);
								}			
							}else{
								builder.append(entry.getKey(), entry.getValue());
							}

						}
					}
				}
				collection.update(query, builder.get());
			}

		}catch(MongoException e) {
			log.error(SystemMsg.MONGO_ERROR.valor(), e);
		}

	}

	@Override
	public List<Map<String, Object>> getDataPage(String datasetName, String version, Integer initial, Integer limit){

		log.info(SystemMsg.MONGO_GET_COLLECTION_PAGE.valor()+datasetName+" - " +initial);

		List<Map<String, Object>> dados = new ArrayList<>();
		try{
			DBCollection collection = getCollection(datasetName);


			BasicDBObject query = new BasicDBObject();
			query.put(MongoVariables.VERSION.valor(), version);

			DBCursor cursor;

			cursor = collection.find(query).skip(initial).limit(limit);

			while(cursor.hasNext()){
				dados.add(cursor.next().toMap());
			}

		}catch(MongoException e){
			log.error(SystemMsg.MONGO_ERROR.valor(), e);
		}

		return dados;
	}

	@Override
	public void insertObj(Object obj, String collectionName){

		log.info(SystemMsg.MONGO_INSERT_OBJ.valor()+collectionName);

		try{
			DBCollection collection = getCollection(collectionName);

			BasicDBObject document = (BasicDBObject) JSON.parse(new Gson().toJson(obj));
			document.put(MongoVariables.ID_COLLECTION.valor(), document.get(ID));
			document.remove(ID);

			collection.insert(document);

		}catch(NullPointerException e) {
			log.error(SystemMsg.MONGO_ERROR.valor(), e);
		}

	}

	@Override
	public SourceConfig getDbConfig(String id){
		log.info(SystemMsg.MONGO_GET_SOURCE_CONFIG.valor()+id);

		SourceConfig config = null;

		try{
			DBCollection collection = getCollection(MongoVariables.SOURCE_CONF_COLLECTION.valor());

			BasicDBObject query = new BasicDBObject();
			query.put(MongoVariables.ID_COLLECTION.valor(), id);
			DBCursor cursor = collection.find(query);

			DBObject obj = cursor.next();

			config = new SourceConfig(obj);



		}catch(MongoException e) {
			log.error(SystemMsg.MONGO_ERROR.valor(), e);
		}

		return config;
	}

	@Override
	public void updateMetadata(String collectionName, String type, String description){	

		log.info(SystemMsg.MONGO_UPDATING_METADATA.valor()+collectionName);

		try{

			DBCollection collection = getCollection(MongoVariables.CONF_COLLECTION.valor());
			BasicDBObject query = new BasicDBObject();
			query.put(CollectionConfVariables.COLLECTION_NAME.valor(), collectionName);

			BasicDBObject builder = new BasicDBObject();
			builder.append(CollectionConfVariables.COLLECTION_PRESERVE.valor(), type);
			builder.append(CollectionConfVariables.COLLECTION_PRESERVE_DESCRIPTION.valor(), description);

			collection.update(query, new BasicDBObject("$set", builder), true, false);

		}catch(MongoException e) {
			log.error(SystemMsg.MONGO_ERROR.valor(), e);
		}

	}

	@Override
	public Metadata getMetadata(String datasetName){

		log.info(SystemMsg.MONGO_GET_METADATA.valor()+datasetName);

		Metadata metadata = null;
		try{
			DBCollection collection = getCollection(MongoVariables.CONF_COLLECTION.valor());

			BasicDBObject query = new BasicDBObject();
			query.put(CollectionConfVariables.COLLECTION_NAME.valor(), datasetName);

			DBObject obj;	
			DBCursor cursor = collection.find(query);	

			if(cursor.hasNext()){
				obj = cursor.next();	
				metadata = new Gson().fromJson(JSON.serialize(obj), Metadata.class);
			}
		}catch(MongoException e) {
			log.error(SystemMsg.MONGO_ERROR.valor(), e);
		}

		return metadata;
	}

	@Override
	public void updateMetadata(Metadata metadata){

		log.info(SystemMsg.MONGO_UPDATING_METADATA.valor()+metadata.getIdentifierUri());

		try{
			DBCollection collection = getCollection(MongoVariables.CONF_COLLECTION.valor());
			BasicDBObject query = new BasicDBObject();
			query.put(CollectionConfVariables.COLLECTION_NAME.valor(), metadata.getCollectorDataset());
			DBObject dbObject = (DBObject) JSON.parse(new Gson().toJson(metadata));

			collection.update(query, dbObject);
		}catch(MongoException e) {
			log.error(SystemMsg.MONGO_ERROR.valor(), e);
		}
	}

	@Override
	public List<Suggestion> listSuggestions(){
		List<Suggestion> suggestionsList = new ArrayList<>();
		try{
			DBCollection collection = getCollection(MongoVariables.SUGESTION_COLLECTION.valor());
			BasicDBObject query = new BasicDBObject();
			DBCursor cursor = collection.find(query);

			while(cursor.hasNext()){
				suggestionsList.add(new Suggestion(cursor.next()));
			}
		}catch(MongoException e) {
			log.error(SystemMsg.MONGO_ERROR.valor(), e);
		}

		return suggestionsList;
	}

	@Override
	public List<Category> listCategories(){
		List<Category> categoriesList = new ArrayList<>();
		try{
			DBCollection collection = getCollection(MongoVariables.CATEGORY.valor());
			BasicDBObject query = new BasicDBObject();
			DBCursor cursor = collection.find(query);

			while(cursor.hasNext()){
				categoriesList.add(new Category(cursor.next()));
			}
		}catch(MongoException e) {
			log.error(SystemMsg.MONGO_ERROR.valor(), e);
		}

		return categoriesList;
	}

	@Override
	public Boolean exists(String value, String collectionName, String keyName){
		Boolean exists = false;
		Map<String, Object> obj = getById(value, collectionName, keyName);
		if(obj != null && !obj.isEmpty()){
			exists = true;
		}
		return exists;
	}

	@Override
	public void updateCollection(String collectionName, String keyname, String value, Map<String, Object> data){
		try{
			DBCollection collection = getCollection(collectionName);
			BasicDBObject query = new BasicDBObject();
			query.put(keyname, value);
			DBObject dbObject = (DBObject) JSON.parse(new Gson().toJson(data));

			collection.update(query, dbObject);
		}catch(MongoException e) {
			log.error(SystemMsg.MONGO_ERROR.valor(), e);
		}

	}
	
	@Override
	public void removeCollection(String collectionName) {
		try {
			DBCollection collection = getCollection(collectionName);
			collection.drop();
		}catch (MongoException e) {
			log.error(e);
		}
	}
	
	@Override
	public void removeResister(String collectionName, String key, String value) {
		try {
			DBCollection collection = getCollection(collectionName);
			BasicDBObject query = new BasicDBObject();
			query.put(key, value);
			collection.remove(query);
		}catch (MongoException e) {
			log.error(e);
		}
	}
}