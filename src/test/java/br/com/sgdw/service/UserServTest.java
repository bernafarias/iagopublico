package br.com.sgdw.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.google.gson.Gson;

import br.com.sgdw.api.AdminRest;
import br.com.sgdw.api.security.AccountCredentials;
import br.com.sgdw.domain.dto.NovaSenha;
import br.com.sgdw.domain.dto.NovoUsuario;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.service.exception.UserNotFoundException;
import br.com.sgdw.util.constantes.CollectorUserVariables;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext
@EnableAutoConfiguration
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserServTest{
	
	static final Logger log = Logger.getLogger(UserServTest.class);

	@Autowired
	private MongoRep mongoRep;
	
	@Autowired
	private AdminRest adminRest;
	
	@Autowired
	private UserServ userServ;
	
	private static NovoUsuario novo;
	private static final String ADMIN_USER = "admin";
	private static final String FAKEUSER = "fakeUser";
	
	@Autowired
	private MockMvc mockMvc;

	@BeforeClass 
	public static void initUserServTest() throws IOException {

		novo = new NovoUsuario();

		novo.setNovoNome("Usuario Teste");
		novo.setUsuarioNovo("teste");
		novo.setNovaSenha("teste123");

	}
	
	@Test
	public void teste0LoginAdmin(){
		UserDetails details = userServ.loadUserByUsername(ADMIN_USER);
		assertEquals(ADMIN_USER, details.getUsername());
	}

	@Test
	public void teste1InserirUsuario(){
		
		adminRest.addUser(novo);

		Map<String, Object> user = mongoRep.getById(novo.getUsuarioNovo(), CollectorUserVariables.COLLECTOR_COLLECTION_NAME.valor(),
				CollectorUserVariables.COLLECTOR_LOGIN.valor());

		assertNotNull(user);

		if(!user.isEmpty()){
			String login = user.get(CollectorUserVariables.COLLECTOR_LOGIN.valor()).toString();
			assertEquals(novo.getUsuarioNovo(), login);

			String nome = user.get(CollectorUserVariables.COLLECTOR_USERNAME.valor()).toString();
			assertEquals(novo.getNovoNome(), nome);
		}

	}
	
	@Test
	public void teste2AlterarSenha() throws UserNotFoundException{
		
		String senhaAntiga = "";
		String senhaNova = "";
		Map<String, Object> user;
			
		user = mongoRep.getById(novo.getUsuarioNovo(), CollectorUserVariables.COLLECTOR_COLLECTION_NAME.valor(),
				CollectorUserVariables.COLLECTOR_LOGIN.valor());

		if(!user.isEmpty()){
			senhaAntiga = user.get(CollectorUserVariables.COLLECTOR_PASSWORD.valor()).toString();
		}
		
		NovaSenha novaSenha = new NovaSenha();
		novaSenha.setUsuarioAlvo(novo.getUsuarioNovo());
		novaSenha.setSenhaNova("novasenha123");
		
		adminRest.alterPassword(novaSenha, null);
				
		user = mongoRep.getById(novo.getUsuarioNovo(), CollectorUserVariables.COLLECTOR_COLLECTION_NAME.valor(),
				CollectorUserVariables.COLLECTOR_LOGIN.valor());

		if(!user.isEmpty()){
			senhaNova = user.get(CollectorUserVariables.COLLECTOR_PASSWORD.valor()).toString();
		}
		
		assertNotEquals(senhaAntiga, senhaNova);
		
		HttpServletResponse response = new MockHttpServletResponse();
		novaSenha.setUsuarioAlvo(FAKEUSER);
		adminRest.alterPassword(novaSenha, response);
		assertEquals(UserNotFoundException.STATUS, response.getStatus());
	}
	
	@Test
	public void loginTest() throws Exception {
		AccountCredentials credentials = new AccountCredentials();
		credentials.setUsername(ADMIN_USER);
		credentials.setPassword("password");
		
		MvcResult result = mockMvc.perform(post("/login")
								.contentType(MediaType.APPLICATION_JSON)
								.content(new Gson().toJson(credentials))).andReturn();
		String token = result.getResponse().getHeader("Authorization");
		assertNotNull(token);
		
		result = mockMvc.perform(post("/admin/verify_login")
				.header("Authorization", token)).andReturn();
		
		assertEquals(HttpStatus.SC_OK, result.getResponse().getStatus());
	}
}
