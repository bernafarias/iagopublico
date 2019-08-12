package br.com.sgdw.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.sgdw.api.OpenRest;
import br.com.sgdw.domain.Suggestion;
import br.com.sgdw.domain.dto.NewSugestion;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.util.constantes.MongoVariables;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext
@EnableAutoConfiguration
@ActiveProfiles("test")
public class FeedbackServTest{
	
	static final Logger log = Logger.getLogger(FeedbackServTest.class); 
	
	@Autowired
	private MongoRep mongoRep;
	
	private static NewSugestion newS1;
	private static NewSugestion newS2;
	
	private static final int SUGGESTIONS_NUM = 2;
	
	@Autowired
	private OpenRest openRest; 
	
	@BeforeClass
	public static void initFeedbackServTest() throws IOException{
			    
		newS1 = new NewSugestion();
		newS1.setDescription("A description for testing 1");
		newS1.setEmail("test1@iago.com");
		newS1.setName("Wilker Santos");
		
		newS2 = new NewSugestion();
		newS2.setDescription("A description for testing 2");
		newS2.setEmail("test2@iago.com");
		newS2.setName("Lairson Alencar");
	}
	
	@Test
	public void test1InsertSuggestion(){
		
		openRest.insertNewSuggestion(newS1);
		openRest.insertNewSuggestion(newS2);
		
		Map<String, Object> suggestion1 = mongoRep.getById(newS1.getEmail(), MongoVariables.SUGESTION_COLLECTION.valor(), "email");
		Map<String, Object> suggestion2 = mongoRep.getById(newS2.getEmail(), MongoVariables.SUGESTION_COLLECTION.valor(), "email");
		
		assertNotNull(suggestion1);
		assertEquals(newS1.getDescription(), suggestion1.get("description"));
		assertEquals(newS1.getName(), suggestion1.get("name"));
		
		assertNotNull(suggestion2);
		assertEquals(newS2.getDescription(), suggestion2.get("description"));
		assertEquals(newS2.getName(), suggestion2.get("name"));
	}
	
	@Test
	public void test2ListSuggestions(){
		
		List<Suggestion> suggestions = openRest.listSuggestions();
		assertEquals(SUGGESTIONS_NUM, suggestions.size());
		
		assertNotNull(suggestions.get(0));
		assertEquals(newS1.getDescription(), suggestions.get(0).getDescription());
		assertEquals(newS1.getName(), suggestions.get(0).getName());
		assertEquals(newS1.getEmail(), suggestions.get(0).getEmail());
		
		assertNotNull(suggestions.get(1));
		assertEquals(newS2.getDescription(), suggestions.get(1).getDescription());
		assertEquals(newS2.getName(), suggestions.get(1).getName());
		assertEquals(newS2.getEmail(), suggestions.get(1).getEmail());
		
	}
}
