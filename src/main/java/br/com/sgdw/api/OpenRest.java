package br.com.sgdw.api;

import java.io.InputStream;
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
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import br.com.sgdw.domain.Category;
import br.com.sgdw.domain.Suggestion;
import br.com.sgdw.domain.dto.Contact;
import br.com.sgdw.domain.dto.DatasetQuery;
import br.com.sgdw.domain.dto.DatasetQueryResult;
import br.com.sgdw.domain.dto.NewSugestion;
import br.com.sgdw.service.AccessServ;
import br.com.sgdw.service.FeedbackServ;
import br.com.sgdw.service.PreservationServ;
import br.com.sgdw.service.VersionServ;
import br.com.sgdw.service.exception.DatasetNotFoundException;
import br.com.sgdw.service.exception.DatasetPageNotFound;
import br.com.sgdw.util.constantes.Formats;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "/open")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/open")
public class OpenRest {
	
	static final Logger log = Logger.getLogger(OpenRest.class); 
	
	private static final String CONTENTDISPOSION = "Content-Disposition";
	private static final String ATTACHMENT = "attachment; filename=\"";

	@Autowired
	private AccessServ accessServ;

	@Autowired
	private VersionServ versionServ;

	@Autowired
	private PreservationServ preservationServ;

	@Autowired
	private FeedbackServ feedbackServ;

	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Retornar dados de um conjunto de dados", nickname = "buscar", 
	notes="Esta rota permite retornar dados de um conjunto de dados")
	@RequestMapping(method = RequestMethod.GET, path="/{datasetTitle}")
	public void buscarTudo(@PathVariable("datasetTitle") String datasetTitle, HttpServletResponse response){		
		InputStream is = this.accessServ.getCompleteDataset(datasetTitle, Formats.JSON.getValor());
		IagoResponse.getResponse(Formats.JSON.getValor(), response, datasetTitle, is);
	}
	
	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Retornar dados de um conjunto de dados por paginação (10000 registros)", nickname = "buscar", 
	notes="Esta rota permite retornar dados de um conjunto de dados por paginação (10000 registros)")
	@RequestMapping(method = RequestMethod.GET, path="/{datasetTitle}/page/{page}/version/{version}")
	public List<Map<String, Object>> getDatasetByPage(@PathVariable("datasetTitle") String datasetName, @PathVariable("page") Integer page, 
			@PathVariable("version") String version, HttpServletResponse response){
		List<Map<String, Object>> datasetPage = new ArrayList<>();
		try {
			datasetPage = this.accessServ.getDatasetByPage(datasetName, version, page);
		} catch (DatasetPageNotFound e) {
			log.error(e);
			IagoResponse.setResponse(response, e);
		}
		return datasetPage;
	}
	
	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Retornar dados de um conjunto de dados por paginação (10000 registros)", nickname = "buscar", 
	notes="Esta rota permite retornar dados de um conjunto de dados por paginação (10000 registros)")
	@RequestMapping(method = RequestMethod.GET, path="/preview/{datasetTitle}/version/{version}/limit/{limit}")
	public List<Map<String, Object>> preVisualizeData(@PathVariable("datasetTitle") String datasetName, @PathVariable("version") String version, 
			@PathVariable("limit") Integer limit, HttpServletResponse response){
		List<Map<String, Object>> datasetPage = new ArrayList<>();
		try {
			datasetPage = this.accessServ.preVisualizeData(datasetName, version, limit);
		} catch (DatasetNotFoundException e) {
			log.error(e);
			IagoResponse.setResponse(response, e);
		}
		return datasetPage;
	}

	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Retorna se um conjunto de dados está preservado", nickname = "verifica_preservacao", 
	notes="Esta rota permite retornar se um conjunto de dados está ou não preservado")
	@RequestMapping(method = RequestMethod.GET, path="/{datasetTitle}/verificar_preservacao")
	public Boolean verificarPreservacao(@PathVariable("datasetTitle") String datasetTitle, HttpServletResponse response){		
		Boolean preservacao = false;
		try {
			preservacao = this.preservationServ.verificarPreservacao(datasetTitle);
		} catch (DatasetNotFoundException e) {
			log.error(e);
			IagoResponse.setResponse(response, e);
		}
		return preservacao;
	}


	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Listar versões de um conjunto de dados", nickname = "buscarVersoes", 
	notes="Esta rota permite realizar listar as versões de um conjunto de dados")
	@RequestMapping(method = RequestMethod.GET, path="/{datasetTitle}/list_versions")
	public String buscarVersoes(@PathVariable("datasetTitle") String datasetTitle, HttpServletResponse response){
		IagoResponse.getResponse(Formats.JSON.getValor(), response, datasetTitle, null);
		return this.versionServ.listVersionsDataset(datasetTitle);
	}

	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Filtrar dados de um conjunto de dados", nickname = "buscarPorId", 
	notes="Esta rota permite filtrar os dados de um conjunto de dados")
	@RequestMapping(method = RequestMethod.GET, path="/{datasetTitle}/{id}")
	public String buscarPorId(@PathVariable("datasetTitle") String datasetTitle, @PathVariable("id") String id, HttpServletResponse response){
		IagoResponse.getResponse(Formats.JSON.getValor(), response, datasetTitle, null);
		return this.accessServ.getById(datasetTitle, id, Formats.JSON.getValor());	
	}

	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Retornar dados de uma versão em um formato específico", nickname = "buscarPorVersao", 
	notes="Esta rota permite retornar dados de uma versão em um formato específico")
	@RequestMapping(method = RequestMethod.GET, path="/{datasetTitle}/version/{versionId}")
	public void buscarPorVersao(@PathVariable("datasetTitle") String datasetTitle, @PathVariable("versionId") String versionId, HttpServletResponse response){
		InputStream is = null;
		try {
			is = this.versionServ.getDatasetByVersion(datasetTitle, versionId, Formats.JSON.getValor());
			IagoResponse.getResponse(Formats.JSON.getValor(), response, datasetTitle, is);
		} catch (DatasetNotFoundException e) {
			log.error(e);
			IagoResponse.setResponse(response, e);
		}
	}

	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Download de uma versão do conjunto de dados em um formato específico", nickname = "buscarPorVersaoDownload", 
	notes="Esta rota permite fazer download de uma versão do conjunto de dados em um formato específico")
	@RequestMapping(method = RequestMethod.GET, path="/{datasetTitle}/version/{versionId}/format/{format}/download")
	public void buscarPorVersaoDownload(@PathVariable("datasetTitle") String datasetTitle, @PathVariable("versionId") String versionId, 
			@PathVariable("format") String formato, HttpServletResponse response){
		InputStream dados = null;
		try {
			dados = this.versionServ.getDatasetByVersion(datasetTitle, versionId, formato);
			IagoResponse.getResponseDownload(formato, response, datasetTitle, dados);
		} catch (DatasetNotFoundException e) {
			log.error(e);
			IagoResponse.setResponse(response, e);
		}
	}

	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Download de parte dos dados em um formato específico", nickname = "buscarPorIdFormatoDownload", 
	notes="Esta rota permite fazer download de parte dos dados em um formato específico")
	@RequestMapping(method = RequestMethod.GET, path="/{datasetTitle}/{id}/format/{formato}/download")
	public String buscarPorIdFormatoDownload(@PathVariable("datasetTitle") String datasetTitle, 
			@PathVariable("id") String id, @PathVariable("formato") String formato, HttpServletResponse response){	
		return this.accessServ.getById(datasetTitle, id, formato);	
	}	

	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Download do conjunto de dados em um formato específico", nickname = "buscarTudoComFormatoDownload", 
	notes="Esta rota permite fazer download dos dados em um formato específico")
	@RequestMapping(method = RequestMethod.GET, path="/{datasetTitle}/format/{formato}/download")	
	public void buscarTudoComFormatoDownload(@PathVariable("datasetTitle") String datasetTitle, @PathVariable("formato") String formato,
			HttpServletResponse response){	
		InputStream dados = this.accessServ.getCompleteDataset(datasetTitle, formato);
		IagoResponse.getResponseDownload(formato, response, datasetTitle, dados);
	}

	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Lista dos conjuntos de dados (público)", nickname = "listDatasets", 
	notes="Esta rota permite listar os conjuntos de dados")
	@RequestMapping(method = RequestMethod.GET, path="/list_datasets")		
	public String listDatasets(HttpServletResponse response){
		IagoResponse.getResponse("json", response, null, null);
		return this.accessServ.getDatasetsNames();
	}
	/**
	 * 
	 * @deprecated(Refatorar o caminho)
	 * @param datasetTitle
	 * @param response
	 * @return
	 * @throws DatasetNotFoundException
	 */
	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Lista dos metadados de um conjunto de dados", nickname = "getMetadata", 
	notes="Esta rota permite listar os metadados de um conjunto de dados")
	@RequestMapping(method = RequestMethod.GET, path="/about/{datasetTitle}")
	@Deprecated
	public String getMetadata(@PathVariable("datasetTitle") String datasetTitle, HttpServletResponse response){
		String resultado = new String();
		try {
			if (!this.preservationServ.verificarPreservacao(datasetTitle)){
				IagoResponse.getResponse("json", response, null, null);
				resultado = new Gson().toJson(this.accessServ.getMetadata(datasetTitle));
			}else{
				response.setStatus(HttpServletResponse.SC_GONE);
				response.setHeader("Motivo", this.preservationServ.motivoPreservacao(datasetTitle));
				resultado = this.preservationServ.motivoPreservacao(datasetTitle);
			}
		} catch (DatasetNotFoundException e) {
			log.error(e);
			IagoResponse.setResponse(response, e);
		}
		return resultado;
	}

	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Lista dos metadados de uma versão do conjuntos de dados", nickname = "getMetadataVersion", 
	notes="Esta rota permite listar os metadados de uma versão do conjuntos de dados")
	@RequestMapping(method = RequestMethod.GET, path="/about/{datasetTitle}/{version}")		
	public String getMetadataVersion(@PathVariable("datasetTitle") String datasetTitle,
			@PathVariable("version") String version, HttpServletResponse response){
		IagoResponse.getResponse("json", response, null, null);
		return new Gson().toJson(this.versionServ.getMetadataVersion(datasetTitle, version));
	}
	
	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Lista dos metadados de um conjuntos de dados em formato dcat", nickname = "getMetadataDcat", 
	notes="Esta rota permite listar os metadados de uma versão do conjuntos de dados em formato dcat")
	@RequestMapping(method = RequestMethod.GET, path="/dcat/{datasetTitle}")		
	public Map<String, Object> getMetadataDcat(@PathVariable("datasetTitle") String datasetTitle, HttpServletResponse response){
		IagoResponse.getResponse("json", response, null, null);
		return this.accessServ.getMetadataDcat(datasetTitle);
	}
	
	@CrossOrigin(methods = {RequestMethod.POST})
	@ApiOperation(value = "Buscar conjuntos de dados por consulta personalizada (limite de 10000 registros)", nickname = "dataset_query", 
	notes="Esta rota permite buscar os conjuntos de dados por consulta personalizada (limite de 10000 registros)")
	@RequestMapping(method = RequestMethod.POST, path="/execute_query")	
	public DatasetQueryResult getDatasetByQuery(@Valid @RequestBody DatasetQuery query, HttpServletResponse response){
		DatasetQueryResult result = null;
		try {
			result = this.accessServ.getDatasetByQuery(query);
		} catch (DatasetPageNotFound e) {
			log.error(e);
			IagoResponse.setResponse(response, e);
		}
		return result;
	}
	
	@CrossOrigin(methods = {RequestMethod.POST})
	@ApiOperation(value = "Buscar conjuntos de dados por consulta personalizada e realizar o download (limite de 10000 registros)",
	nickname = "dataset_query_download", 
	notes="Esta rota permite buscar os conjuntos de dados por consulta personalizada e realizar o download (limite de 10000 registros)")
	@RequestMapping(method = RequestMethod.POST, path="/execute_query/download")	
	public List<Map<String, Object>> getDatasetByQueryDownload(@Valid @RequestBody DatasetQuery query, HttpServletResponse response){
		DatasetQueryResult result = null;
		try {
			result = this.accessServ.getDatasetByQuery(query);
			response.setHeader(CONTENTDISPOSION, ATTACHMENT +result.getDatasetName()+"_subconjunto."+Formats.JSON.getValor()+"\"");
		} catch (DatasetPageNotFound e) {
			log.error(e);
			IagoResponse.setResponse(response, e);
			return new ArrayList<>();
		}
		return result.getData();
	}
	
	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Listar formatos de dados implementados", nickname = "list_formats", 
	notes="Esta rota permite listar os formatos de dados implementados")
	@RequestMapping(method = RequestMethod.GET, path="/list_formats")	
	public List<Map<String, Object>> listFormats(HttpServletResponse response){
		return this.accessServ.listFormats();
	}

	@CrossOrigin(methods = {RequestMethod.POST})
	@ApiOperation(value = "Inserir nova sugestão", nickname = "insert_suggestion", 
	notes="Esta rota permite inserir sugestões para criação de novos conjuntos de dados")
	@RequestMapping(method = RequestMethod.POST, path="/insert_suggestion")
	public void insertNewSuggestion(@RequestBody NewSugestion newSugestion){
		this.feedbackServ.insertNewSugestion(newSugestion);
	}

	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Listar todas as sugestões", nickname = "list_suggestion", 
	notes="Esta rota permite listar todas as sugestões cadastradas para criação de novos conjuntos de dados")
	@RequestMapping(method = RequestMethod.GET, path="/list_suggestions")
	public List<Suggestion> listSuggestions(){
		return this.feedbackServ.listSuggestions();
	}

	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Lista todas as categorias para conjuntos de dados", nickname = "list_categories", 
	notes="Esta rota permite listar todas as categorias cadastradas")
	@RequestMapping(method = RequestMethod.GET, path="/list_categories")
	public List<Category> listCategories(){
		return this.accessServ.listCategories();
	}
	
	@CrossOrigin(methods = {RequestMethod.POST})
	@ApiOperation(value = "Enviar dados para contato", nickname = "send_contact", 
	notes="Esta rota permite Enviar dados para contato")
	@RequestMapping(method = RequestMethod.POST, path = "/send_contact")
	public void sendContact(@Valid @RequestBody Contact contact){
		this.accessServ.sendContact(contact);
	}
}
