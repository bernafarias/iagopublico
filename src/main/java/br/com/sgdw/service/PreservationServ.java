package br.com.sgdw.service;

import br.com.sgdw.domain.dto.PreservarDataset;
import br.com.sgdw.service.exception.DatasetNotFoundException;

/**Esta classe contém todos os métodos necessários para a preservação (não ficar mais disponível para visualização)
 * de um conjunto de dados.
 * @author Lairson
 */
public interface PreservationServ {

	/**Esta função preserva um dataset, impedindo que um consumidor de dados possa vizualiza-lo futuramente.
	 * @author Lairson
	 * @param PreservarDataset - Classe com todos os dados necessários para a preservação do conjunto de dados
	 * @see PreservarDataset
	 * @throws DatasetNotFoundException
	 */
	void preservarDataset(PreservarDataset preservacao) throws DatasetNotFoundException;
	
	/**Esta função verifica o estado de preservação de um consjunto de dados.
	 * @author Lairson
	 * @param datasetTitle - Nome do conjunto de dados que deseja verificar
	 * @return Boolean - Booleano em resposta à preservação do conjunto de dados
	 * @throws DatasetNotFoundException
	 */
	Boolean verificarPreservacao(String datasetTitle) throws DatasetNotFoundException;
	
	/**Esta função retorna o motivo que o conjunto de dados foi preservado.
	 * @author Lairson
	 * @param datasetTitle - Nome do conjunto de dados que deseja verificar
	 * @return String - Motivo da preservação
	 * @throws DatasetNotFoundException
	 */
	String motivoPreservacao(String datasetTitle) throws DatasetNotFoundException;
	
	void removeDataset(String datasetName) throws DatasetNotFoundException;
}
