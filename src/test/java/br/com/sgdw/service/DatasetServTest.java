package br.com.sgdw.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

import br.com.sgdw.api.AdminRest;
import br.com.sgdw.api.OpenRest;
import br.com.sgdw.domain.Category;
import br.com.sgdw.domain.FieldDescription;
import br.com.sgdw.domain.Metadata;
import br.com.sgdw.domain.dto.AttributesDescriptionRequest;
import br.com.sgdw.domain.dto.AttrubutesNamesRequest;
import br.com.sgdw.domain.dto.DatasetQuery;
import br.com.sgdw.domain.dto.DatasetQueryResult;
import br.com.sgdw.domain.dto.DatasetUri;
import br.com.sgdw.domain.dto.NovoDataset;
import br.com.sgdw.domain.dto.PreservarDataset;
import br.com.sgdw.domain.dto.SourceConfig;
import br.com.sgdw.domain.dto.UriValidacao;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.service.exception.CategoryAlreadyExistsException;
import br.com.sgdw.service.exception.DatasetAlreadyExistsException;
import br.com.sgdw.service.exception.DatasetListNotFoundException;
import br.com.sgdw.service.exception.DatasetNotFoundException;
import br.com.sgdw.service.scheduling.UpdateTask;
import br.com.sgdw.util.IagoCfg;
import br.com.sgdw.util.MongoUtil;
import br.com.sgdw.util.constantes.CollectionConfVariables;
import br.com.sgdw.util.constantes.DataType;
import br.com.sgdw.util.constantes.Formats;
import br.com.sgdw.util.constantes.Frequency;
import br.com.sgdw.util.constantes.MongoVariables;
import br.com.sgdw.util.constantes.SourceType;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext
@EnableAutoConfiguration
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DatasetServTest{
	
	static final Logger log = Logger.getLogger(DatasetServTest.class);
	
	@Autowired
	private MockMvc mockMvc; 
	
	@Autowired
	private AdminRest adminRest;
	
	@Autowired
	private OpenRest openRest;
	
	@Autowired
	private AccessServ accessServ;
	
	@Autowired
	private UpdateServ updateServ;
	
	@Autowired
	private MongoRep mongoRep;
	
	@Autowired
	private VersionServ versionServ;
	
	@Autowired
	private PreservationServ preservationServ;
	
	@Autowired
	private UpdateTask updateTask;
	
	@Autowired
	private IdentifierServ identifierServ;
	
	private static Category category;
	
	private static NovoDataset dataset;
	
	private static SourceConfig newSource;
	
	private static PreservarDataset preservarDataset;
	
	private static final int ROWS_NUMBER = 1000;
	private static final int ROWS_NUMBER_UPDATE = 2000;
	private static final int ROWS_TOTAL = 1001;
	private static final int ROWS_LIMIT = 100;
	private static final int PREVISUALIZE_LIMIT = 500;
	private static final int LOCAL_FIELDS_NUM = 9;
	private static final int TOTAL_FIELDS_NUM = 11;
	private static final int QUERY_RESULT_NUM = 3;
	
	private static final String DESCRIPTION_FIELD = "description";
	private static final String LASTVERSION_FIELD = "lastVersion";
	private static final String KEYWORS_FIELD = "keywords";
	private static final String PUBLISHER_FIELD = "publisher";
	private static final String CAMPO2_FIELD = "CAMPO_2";
	private static final String CAMPO3_FIELD = "CAMPO_3";
	private static final String CAMPO4_FIELD = "CAMPO_4";
	private static final String CAMPO5_FIELD = "CAMPO_5";
	private static final String CAMPO6_FIELD = "CAMPO_6";
	private static final String CAMPO7_FIELD = "CAMPO_7";
	private static final String CAMPO8_FIELD = "CAMPO_8";
	private static final String CAMPO9_FIELD = "CAMPO_9";
	private static final String CAMPO10_FIELD = "CAMPO_10";
	private static final String DESCRIPTION_FIELD_TEST = "Um campo para teste";
	
	private static final String CREATOR = "creator";
	private static final String PERIOD = "period";
	private static final String LANGUAGE = "language";
	private static final String SEPARATOR = "separator";
	private static final String FREQUENCY = "frequency";
	private static final String LICENSE = "license";
	private static final String DATETIMEFORMATS = "datetimeFormats";
	private static final String SPACIALCOVERAGE = "spacialCoverage";
	private static final String CONTACTPOINT = "contactPoint";
	private static final String DATASETCATEGORY = "category";
	private static final String NEXTUPDATE = "nextUpdate";
	
	private static final String FAKEURI = "fakeUri";
	
	private static final String OPENPATH = "/open/";
	
	private static final int SLEEP_TIME = 5;
			
	@BeforeClass
	public static void initDatasetServTest(){
		
		category = new Category();
		category.setBase64Image("asud98ne83nd0ad90dsasd");
		category.setDescription("Descrição para teste");
		category.setName("teste");
		
		newSource = new SourceConfig();
		newSource.setSourceIdName("testSource");
		newSource.setUrl("source::tests");
		newSource.setSourceType(SourceType.FILE);
		
		dataset = new NovoDataset();
		dataset.setCategoryName("teste");
		dataset.setCollectionIdColumnName("CAMPO_1");
		dataset.setContactPoint("8888-8888");
		dataset.setCreator("Wilker");
		dataset.setDatasetTitle("DatasetTeste");
		dataset.setDatasetRealName("Dataset Teste");
		dataset.setDateTimeFormats("HH:MM:SS");
		dataset.setDescription("Um conjunto de dados para testes");
		dataset.setIdSource("1");
		dataset.setKeywords("teste, experimento");
		dataset.setLanguage("PT-br");
		dataset.setLicense("OO02");
		dataset.setPeriod("2018");
		dataset.setPublisher("Aladin");
		dataset.setQueryData("dataset-test.csv");
		dataset.setSeparator(",");
		dataset.setSpatialCoverage("ufpe");
		dataset.setUpdateFrequency(Frequency.ANUAL);
		
		preservarDataset = new PreservarDataset();
		preservarDataset.setDatasetUri(dataset.getDatasetTitle());
		preservarDataset.setType("preservado");
		preservarDataset.setDescription("Preservação para teste");
	}
	
	
	
	/**
	 *#####################################################################################################################################
	  ############################################### TESTES RELACIONADOS AO CREATESERV ###################################################
	  #####################################################################################################################################
	 * @throws CategoryAlreadyExistsException
	 */
	@Test
	public void testA0CreateCategory() throws CategoryAlreadyExistsException{
		this.adminRest.insertNewCategory(category, null);
		
		Map<String, Object> ctgr = mongoRep.getById(category.getName(), MongoVariables.CATEGORY.valor(), "name");
		assertNotNull(ctgr);
		assertEquals(category.getBase64Image(), ctgr.get("base64Image"));
		assertEquals(category.getDescription(), ctgr.get(DESCRIPTION_FIELD));
		
		HttpServletResponse response = new MockHttpServletResponse();
		this.adminRest.insertNewCategory(category, response);
		assertEquals(HttpStatus.SC_CONFLICT, response.getStatus());
	}
		
	@Test
	public void testA1PublishDatasetCsv(){
		
		adminRest.addDatabase(newSource, null);
		adminRest.addDataset(dataset, null);
		
		Metadata metadata = mongoRep.getMetadata(dataset.getDatasetTitle());
		
		assertMetadata(metadata);
		
		Map<String, Object> conf = this.mongoRep.getById(dataset.getDatasetTitle(), MongoVariables.CONF_COLLECTION.valor(),
				CollectionConfVariables.COLLECTION_NAME.valor());
		
		List<Map<String, Object>> dadosMongo = mongoRep.readCollection(dataset.getDatasetTitle(), conf.get(LASTVERSION_FIELD).toString());
		assertNotNull(dadosMongo);
		assertTrue(dadosMongo.size() == ROWS_NUMBER);
		
		try {
			TimeUnit.SECONDS.sleep(SLEEP_TIME);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error(e);
		}
		
		String version = metadata.getLastVersion();
		
		assertTrue(new File(IagoCfg.getPath()+"/"+dataset.getDatasetTitle()+"/"+dataset.getDatasetTitle()+"_"+version+".json").exists());
		assertTrue(new File(IagoCfg.getPath()+"/"+dataset.getDatasetTitle()+"/"+dataset.getDatasetTitle()+"_"+version+".csv").exists());
		assertTrue(new File(IagoCfg.getPath()+"/"+dataset.getDatasetTitle()+"/"+dataset.getDatasetTitle()+"_"+version+".xml").exists());
		
		HttpServletResponse response = new MockHttpServletResponse();
		adminRest.addDataset(dataset, response);
		assertEquals(DatasetAlreadyExistsException.STATUS, response.getStatus());
		
	}
		
	@Test
	public void testA2InsertFieldDescription() throws DatasetNotFoundException{
		
		AttributesDescriptionRequest attributesDescriptionRequest = new AttributesDescriptionRequest();
		Map<String, FieldDescription> map = new HashMap<>();
		attributesDescriptionRequest.setDatasetName(dataset.getDatasetTitle());
		
		FieldDescription description = new FieldDescription();
		description.setFieldName(CAMPO2_FIELD);
		description.setTitle("Campo 2");
		description.setDescription(DESCRIPTION_FIELD_TEST);
		description.setDataType(DataType.TEXT);
		map.put(CAMPO2_FIELD, description);
		
		description = new FieldDescription();
		description.setFieldName(CAMPO3_FIELD);
		description.setTitle("Campo 3");
		description.setDescription(DESCRIPTION_FIELD_TEST);
		description.setDataType(DataType.TEXT);
		map.put(CAMPO3_FIELD, description);
		
		description = new FieldDescription();
		description.setFieldName(CAMPO4_FIELD);
		description.setTitle("Campo 4");
		description.setDescription(DESCRIPTION_FIELD_TEST);
		description.setDataType(DataType.TEXT);
		map.put(CAMPO4_FIELD, description);
		
		description = new FieldDescription();
		description.setFieldName(CAMPO5_FIELD);
		description.setTitle("Campo 5");
		description.setDescription(DESCRIPTION_FIELD_TEST);
		description.setDataType(DataType.TEXT);
		map.put(CAMPO5_FIELD, description);
		
		description = new FieldDescription();
		description.setFieldName(CAMPO6_FIELD);
		description.setTitle("Campo 6");
		description.setDescription(DESCRIPTION_FIELD_TEST);
		description.setDataType(DataType.TEXT);
		map.put(CAMPO6_FIELD, description);
		
		description = new FieldDescription();
		description.setFieldName(CAMPO7_FIELD);
		description.setTitle("Campo 7");
		description.setDescription(DESCRIPTION_FIELD_TEST);
		description.setDataType(DataType.TEXT);
		map.put(CAMPO7_FIELD, description);
		
		description = new FieldDescription();
		description.setFieldName(CAMPO8_FIELD);
		description.setTitle("Campo 8");
		description.setDescription(DESCRIPTION_FIELD_TEST);
		description.setDataType(DataType.TEXT);
		map.put(CAMPO8_FIELD, description);
		
		description = new FieldDescription();
		description.setFieldName(CAMPO9_FIELD);
		description.setTitle("Campo 9");
		description.setDescription(DESCRIPTION_FIELD_TEST);
		description.setDataType(DataType.TEXT);
		map.put(CAMPO9_FIELD, description);
		
		description = new FieldDescription();
		description.setFieldName(CAMPO10_FIELD);
		description.setTitle("Campo 10");
		description.setDescription(DESCRIPTION_FIELD_TEST);
		description.setDataType(DataType.TEXT);
		map.put(CAMPO10_FIELD, description);
		
		attributesDescriptionRequest.setAttributesDescription(map);
		
		this.adminRest.insertAttributesDescription(attributesDescriptionRequest, null);
		
		Metadata metadata = mongoRep.getMetadata(dataset.getDatasetTitle());
		
		assertMetadata(metadata);
		
		List<FieldDescription> list = metadata.getFieldDescriptions();
		assertEquals(LOCAL_FIELDS_NUM, list.size());
		
		for(FieldDescription f : list){
			assertNotNull(f.getFieldName());
			assertNotNull(f.getDescription());
		}
		
		HttpServletResponse response = new MockHttpServletResponse();
		attributesDescriptionRequest.setDatasetName(FAKEURI);
		this.adminRest.insertAttributesDescription(attributesDescriptionRequest, response);
		assertEquals(DatasetNotFoundException.STATUS, response.getStatus());
		
	}
	

	
	/**
	 *#####################################################################################################################################
	  ############################################### TESTES RELACIONADOS AO ACCESSERV ####################################################
	  #####################################################################################################################################
	 * @throws Exception 
	 */
	@Test
	public void testA3GetById() throws Exception{
		Metadata metadata = this.mongoRep.getMetadata(dataset.getDatasetTitle());
		String version = metadata.getLastVersion();
			
		String jsonDownload = this.openRest.buscarPorIdFormatoDownload(dataset.getDatasetTitle(), "2_"+version,
				Formats.JSON.getValor(), null);
		assertNotNull(jsonDownload);
		assertFalse(jsonDownload.isEmpty());
				
		String json = this.openRest.buscarPorId(dataset.getDatasetTitle(), "2_"+version, null);
		assertNotNull(json);
		assertFalse(json.isEmpty());
		json = json.replace("[", "").replace("]", "");
		
		JSONObject jsonObject = new JSONObject(json);
		assertNotNull(jsonObject.get(CAMPO2_FIELD));
		assertNotNull(jsonObject.get(CAMPO3_FIELD));
		assertNotNull(jsonObject.get(CAMPO4_FIELD));
		assertNotNull(jsonObject.get(CAMPO5_FIELD));
		assertNotNull(jsonObject.get(CAMPO6_FIELD));
		assertNotNull(jsonObject.get(CAMPO7_FIELD));
		assertNotNull(jsonObject.get(CAMPO8_FIELD));
		assertNotNull(jsonObject.get(CAMPO9_FIELD));
		assertNotNull(jsonObject.get(CAMPO10_FIELD));
		
		String csv = this.accessServ.getById(dataset.getDatasetTitle(), "2_"+version, Formats.CSV.getValor());
		assertNotNull(csv);
		assertFalse(csv.isEmpty());
		
		String xml = this.accessServ.getById(dataset.getDatasetTitle(), "2_"+version, Formats.XML.getValor());
		assertNotNull(xml);
		assertFalse(xml.isEmpty());
		
	}
	
	@Test
	public void testA4GetDatasetsNames(){
		JSONArray jsonArray = new JSONArray(this.openRest.listDatasets(null));
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		
		assertEquals(dataset.getDatasetTitle(), jsonObject.get("datasetTitle"));
		assertEquals(dataset.getKeywords(), jsonObject.get(KEYWORS_FIELD));
		assertEquals(dataset.getDescription(), jsonObject.get(DESCRIPTION_FIELD));
		assertEquals(dataset.getPublisher(), jsonObject.get(PUBLISHER_FIELD));
	}
	
	@Test
	public void testA5GetMetadata() throws DatasetNotFoundException{
		
		String metadadosJson = this.openRest.getMetadata(dataset.getDatasetTitle(), null);
		Map<String, Object> metadados = new Gson().fromJson(metadadosJson, HashMap.class);
		assertNotNull(metadados);
		assertEquals(dataset.getCreator(), metadados.get(CREATOR));
		assertEquals(dataset.getPeriod(), metadados.get(PERIOD));
		assertEquals(dataset.getKeywords(), metadados.get(KEYWORS_FIELD));
		assertEquals(dataset.getDescription(), metadados.get(DESCRIPTION_FIELD));
		assertEquals(dataset.getLanguage(), metadados.get(LANGUAGE));
		assertEquals(dataset.getSeparator(), metadados.get(SEPARATOR));
		assertEquals(dataset.getUpdateFrequency().toString(), metadados.get(FREQUENCY));
		assertEquals(dataset.getLicense(), metadados.get(LICENSE));
		assertEquals(dataset.getDateTimeFormats(), metadados.get(DATETIMEFORMATS));
		assertEquals(dataset.getSpatialCoverage(), metadados.get(SPACIALCOVERAGE));
		assertEquals(dataset.getPublisher(), metadados.get(PUBLISHER_FIELD));
		assertEquals(dataset.getContactPoint(), metadados.get(CONTACTPOINT));
		assertEquals(dataset.getCategoryName(), metadados.get(DATASETCATEGORY));
		assertEquals(ROWS_NUMBER, ((Double) metadados.get("rowsNumber")).intValue());
		assertEquals(1, ((Double) metadados.get("pagesNumber")).intValue());
		assertNotNull(metadados.get("creationDate"));
		assertNotNull(metadados.get(LASTVERSION_FIELD));
		assertNotNull(metadados.get(NEXTUPDATE));
		
		List<BasicDBObject> list = (List<BasicDBObject>) metadados.get("fieldDescriptions");
		assertEquals(LOCAL_FIELDS_NUM, list.size());
		
		for(Map<String, Object> f : list){
			assertNotNull(f.get("fieldName"));
			assertNotNull(f.get(DESCRIPTION_FIELD));
		}
		
		Map<String, Object> metadataAdmin = new Gson().fromJson(this.adminRest.getMetadata(dataset.getDatasetTitle(), null), HashMap.class);
		assertNotNull(metadataAdmin);
		assertEquals(dataset.getCreator(), metadataAdmin.get(CREATOR));
		assertEquals(dataset.getPeriod(), metadataAdmin.get(PERIOD));
		assertEquals(dataset.getKeywords(), metadataAdmin.get(KEYWORS_FIELD));
		assertEquals(dataset.getDescription(), metadataAdmin.get(DESCRIPTION_FIELD));
		assertEquals(dataset.getLanguage(), metadataAdmin.get(LANGUAGE));
		assertEquals(dataset.getSeparator(), metadataAdmin.get(SEPARATOR));
		assertEquals(dataset.getUpdateFrequency().toString(), metadataAdmin.get(FREQUENCY));
		assertEquals(dataset.getLicense(), metadataAdmin.get(LICENSE));
		assertEquals(dataset.getDateTimeFormats(), metadataAdmin.get(DATETIMEFORMATS));
		assertEquals(dataset.getSpatialCoverage(), metadataAdmin.get(SPACIALCOVERAGE));
		assertEquals(dataset.getPublisher(), metadataAdmin.get(PUBLISHER_FIELD));
		assertEquals(dataset.getContactPoint(), metadataAdmin.get(CONTACTPOINT));
		assertEquals(dataset.getCategoryName(), metadataAdmin.get(DATASETCATEGORY));
		assertEquals(ROWS_NUMBER, ((Double) metadataAdmin.get("rowsNumber")).intValue());
		assertEquals(1, ((Double) metadataAdmin.get("pagesNumber")).intValue());
		assertNotNull(metadataAdmin.get("creationDate"));
		assertNotNull(metadataAdmin.get(LASTVERSION_FIELD));
		assertNotNull(metadataAdmin.get(NEXTUPDATE));
		
		Map<String, Object> dcat = this.openRest.getMetadataDcat(dataset.getDatasetTitle(), null);
		assertNotNull(dcat);
		
		HttpServletResponse response = new MockHttpServletResponse();
		this.adminRest.getMetadata(FAKEURI, response);
		assertEquals(DatasetNotFoundException.STATUS, response.getStatus());
		
	}
	
	@Test
	public void testA6ListDatasetAdmin() throws DatasetListNotFoundException{
		JSONArray metadadosJson = new JSONArray(this.adminRest.listDatasets(null));
		JSONObject metadados = metadadosJson.getJSONObject(0);
		
		assertNotNull(metadados);
		assertEquals(dataset.getCreator(), metadados.get(CREATOR));
		assertEquals(dataset.getPeriod(), metadados.get(PERIOD));
		assertEquals(dataset.getKeywords(), metadados.get(KEYWORS_FIELD));
		assertEquals(dataset.getQueryData(), metadados.get("query"));
		assertEquals(dataset.getDescription(), metadados.get(DESCRIPTION_FIELD));
		assertEquals(dataset.getLanguage(), metadados.get(LANGUAGE));
		assertEquals(dataset.getSeparator(), metadados.get(SEPARATOR));
		assertEquals(dataset.getUpdateFrequency().toString(), metadados.get(FREQUENCY));
		assertEquals(dataset.getLicense(), metadados.get(LICENSE));
		assertEquals(dataset.getDateTimeFormats(), metadados.get(DATETIMEFORMATS));
		assertEquals(dataset.getCollectionIdColumnName(), metadados.get("idSource"));
		assertEquals(dataset.getSpatialCoverage(), metadados.get(SPACIALCOVERAGE));
		assertEquals(dataset.getPublisher(), metadados.get(PUBLISHER_FIELD));
		assertEquals(dataset.getContactPoint(), metadados.get(CONTACTPOINT));
		assertEquals(dataset.getCategoryName(), metadados.get(DATASETCATEGORY));
		assertNotNull(metadados.get(LASTVERSION_FIELD));
		assertNotNull(metadados.get(NEXTUPDATE));
		
	}
	
	@Test
	public void testA7GetDatasetByQuery(){
		DatasetQuery query = new DatasetQuery();
		query.setDatasetName(dataset.getDatasetTitle());
		query.setLimit(ROWS_LIMIT);
		query.setPage(1);
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(CAMPO8_FIELD, "(?<![\\w\\d])2.99(?![\\w\\d])");
		
		query.setParameters(parameters);
		
		DatasetQueryResult result;
		result = this.openRest.getDatasetByQuery(query, null);
		
		assertEquals(dataset.getDatasetTitle(), result.getDatasetName());
		assertEquals(1, result.getAtualPage().intValue());
		assertEquals(QUERY_RESULT_NUM , result.getRowsNumber().intValue());
		assertEquals(1, result.getPagesNumber().intValue());
		
		List<Map<String, Object>> dataResult = result.getData();
		assertEquals(QUERY_RESULT_NUM, dataResult.size());
		
		HttpServletResponse response = new MockHttpServletResponse();
		
		List<Map<String, Object>> resultDownload = this.openRest.getDatasetByQueryDownload(query, response);
		assertNotNull(resultDownload);
		assertNotEquals(0, resultDownload.size());
		
		response = new MockHttpServletResponse();
		query.setDatasetName(FAKEURI);
		this.openRest.getDatasetByQueryDownload(query, response);
		assertEquals(DatasetNotFoundException.STATUS, response.getStatus());
		
		response = new MockHttpServletResponse();
		this.openRest.getDatasetByQuery(query, response);
		assertEquals(DatasetNotFoundException.STATUS, response.getStatus());
	}
	
	@Test
	public void testA8GetCompleteDataset() throws Exception{
		
		MvcResult result = this.mockMvc.perform(get(OPENPATH+dataset.getDatasetTitle())).andReturn();
		assertEquals(HttpStatus.SC_OK, result.getResponse().getStatus());
		result.getResponse().getOutputStream().close();
		
		this.mockMvc.perform(get(OPENPATH+dataset.getDatasetTitle()+"/format/"+Formats.JSON.getValor()+
				"/download")).andReturn();
		assertEquals(HttpStatus.SC_OK, result.getResponse().getStatus());
		result.getResponse().getOutputStream().close();
		
		InputStream isJson = this.accessServ.getCompleteDataset(dataset.getDatasetTitle(), "json");
		
		JSONArray datasetJson;
		try {
			datasetJson = new JSONArray(IOUtils.toString(isJson));
			
			assertEquals(ROWS_NUMBER, datasetJson.length());
			isJson.close();
			
			InputStream isXML = this.accessServ.getCompleteDataset(dataset.getDatasetTitle(), "xml");
			
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(new StringReader(IOUtils.toString(isXML)));
			Element element = document.getRootElement();
			List<?> list = element.getChildren(dataset.getDatasetTitle());
			assertEquals(ROWS_NUMBER, list.size());
			isXML.close();
			
			InputStream isCSV = this.accessServ.getCompleteDataset(dataset.getDatasetTitle(), "csv");
			
			String datasetCSV = IOUtils.toString(isCSV);
			String[] datasetCSVArray = datasetCSV.split("\n");
			assertEquals(ROWS_TOTAL, datasetCSVArray.length);
			isCSV.close();
		
		} catch (JSONException | IOException | JDOMException e) {
			log.error(e);
		}
	
	}
	
	@Test
	public void testA9ListFormats(){
		List<Map<String, Object>> formatos = this.openRest.listFormats(null);
		assertEquals(Formats.values().length, formatos.size());
		
		String formatsAdmin = this.adminRest.listFormats();
		assertNotNull(formatsAdmin);
		assertFalse(formatsAdmin.isEmpty());
	}
	
	@Test
	public void testB0GetDatasetFiels() throws DatasetNotFoundException{
		Metadata metadata = mongoRep.getMetadata(dataset.getDatasetTitle());
		AttrubutesNamesRequest request = new AttrubutesNamesRequest();
		request.setDatasetName(dataset.getDatasetTitle());
		request.setVersion(metadata.getLastVersion());
		List<String> dados = this.adminRest.getAttributeNames(request, null);
		assertEquals(TOTAL_FIELDS_NUM, dados.size());
		
		HttpServletResponse response = new MockHttpServletResponse();
		request.setDatasetName(FAKEURI);
		this.adminRest.getAttributeNames(request, response);
		assertEquals(DatasetNotFoundException.STATUS, response.getStatus());
	}
	
	@Test
	public void testB1ListCategories(){
		List<Category> categories = this.openRest.listCategories();
		assertNotNull(categories);
		assertFalse(categories.isEmpty());
		
		Category c = categories.get(0);
		assertEquals(category.getBase64Image(), c.getBase64Image());
		assertEquals(category.getDescription(), c.getDescription());
		assertEquals(category.getName(), c.getName());
	}
	
	@Test
	public void testB2GetDatasetByPage(){
		List<Map<String, Object>> data;
		data = this.openRest.getDatasetByPage(dataset.getDatasetTitle(), 1, null, null);
		assertEquals(ROWS_NUMBER, data.size());
		
		HttpServletResponse response = new MockHttpServletResponse();
		this.openRest.getDatasetByPage(FAKEURI, 1, null, response);
		assertEquals(DatasetNotFoundException.STATUS, response.getStatus());
	}
	
	@Test
	public void testB3PreVisualizeData() throws DatasetNotFoundException{
		List<Map<String, Object>> data = this.openRest.preVisualizeData(dataset.getDatasetTitle(), null, PREVISUALIZE_LIMIT, null);
		assertEquals(PREVISUALIZE_LIMIT, data.size());
		
		HttpServletResponse response = new MockHttpServletResponse();
		this.openRest.preVisualizeData(FAKEURI, null, PREVISUALIZE_LIMIT, response);
		assertEquals(DatasetNotFoundException.STATUS, response.getStatus());
	}
	
	/**
	 *#####################################################################################################################################
	  ############################################### TESTES RELACIONADOS AO UPDATESERV ###################################################
	  #####################################################################################################################################
	  OBS: A função getAtualizacaoData() é estática, por isso, não foi adicionada aos testes
	 * @throws ParseException 
	 * @throws InterruptedException 
	 */
	
	@Test
	public void testB4AtualizacaoAuto(){
		
		Metadata metadata = this.mongoRep.getMetadata(dataset.getDatasetTitle());
		String atualVersion = metadata.getLastVersion();
		this.updateDateAux();
		
		try {
			this.updateTask.atualizarBase();
			TimeUnit.SECONDS.sleep(SLEEP_TIME);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error(e);
		} catch (ParseException e) {
			log.error(e);
		}
		
		metadata = this.mongoRep.getMetadata(dataset.getDatasetTitle());
		String newVersion = metadata.getLastVersion();
		
		assertNotEquals(atualVersion, newVersion);
		
		List<Map<String, Object>> dadosMongo = mongoRep.readCollection(dataset.getDatasetTitle(), null);
		assertNotNull(dadosMongo);
		assertTrue(dadosMongo.size() == ROWS_NUMBER_UPDATE);
		
		assertTrue(new File(IagoCfg.getPath()+"/"+dataset.getDatasetTitle()+"/"+dataset.getDatasetTitle()+"_"+newVersion+".json").exists());
		assertTrue(new File(IagoCfg.getPath()+"/"+dataset.getDatasetTitle()+"/"+dataset.getDatasetTitle()+"_"+newVersion+".csv").exists());
		assertTrue(new File(IagoCfg.getPath()+"/"+dataset.getDatasetTitle()+"/"+dataset.getDatasetTitle()+"_"+newVersion+".xml").exists());
	}
	
	private void updateDateAux(){
		DB db = MongoUtil.getDB();
		DBCollection collection = db.getCollection(MongoVariables.CONF_COLLECTION.valor());
		BasicDBObject query = new BasicDBObject();
		query.put(CollectionConfVariables.COLLECTION_NAME.valor(), dataset.getDatasetTitle());
		
		BasicDBObject builder = new BasicDBObject();
		builder.append(CollectionConfVariables.COLLECTION_NEXT_UPDATE.valor(), getNewDateAux());
		
		collection.update(query, new BasicDBObject("$set", builder), true, false);
	}
	
	private String getNewDateAux(){
		SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy h", Locale.US);
		Calendar calentar = Calendar.getInstance();
		calentar.setTime(new Date());
		
		return format.format(calentar.getTime());
	}
	

	
	/**
	 *#####################################################################################################################################
	  ############################################### TESTES RELACIONADOS AO VERSIONSERV ##################################################
	  #####################################################################################################################################
	 */
	@Test
	public void testB5ListVersionsDataset(){
		JSONArray jsonArray = new JSONArray(this.openRest.buscarVersoes(dataset.getDatasetTitle(), null));
		assertNotNull(jsonArray);
		
		for(int i=0;i<jsonArray.length();i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			assertNotNull(jsonObject.get("version"));
			assertNotNull(jsonObject.get("date"));
			assertNotNull(jsonObject.get(DESCRIPTION_FIELD));
		}
	}
	
	@Test
	public void testB6GetDatasetByVersion() throws Exception{
		Metadata metadata = this.mongoRep.getMetadata(dataset.getDatasetTitle());
		String version = metadata.getLastVersion();
		
		MvcResult result = this.mockMvc.perform(get(OPENPATH+dataset.getDatasetTitle()+"/version/"+version)).andReturn();
		assertEquals(HttpStatus.SC_OK, result.getResponse().getStatus());
		result.getResponse().getOutputStream().close();
		
		result = this.mockMvc.perform(get(OPENPATH+dataset.getDatasetTitle()+"/version/"+version+
										"/format/"+Formats.JSON.getValor()+"/download")).andReturn();
		assertEquals(HttpStatus.SC_OK, result.getResponse().getStatus());
		result.getResponse().getOutputStream().close();
		
		InputStream is;
		try {
			is = this.versionServ.getDatasetByVersion(dataset.getDatasetTitle(), version, "json");		
			JSONArray dados = new JSONArray(IOUtils.toString(is));
			assertEquals(ROWS_NUMBER, dados.length());
			is.close();
		} catch (DatasetNotFoundException | JSONException | IOException e) {
			log.error(e);
		}
		
	
	}
	
	@Test
	public void testB7GetMetadataVersion(){
		Metadata metadata = this.mongoRep.getMetadata(dataset.getDatasetTitle());
		String version = metadata.getLastVersion();
		
		String dadosStr = this.openRest.getMetadataVersion(dataset.getDatasetTitle(), version, null);
		Map<String, Object> dados = new Gson().fromJson(dadosStr, HashMap.class);
		assertNotNull(dados);
		assertNotNull(dados.get("version"));
		assertNotNull(dados.get("date"));
		assertNotNull(dados.get(DESCRIPTION_FIELD));
	}
	
	/**
	 *#####################################################################################################################################
	  ############################################### TESTES RELACIONADOS AO PRESERVATIONSERV #############################################
	  #####################################################################################################################################
	 * 
	 */
	@Test
	public void testB8PreservarDataset() throws DatasetNotFoundException{
		this.adminRest.preservarDataset(preservarDataset, null);
		Metadata metadata = this.mongoRep.getMetadata(dataset.getDatasetTitle());
		assertEquals(preservarDataset.getType(), metadata.getPreservation());
		assertEquals(preservarDataset.getDescription(), metadata.getPreservationDescription());
		
		HttpServletResponse response = new MockHttpServletResponse();
		PreservarDataset datasetFake = new PreservarDataset();
		datasetFake.setDatasetUri(FAKEURI);
		datasetFake.setDescription("teste de erro");
		datasetFake.setType("preservado");
		
		this.adminRest.preservarDataset(datasetFake, response);
		assertEquals(DatasetNotFoundException.STATUS, response.getStatus());
	}
	
	@Test
	public void testB9VerificarPreservacao() throws DatasetNotFoundException{
		assertTrue(this.openRest.verificarPreservacao(dataset.getDatasetTitle(), null));
		
		HttpServletResponse response = new MockHttpServletResponse();
		this.openRest.verificarPreservacao(FAKEURI, response);
		assertEquals(DatasetNotFoundException.STATUS, response.getStatus());
	}
	
	@Test
	public void testC0MotivoPreservacao() throws DatasetNotFoundException{
		assertEquals(preservarDataset.getDescription(), this.preservationServ.motivoPreservacao(dataset.getDatasetTitle()));
	}
	
	private void assertMetadata(Metadata metadata){
		
		assertNotNull(metadata);
		assertEquals(dataset.getDatasetRealName(), metadata.getDatasetRealName());
		assertEquals(dataset.getCreator(), metadata.getCreator());
		assertEquals(dataset.getPeriod(), metadata.getPeriod());
		assertEquals(dataset.getKeywords(), metadata.getKeywords());
		assertEquals(dataset.getQueryData(), metadata.getQuery());
		assertEquals(dataset.getDescription(), metadata.getDescription());
		assertEquals(dataset.getLanguage(), metadata.getLanguage());
		assertEquals(dataset.getSeparator(), metadata.getSeparator());
		assertEquals(dataset.getUpdateFrequency().toString(), metadata.getFrequency().toString());
		assertEquals(dataset.getLicense(), metadata.getLicense());
		assertEquals(dataset.getDateTimeFormats(), metadata.getDatetimeFormats());
		assertEquals(dataset.getCollectionIdColumnName(), metadata.getIdSource());
		assertEquals(dataset.getSpatialCoverage(), metadata.getSpacialCoverage());
		assertEquals(dataset.getPublisher(), metadata.getPublisher());
		assertEquals(dataset.getContactPoint(), metadata.getContactPoint());
		assertEquals(dataset.getCategoryName(), metadata.getCategory());
		assertEquals(ROWS_NUMBER, metadata.getRowsNumber().intValue());
		assertEquals(1, metadata.getPagesNumber().intValue());
		assertNotNull(metadata.getCreationDate());
		assertNotNull(metadata.getLastVersion());
		assertNotNull(metadata.getNextUpdate());
	}
	
	@Test
	public void testC1IndentifierServ() {
		String datasetName = "=DátãsetTest#e2&@%";
		UriValidacao uriValidacao = new UriValidacao();
		uriValidacao.setUri(datasetName);
		datasetName = this.adminRest.criarURI(uriValidacao);
		assertEquals(dataset.getDatasetTitle()+"2-", datasetName);
		datasetName = "=DátãsetTest#e&@%";
		datasetName = this.identifierServ.criarIdentificador(datasetName);
		assertNotEquals(dataset.getDatasetTitle(), datasetName);
		
		uriValidacao.setUri(dataset.getDatasetTitle());
		Boolean valid = this.adminRest.checarURI(uriValidacao);
		assertFalse(valid);
	}
	
	@Test
	public void testC2RemoveDataset() {
		HttpServletResponse response = new MockHttpServletResponse();
		DatasetUri uri = new DatasetUri();
		uri.setDatasetName(dataset.getDatasetTitle());
		this.adminRest.removeDataset(uri, response);
		
		Metadata metadata = this.mongoRep.getMetadata(dataset.getDatasetTitle());
		assertNull(metadata);
	}
	
	//@Test
	public void getAtualizacaoDataTest() {
		String result = updateServ.getAtualizacaoData(Frequency.POR_HORA);
		assertNotNull(result);
		
		result = updateServ.getAtualizacaoData(Frequency.DIARIO);
		assertNotNull(result);
		
		result = updateServ.getAtualizacaoData(Frequency.SEMANAL);
		assertNotNull(result);
		
		result = updateServ.getAtualizacaoData(Frequency.MENSAL);
		assertNotNull(result);
		
		result = updateServ.getAtualizacaoData(Frequency.SEMESTRAL);
		assertNotNull(result);
		
		result = updateServ.getAtualizacaoData(Frequency.ANUAL);
		assertNotNull(result);
		
		result = updateServ.getAtualizacaoData(Frequency.STATIC);
		assertEquals("", result);
	}
	
	@AfterClass
	public static void deleteFiles() {
		File file = new File(IagoCfg.getPath());
		FileUtils.deleteQuietly(file);
	}
}
