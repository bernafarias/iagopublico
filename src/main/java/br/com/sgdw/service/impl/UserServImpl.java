package br.com.sgdw.service.impl;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.sgdw.domain.dto.NovaSenha;
import br.com.sgdw.domain.dto.NovoUsuario;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.service.UserServ;
import br.com.sgdw.service.exception.UserNotFoundException;
import br.com.sgdw.util.constantes.CollectorUserVariables;
import br.com.sgdw.util.constantes.MongoVariables;

@Service
public class UserServImpl implements UserServ, UserDetailsService{

	@Autowired
	private MongoRep mongoRep;
	
	@Override
	public void inserirUsuario(NovoUsuario novoUsuario){
		Map<String, Object> usuario = new HashMap<>();

		usuario.put(CollectorUserVariables.COLLECTOR_LOGIN.valor(), novoUsuario.getUsuarioNovo());
		usuario.put(CollectorUserVariables.COLLECTOR_PASSWORD.valor(), new BCryptPasswordEncoder().encode(novoUsuario.getNovaSenha()));
		usuario.put(CollectorUserVariables.COLLECTOR_USERNAME.valor(), novoUsuario.getNovoNome());

		this.mongoRep.insert(usuario, CollectorUserVariables.COLLECTOR_COLLECTION_NAME.valor());
	}

	@Override
	public void alterarSenha(NovaSenha nova) throws UserNotFoundException{
		Map<String, Object> usuario = this.mongoRep.getById(nova.getUsuarioAlvo(), MongoVariables.USUARIO_COLLECTION.valor(),
				CollectorUserVariables.COLLECTOR_LOGIN.valor());

		if(usuario != null){
			String novaSenha = generateHash(nova.getSenhaNova());

			this.mongoRep.updatePassword(novaSenha, usuario.get(CollectorUserVariables.COLLECTOR_LOGIN.valor()).toString());
		}else{
			throw new UserNotFoundException();
		}
	}

	private void generateAdmin(){
		Map<String, Object> usuario = this.mongoRep.getById(MongoVariables.ADMIN_LOGIN.valor(), 
				MongoVariables.USUARIO_COLLECTION.valor(), 
				MongoVariables.COLLECTOR_USERNAME.valor());

		if(usuario == null){
			NovoUsuario novoUsuario = new NovoUsuario();
			novoUsuario.setNovoNome(MongoVariables.ADMIN_NAME.valor());
			novoUsuario.setUsuarioNovo(MongoVariables.ADMIN_LOGIN.valor());
			novoUsuario.setNovaSenha(MongoVariables.ADMIN_PASSWORD.valor());

			this.inserirUsuario(novoUsuario);
		}
	}

	private String generateHash(String password){
		Object salt = null;
		MessageDigestPasswordEncoder digestPasswordEncoder = getInstanceMessageDisterPassword();
		return digestPasswordEncoder.encodePassword(password, salt);
	}

	private static MessageDigestPasswordEncoder getInstanceMessageDisterPassword() {
		return new MessageDigestPasswordEncoder("MD5");
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserDetails user = null;
		Map<String, Object> u = this.mongoRep.getById(username, CollectorUserVariables.COLLECTOR_COLLECTION_NAME.valor(),
				CollectorUserVariables.COLLECTOR_LOGIN.valor());		

		if(u == null && username.equals(MongoVariables.ADMIN_LOGIN.valor())){
			this.generateAdmin();
			u = this.mongoRep.getById(username, CollectorUserVariables.COLLECTOR_COLLECTION_NAME.valor(),
					CollectorUserVariables.COLLECTOR_LOGIN.valor());		
		}


		List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));

		if(u != null){
			user = new User(u.get(CollectorUserVariables.COLLECTOR_LOGIN.valor()).toString(),
					u.get(CollectorUserVariables.COLLECTOR_PASSWORD.valor()).toString(), true,
					true, true, true, authorities);
		}else{
			throw new UsernameNotFoundException("Usuário não encontrado.");
		}

		return user;

	}

}
