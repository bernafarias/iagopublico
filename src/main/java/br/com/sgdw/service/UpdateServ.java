package br.com.sgdw.service;

import java.text.ParseException;

import br.com.sgdw.util.constantes.Frequency;

/** Esta classe contém todos os métodos para a atualização(seja automática ou manual) dos conjuntos de dados.
 * @author Lairson
 */
public interface UpdateServ {
	
	/** Este método atualiza os dados de todos os conjuntos de dados.
	 * @author Wilker
	 * @throws ParseException
	 */
	void atualizarDatasetsAuto() throws ParseException;	
	
	/** Este método retorna uma data futura, a partir do intervalo de tempo passado como parâmetro. Isto, para auxiliar na programação de novas
	 * atualização automáticas futuras.
	 * @author Wilker
	 * @param Frequency - Enum com o intervalo de tempo
	 * @see Frequency
	 * @return String - Data em formato String
	 */
	String getAtualizacaoData(Frequency frequency);
}
