package br.com.sgdw.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import br.com.sgdw.domain.Category;
import br.com.sgdw.domain.dto.AttributesDescriptionRequest;
import br.com.sgdw.domain.dto.AttrubutesNamesRequest;
import br.com.sgdw.domain.dto.DatasetUri;
import br.com.sgdw.domain.dto.NovaSenha;
import br.com.sgdw.domain.dto.NovoDataset;
import br.com.sgdw.domain.dto.NovoUsuario;
import br.com.sgdw.domain.dto.PreservarDataset;
import br.com.sgdw.domain.dto.SourceConfig;
import br.com.sgdw.domain.dto.UriValidacao;
import br.com.sgdw.service.AccessServ;
import br.com.sgdw.service.CreateServ;
import br.com.sgdw.service.DataSourceServ;
import br.com.sgdw.service.IdentifierServ;
import br.com.sgdw.service.PreservationServ;
import br.com.sgdw.service.UserServ;
import br.com.sgdw.service.exception.CategoryAlreadyExistsException;
import br.com.sgdw.service.exception.CategoryNotFoundException;
import br.com.sgdw.service.exception.DatasetAlreadyExistsException;
import br.com.sgdw.service.exception.DatasetListNotFoundException;
import br.com.sgdw.service.exception.DatasetNotFoundException;
import br.com.sgdw.service.exception.SourceConnectionException;
import br.com.sgdw.service.exception.SourceListNotFound;
import br.com.sgdw.service.exception.UserNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "/admin")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/admin")
public class AdminRest {
	
	static final Logger log = Logger.getLogger(AdminRest.class);
	
	@Autowired
	private AccessServ accessServ;
	
	@Autowired
	private CreateServ createServ;
	
	@Autowired
	private UserServ userServ;
	
	@Autowired
	private AccessServ collectorServ;
	
	@Autowired
	private DataSourceServ dataSourceServ;
	
	@Autowired
	private IdentifierServ identifierServ;
	
	@Autowired
	private PreservationServ preservationServ;
	
