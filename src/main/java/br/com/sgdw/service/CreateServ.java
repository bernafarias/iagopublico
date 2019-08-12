package br.com.sgdw.service;
import br.com.sgdw.domain.Category;
import br.com.sgdw.domain.dto.AttributesDescriptionRequest;
import br.com.sgdw.domain.dto.NovaCollection;
import br.com.sgdw.domain.dto.NovoDataset;
import br.com.sgdw.service.exception.CategoryAlreadyExistsException;
import br.com.sgdw.service.exception.CategoryNotFoundException;
import br.com.sgdw.service.exception.DatasetAlreadyExistsException;
import br.com.sgdw.service.exception.DatasetNotFoundException;

/** Classe para criação de novos conjuntos de dados e inserção de novos dados. 
 * 
 * @author Lairson
 *
 */
public interface CreateServ {

	/** Esta função tem como o objetivo a publicação de um novo conjunto de dados, desde a inserção de seus metadados e configurações,
	 * até a extração dos dados da fonte de origem.
	 * @author Wilker
	 * @param NovoDataset - Classe contendo todas as informações necessárias para a criação
	 * @throws EmpyDatasetSQLException
	 * @throws DatasetAlreadyExistsException
	 * @see NovoDataset
	 */
	void createDataset(NovoDataset novo) throws DatasetAlreadyExistsException, CategoryNotFoundException;
	
	/** Esta função insere novos dados, em um conjunto da dados já criado
	 * @author Wilker
	 * @deprecated Função desatualizada, não gera uma nova versão e nem cria os arquivos em diversas distribuições
	 * @param NovaCollection - Classe que guarda todos os novos dados a serem inseridos
	 * @see NovaCollection
	 * @deprecated(Pula algumas regras de negócio. Rever.)
	 */
	@Deprecated
	void insertCollection(NovaCollection newCollection);

	/** Esta função permite a criação de novas categorias para conjuntos de dados.
	 * @author Wilker
	 * @param category - Classe contendo todas as informações necessárias para a criação
	 * @throws CategoryAlreadyExists - Exceção caso a categoria já exista.
	 * @see Category
	 */
	void createCategory(Category category) throws CategoryAlreadyExistsException;

	/** Esta função insere a descrição dos campos nos metadados do conjunto de dados
	 * @author Wilker
	 * @param description
	 */
	void insertFieldDescription(AttributesDescriptionRequest description) throws DatasetNotFoundException;
}
