package br.com.sgdw.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.sgdw.domain.dto.NovaSenha;
import br.com.sgdw.domain.dto.NovoUsuario;
import br.com.sgdw.service.exception.UserNotFoundException;

/**Esta classe contém todos os métodos referentes a gerenciamento de usuários.
 * @author Wilker
 *
 */
public interface UserServ {
	
	/**Esta função insere um novo usuário.
	 * @author Wilker
	 * @param NovoUsuario - Classe com dados necessários para a inserção de um novo usuário
	 * @see NovoUsuario
	 */
	void inserirUsuario(NovoUsuario novoUsuario);

	/**Esta função altera a senha de um usuário existente.
	 * @author Wilker
	 * @param NovaSenha - Classe com dados necessários para a alteração de senha
	 * @see NovaSenha
	 * @throws UserNotFoundException
	 */
	void alterarSenha(NovaSenha nova) throws UserNotFoundException;
	
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}
