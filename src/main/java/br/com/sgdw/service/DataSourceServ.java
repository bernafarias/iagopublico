package br.com.sgdw.service;

import java.util.List;
import java.util.Map;

import br.com.sgdw.domain.dto.SourceConfig;
import br.com.sgdw.service.exception.SourceConnectionException;
import br.com.sgdw.service.exception.SourceListNotFound;

/** Esta classe contém todos o métodos para interação com as fontes em que o IAGO irá extrair dados.
 * @author Wilker
 *
 */
public interface DataSourceServ {

	/** Esta função cadastra uma nova fonte de dados, podendo ser Banco de dados Relacional, Não-Relacional, Arquivos de Texto ou APIs externas.
	 * @author Wilker
	 * @param SourceConfig - Classe com todas as configurações necessárias para o cadastro
	 * @see SourceConfig
	 * @throws SourceConnectionException
	 */
	void createSourceConfig(SourceConfig cfg) throws SourceConnectionException;

	/** Esta função lista todos os Sistemas de Bancos de Dados compatíveis com o IAGO
	 * @author Lairson
	 * @return List<Map<String, Object>> - Lista com os nomes dos SGBDs
	 */
	List<Map<String, Object>> listSGBDs();
	
	/** Esta função lista todas as fontes de dados cadastradas.
	 * @author Lairson
	 * @return List<Map<String, Object>> - Lista com todas as fontes de dados cadastradas
	 * @throws SourceListNotFound
	 */
	List<Map<String, Object>> lisRepSGBDs() throws SourceListNotFound;
}
