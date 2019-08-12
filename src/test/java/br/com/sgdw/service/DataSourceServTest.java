package br.com.sgdw.service;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;

import br.com.sgdw.api.AdminRest;
import br.com.sgdw.domain.dto.SourceConfig;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.service.exception.SourceConnectionException;
import br.com.sgdw.service.exception.SourceListNotFound;
import br.com.sgdw.util.constantes.MongoVariables;
import br.com.sgdw.util.constantes.SGBDs;
import br.com.sgdw.util.constantes.SourceType;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext
@EnableAutoConfiguration
@ActiveProfiles("test")
public class DataSourceServTest{
	
	@Autowired
	private AdminRest adminRest;
	
	@Autowired
	private MongoRep mongoRep;
	
	private static SourceConfig newSource1;
	
	private static SourceConfig newSource2;
	
	private static SourceConfig newSourceFake;
	
	@BeforeClass
	public static void initDataSourceServTest() throws IOException{
		newSource1 = new SourceConfig();
		newSource1.setSourceIdName("testSource1");
		newSource1.setUrl("src/main/resources");
		newSource1.setSourceType(SourceType.FILE);
		
		newSource2 = new SourceConfig();
		newSource2.setSourceIdName("testSource2");
		newSource2.setUrl("src/main/resources");
		newSource2.setSourceType(SourceType.FILE);
		
		newSourceFake = new SourceConfig();
		newSourceFake.setSourceIdName("testSourceFake");
		newSourceFake.setUrl("fake");
		newSourceFake.setSourceType(SourceType.FILE);
	}
	
	@Test
	public void test1NewSource() throws SourceConnectionException{
		adminRest.addDatabase(newSource1, null);
		adminRest.addDatabase(newSource2, null);
		
		Map<String, Object> source1 = mongoRep.getById(newSource1.getSourceIdName(), MongoVariables.SOURCE_CONF_COLLECTION.valor(), "sourceIdName");
		assertNotNull(source1);
		
		Map<String, Object> source2 = mongoRep.getById(newSource1.getSourceIdName(), MongoVariables.SOURCE_CONF_COLLECTION.valor(), "sourceIdName");
		assertNotNull(source2);
	
		assertEquals(newSource1.getUrl(), source1.get("url").toString());
		assertEquals(SourceType.FILE.toString(), source2.get("sourceType"));
		
		HttpServletResponse response = new MockHttpServletResponse();
		adminRest.addDatabase(newSourceFake, response);
		assertEquals(SourceConnectionException.STATUS, response.getStatus());
		
	}
	
	@Test
	public void test2ListSources() throws SourceListNotFound{
		List<Map<String, Object>> sourcesList;
		String listStr = adminRest.listDatabase();
		sourcesList = new Gson().fromJson(listStr, ArrayList.class);
		
		for(Map<String, Object> i : sourcesList){
			assertEquals(newSource1.getUrl(), i.get("url").toString());
			assertEquals(SourceType.FILE.toString(), i.get("sourceType"));
		}
	}
	
	@Test
	public void test3ListSGBDs(){
		List<Map<String, Object>> list;
		String listSrt = adminRest.listDatabaseTypes();
		list = new Gson().fromJson(listSrt, ArrayList.class);
		
		assertEquals(list.size(), SGBDs.values().length);
	}
}
