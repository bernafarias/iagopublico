package br.com.sgdw.service;

import java.io.InputStream;
import java.util.Map;

import br.com.sgdw.domain.dto.NovaVersao;
import br.com.sgdw.service.exception.DatasetNotFoundException;

/**Esta classe contém todos os métodos referentes ao versionamento dos conjuntos de dados.
 * @author Lairson
 */
public interface VersionServ {

	/**Esta função retorna todas versões disponíveis em um conjunto de dados.
	 * @author Lairson
	 * @param collectionName - Nome do conjunto de dados a ser consultado
	 * @return String - Lista de versões em formato JSON
	 */
	String listVersionsDataset(String collectionName);
	
	/**Esta função retorna os dados de um conjunto de dados por uma versão específica.
	 * @author Wilker
	 * @param datasetName - Nome do conjunto de dados
	 * @param version - Versão desejada
	 * @param format - Formato desejado
	 * @return String - Dados consultados no formato requerido
	 */
	InputStream getDatasetByVersion(String datasetName, String version, String format) throws DatasetNotFoundException;

	/**Esta função retorna informações sobre a versão desejada de um conjunto de dados.
	 * @author Lairson
	 * @param collectionName - Nome do conjunto de dados
	 * @param version - Versão a ser consultada
	 * @return Map<String, Object> - Atributos com informações da versão
	 */
	Map<String, Object> getMetadataVersion(String collectionName, String version);
	
	/** Esta função cadastra no sistema uma nova versão de dados.
	 * @deprecated Esta função insere uma nova versão apenas em arquivos, não no Mongo.
	 * @author Wilker
	 * @param NovaVersao - Classe com dados necessários para uma nova versão
	 * @see NovaVersao
	 */
	@Deprecated
	void insertNewVersion(NovaVersao newVersion);
}
