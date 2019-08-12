package br.com.sgdw.service;

import java.util.List;


import br.com.sgdw.domain.Suggestion;
import br.com.sgdw.domain.dto.NewSugestion;

/** Esta classe contém todos os métodos referentes ao feedback do consumidor de dados
 * @author Wilker
 */
public interface FeedbackServ {

	/** Esta função insere uma nova sugestão vinda de um consumidor de dados
	 * @author Wilker
	 * @param NewSugestion - Classe com dados da nova sugestão
	 * @see NewSugestion
	 */
	void insertNewSugestion(NewSugestion newSugestion);

	/** Esta função lista todas as sugestões para criação de novos conjuntos de dados
	 * @author Wilker
	 * @return
	 * @see Suggestion
	 */
	List<Suggestion> listSuggestions();
}