	@CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Adiciona novo conjunto de dados", nickname = "add_dataset", 
    		notes="Esta rota permite adicionar um novo conjunto de dados")
    @RequestMapping(method = RequestMethod.POST, path="/add_dataset")
	public void addDataset(@Valid @RequestBody NovoDataset novo, HttpServletResponse response){
		try {
			this.createServ.createDataset(novo);
		} catch (DatasetAlreadyExistsException | CategoryNotFoundException e) {
			log.error(e);
			IagoResponse.setResponse(response, e);
		}
	}
	
	@CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Adiciona novo Produtor (usuário)", nickname = "add_user", 
    		notes="Esta rota permite adicionar um novo usuário/produtor")
    @RequestMapping(method = RequestMethod.POST, path="/add_user")	
    public void addUser(@Valid @RequestBody NovoUsuario novo){
		this.userServ.inserirUsuario(novo);
	}
	
	@CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Alterar senha de acesso", nickname = "alter_password", 
    		notes="Esta rota permite alterar a senha de acesso de um login (produtor)")
    @RequestMapping(method = RequestMethod.POST, path="/alter_password")	
	public void alterPassword(@Valid @RequestBody NovaSenha nova, HttpServletResponse response){
		try {
			this.userServ.alterarSenha(nova);
		} catch (UserNotFoundException e) {
			log.error(e);
			IagoResponse.setResponse(response, e);
		}
	}
	
    @CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Criar URI persistente", nickname = "criar_uri", 
    		notes="Esta rota permite criar uma URI persistente")
    @RequestMapping(method = RequestMethod.POST, path="/criar_uri")	
	public @ResponseBody String criarURI(@Valid @RequestBody UriValidacao uri){
    	return this.identifierServ.criarIdentificador(uri.getUri());
	}
	
    //Existência deste método é questionável, analisar depois e melhorar o codigo, se preciso
    @CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Checar se a URI é válida", nickname = "checar_uri", 
    		notes="Esta rota permite checar se uma URI é válida")
    @RequestMapping(method = RequestMethod.POST, path="/checar_uri")	
	public @ResponseBody Boolean checarURI(@Valid @RequestBody UriValidacao uri){
    	Boolean valido = false;
		valido = this.identifierServ.validarIdentificador(uri.getUri());

		return valido;
	}
    
    @CrossOrigin(methods = {RequestMethod.GET})
    @ApiOperation(value = "Lista dos conjuntos de dados cadastrados (padrão produtor)", nickname = "list_datasets", 
    		notes="Esta rota permite listar os conjuntos de dados com campos adicionais apenas para o Produtor")
    @RequestMapping(method = RequestMethod.GET, path="/list_datasets")	
	public String listDatasets(HttpServletResponse response){
		String datasets = null;
		try {
			datasets = this.collectorServ.listDatasetsAdmin();
		} catch (DatasetListNotFoundException e) {
			log.error(e);
			IagoResponse.setResponse(response, e);
		}
	
		return datasets;
	}
		
    @CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Adicionar nova fonte de dados", nickname = "add_source", 
    		notes="Esta rota permite adicionar uma nova fonte de dados")
    @RequestMapping(method = RequestMethod.POST, path="/add_source")	
	public void addDatabase(@Valid @RequestBody SourceConfig confg, HttpServletResponse response){
		try {
			this.dataSourceServ.createSourceConfig(confg);
		} catch (SourceConnectionException e) {
			log.error(e);
			IagoResponse.setResponse(response, e);
		}
	}

    @CrossOrigin(methods = {RequestMethod.GET})
    @ApiOperation(value = "Listar fontes de dados cadastradas", nickname = "list_databases", 
    		notes="Esta rota permite listar as fontes de dados cadastradas")
    @RequestMapping(method = RequestMethod.GET, path="/list_databases")	
	public String listDatabase() throws SourceListNotFound{
		List<Map<String, Object>> listDatabase = null;
		listDatabase = this.dataSourceServ.lisRepSGBDs();
			
		return new Gson().toJson(listDatabase);
	}
	
    @CrossOrigin(methods = {RequestMethod.GET})
    @ApiOperation(value = "Listar tipos de fontes de dados permitidas", nickname = "list_databases_types", 
    		notes="Esta rota permite listar os tipos de fontes de dados permitidas")
    @RequestMapping(method = RequestMethod.GET, path="/list_databases_types")	
	public String listDatabaseTypes(){
		List<Map<String, Object>> listDatabaseType = null;
		listDatabaseType = this.dataSourceServ.listSGBDs();
		
		return new Gson().toJson(listDatabaseType);
	}
	
    @CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Listar formatos de dados implementados", nickname = "list_formats", 
    		notes="Esta rota permite listar os formatos de dados implementados")
    @RequestMapping(method = RequestMethod.POST, path="/list_formats")	
	public String listFormats(){
		List<Map<String, Object>> listFormats = this.accessServ.listFormats();
		
		return new Gson().toJson(listFormats);
	}
	
    @CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Realizar preservação de um conjunto de dados", nickname = "preservar_dataset", 
    		notes="Esta rota permite preservar um conjunto de dados")
    @RequestMapping(method = RequestMethod.POST, path="/preservar_dataset")	
	public void preservarDataset(@Valid @RequestBody PreservarDataset preservarDataset, HttpServletResponse response){
		try {
			this.preservationServ.preservarDataset(preservarDataset);
		} catch (DatasetNotFoundException e) {
			log.error(e);
			IagoResponse.setResponse(response, e);
		}	
	}
    
    @CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Lista dos metadados de um conjunto de dados", nickname = "getMetadata", 
			notes="Esta rota permite listar os metadados de um conjunto de dados")
	@RequestMapping(method = RequestMethod.GET, path="/{datasetTitle}/about/{codigo}")		
	public String getMetadata(@PathVariable("datasetTitle") String datasetTitle, HttpServletResponse response){
    	String resultado = "";
		try {
			resultado = new Gson().toJson(this.accessServ.getMetadata(datasetTitle));
		} catch (DatasetNotFoundException e) {
			log.error(e);
			IagoResponse.setResponse(response, e);
		}
		
		return resultado;
	}
    
    @CrossOrigin(methods = {RequestMethod.POST})
	@ApiOperation(value = "Cadastra uma nova categoria para conjuntos de dados", nickname = "insert_category", 
	notes="Esta rota permite cadastrar uma nova categoria para conjuntos de dados")
    @RequestMapping(method = RequestMethod.POST, path="/insert_category")	
    public void insertNewCategory(@Valid @RequestBody Category category, HttpServletResponse response){
    	try {
			this.createServ.createCategory(category);
		} catch (CategoryAlreadyExistsException e) {
			log.error(e);
			IagoResponse.setResponse(response, e);
		}
    }
    
    @CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Lista o nomes dos atributos de um conjunto de dados", nickname = "getAttributesNames", 
	notes="Esta rota permite listar os nomes dos atributos de um conjunto de dados")
    @RequestMapping(method = RequestMethod.POST, path="/list_attributes_names")	
    public List<String> getAttributeNames(@Valid @RequestBody AttrubutesNamesRequest request, HttpServletResponse response){
    	try {
			return this.accessServ.getDatasetFiels(request.getDatasetName(), request.getVersion());
		} catch (DatasetNotFoundException e) {
			log.error(e);
			IagoResponse.setResponse(response, e);
			return new ArrayList<>();
		}
    }
    
    @CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Insere a descrição dos atributos do conjunto de dados", nickname = "setAttributesDescription", 
   	notes="Esta rota permite Inserir a descrição dos atributos do conjunto de dados")
    @RequestMapping(method = RequestMethod.POST, path="/insert_attributes_description")	
    public void insertAttributesDescription(@Valid @RequestBody AttributesDescriptionRequest description, HttpServletResponse response){
    	try {
			this.createServ.insertFieldDescription(description);
		} catch (DatasetNotFoundException e) {
			log.error(e);
			IagoResponse.setResponse(response, e);
		}
    }
    
    @CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Verifica a validade do login", nickname = "verifyLogin", 
   	notes="Verifica a validade do login")
    @RequestMapping(method = RequestMethod.POST, path="/verify_login")
    public Boolean verifyLogin(){
    	return true;
    }
    
    @CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Remove um Conjunto de Dados", nickname = "removeDataset", 
   	notes="Remove um Conjunto de Dados")
    @RequestMapping(method = RequestMethod.POST, path="/remove_dataset")	
    public void removeDataset(@Valid @RequestBody DatasetUri uri, HttpServletResponse response) {
    	try{
    		this.preservationServ.removeDataset(uri.getDatasetName());
    	}catch (DatasetNotFoundException e) {
			log.error(e);
			IagoResponse.setResponse(response, e);
		}
    }
}
