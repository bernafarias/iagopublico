package br.com.sgdw.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import br.com.sgdw.domain.Category;
import br.com.sgdw.domain.dto.Contact;
import br.com.sgdw.domain.dto.DatasetQuery;
import br.com.sgdw.domain.dto.DatasetQueryResult;
import br.com.sgdw.service.exception.DatasetListNotFoundException;
import br.com.sgdw.service.exception.DatasetNotFoundException;
import br.com.sgdw.service.exception.DatasetPageNotFound;
import br.com.sgdw.service.exception.PreservationException;

/** Classe de acesso às informações armazenadas no sistema, dados e metadados dos conjuntos de dados
 * @author Lairson
 */
public interface AccessServ {

	/** Esta função tem objetivo de retornar dados em conjunto de dados, filtrando pelo identificador, em uma comparação chave valor.
	 * @author Wilker
	 * @param collectionName - Nome do conjunto de dados
	 * @param id - Código identificador
	 * @param format - Formato desejado - XML, CSV, JSON ou RDF
	 * @return String - Texto com os dados na especificação desejada, já prontos para serem retornados na API
	 */
	String getById(String collectionName, String id, String format);

	/** Esta função retorna uma lista com o nome de todos os conjuntos de dados publicados.
	 * @author Wilker
	 * @return String - JSON com uma lista dos nomes dos conjuntos de dados
	 */
	String getDatasetsNames();

	/** Esta função retorna os metadados de um conjunto de dados especificado.
	 * @author Wilker
	 * @deprecated Esta função será reescrita para substituir o tipo de retorno
	 * @param collectionName - Nome do conjunto de dados
	 * @return Map<String, Object> - Lista de metadados em formato Map
	 * @throws DatasetNotFoundException
	 */
	@Deprecated
	Map<String, Object> getMetadata(String collectionName) throws DatasetNotFoundException;
	
	/** Esta função retorna uma lista com todas as informações de todos os conjuntos de dados cadastrados,
	 * tanto metadados, quanto confugurações.
	 * @author Wilker
	 * @return String - Todas as informações dos conjuntos de dados em formato JSON
	 * @throws DatasetListNotFoundException
	 */
	String listDatasetsAdmin() throws DatasetListNotFoundException;
	
	/** Esta função retorna o conjunto de dados em uma consulta personalizada
	 * @author Wilker
	 * @param query - {@link DatasetQuery}
	 * @return - {@link DatasetQueryResult}
	 * @throws DatasetNotFoundException
	 * @throws DatasetPageNotFound
	 */
	DatasetQueryResult getDatasetByQuery(DatasetQuery query) throws DatasetPageNotFound;

	/** Esta função retorna os dados de um conjunto de dados de forma completa, em sua última versão, no formato desejado
	 * @author Wilker
	 * @param collectionName - Nome do conjunto de dados
	 * @param format - Formato desejado - XML, CSV, JSON ou RDF
	 * @return String - Texto com os dados na especificação desejada, já prontos para serem retornados na API
	 * @throws DatasetNotFoundException
	 * @throws PreservationException
	 */
	InputStream getCompleteDataset(String datasetName, String format);
		
	/** Esta função retorna uma lista com todos os formatos de dados suportados pelo sistema.
	 * 
	 * @return List<Map<String, Object>> - Lista de formatos de dados em Map
	 */
	List<Map<String, Object>> listFormats();

	/** Esta função retorna o nome de todos os atributos de um determinado dataset
	 * @author Wilker
	 * @param datasetName - Nome do Conjunto de dados
	 * @param version - Versão que deseja consultar
	 * @return Listy<String> - Lista com nomes dos atributos
	 */
	List<String> getDatasetFiels(String datasetName, String version) throws DatasetNotFoundException;

	/** Esta função tem o objetivo de enviar um email para contato.
	 * @author Wilker
	 * @param contact - Dados básicos para contato.
	 */
	void sendContact(Contact contact);
	
	/**
	 * Esta função tem o objetivo de retornar todas as categorias cadastradas.
	 * @author Lairson Alencar	 * 
	 * @return List<Suggestion> - Lista com todas as categorias
	 */
	List<Category> listCategories();
	
	/** Esta função retorna os dados de um Conjunto de dados a partir de Paginação
	 * @author Wilker
	 * @param datasetName
	 * @param version
	 * @param page
	 * @return
	 * @throws DatasetNotFoundException
	 * @throws DatasetPageNotFound
	 */
	List<Map<String, Object>> getDatasetByPage(String datasetName, String version, Integer page) throws DatasetPageNotFound;
	
	/** Esta função retorna uma parte do Conjunto de dados, classificado por versão e limitado a 100 registros
	 * @author Wilker
	 * @param datasetName
	 * @param version
	 * @return
	 * @throws DatasetNotFoundException
	 */
	List<Map<String, Object>> preVisualizeData(String datasetName, String version, Integer limit) throws DatasetNotFoundException;
		
	/** Esta função retorna os metadados no formato dcat 
	 * 
	 * @param datasetName - Nome do conjunto de dados
	 * @return - Map com os metadados em formato map
	 */
	Map<String, Object> getMetadataDcat(String datasetName);
}
