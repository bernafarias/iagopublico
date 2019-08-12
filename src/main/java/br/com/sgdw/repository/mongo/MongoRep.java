package br.com.sgdw.repository.mongo;

import java.util.List;
import java.util.Map;

import br.com.sgdw.domain.Metadata;
import br.com.sgdw.domain.Suggestion;
import br.com.sgdw.domain.dto.SourceConfig;
import br.com.sgdw.domain.Category;

public interface MongoRep {

	/**
	 * @deprecated(usar o getByQuery)
	 * @param collectionName
	 * @param version
	 * @return
	 */
	@Deprecated
	List<Map<String, Object>> readCollection(String collectionName, String version);
	
	void insert(Map<String, Object> objeto, String collectionName);
	
	Map<String, Object> getById(String value, String collectionName, String key);
	
	void setOldVersion(String collectionName);

	void updatePassword(String newPassword, String login);

	void updateData(String collecionName, String newDate, String lastVersion, String newQuery);

	List<Map<String, Object>> getByQuery(Map<String, Object> parameters, String datasetName, Integer initial, Integer limit, String version);
	
	Integer countQueryData(Map<String, Object> parameters, String datasetName);

	Integer countData(String collectionName, String version);

	List<Map<String, Object>> getDataPage(String datasetName, String version, Integer initial, Integer limit);

	void insertObj(Object obj, String collectionName);

	SourceConfig getDbConfig(String id);

	void updateMetadata(String collectionName, String type, String description);

	Metadata getMetadata(String datasetName);

	void updateMetadata(Metadata metadata);

	List<Suggestion> listSuggestions();

	Boolean exists(String value, String collectionName, String keyName);

	void updateCollection(String collectionName, String keyname, String value, Map<String, Object> data);

	List<Category> listCategories();

	void removeCollection(String collectionName);
	
	void removeResister(String collectionName, String key, String value); 
}
