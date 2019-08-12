package br.com.sgdw.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import br.com.sgdw.api.IagoResponse;
import br.com.sgdw.service.exception.DatasetNotFoundException;
import br.com.sgdw.util.constantes.Formats;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
@EnableAutoConfiguration(exclude={EmbeddedMongoAutoConfiguration.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class IagoResponseTest {
	
	static final Logger log = Logger.getLogger(IagoResponseTest.class); 
	
	@Autowired
	private MockMvc mockMvc;

	private static final String DATASET_NAME = "DatasetTeste";
	private static final String CONTENTDISPOSION = "Content-Disposition";
	
	private static final InputStream inputStream = new ByteArrayInputStream ("teste".getBytes());
	
	@Test
	public void setResponseTest() {	
		HttpServletResponse response = new MockHttpServletResponse();
		IagoResponse.setResponse(response, new DatasetNotFoundException());
		assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatus());
	}
	
	@Test
	public void getResponseDownloadTest() throws IOException {
		HttpServletResponse response = new MockHttpServletResponse();
		IagoResponse.getResponseDownload(Formats.JSON.getValor(), response, DATASET_NAME, inputStream);
		assertNotNull(response.getHeader(CONTENTDISPOSION));
		assertNotNull(response.getOutputStream());
		assertEquals(HttpStatus.SC_OK, response.getStatus());
	}
	
	@Test
	public void getResponseTest() throws IOException {
		
		HttpServletResponse response = new MockHttpServletResponse();
		IagoResponse.getResponse(Formats.JSON.getValor(), response, DATASET_NAME, inputStream);
		assertEquals(HttpStatus.SC_OK, response.getStatus());
		assertNotNull(response.getOutputStream());
		assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
		
		IagoResponse.getResponse(Formats.XML.getValor(), response, DATASET_NAME, inputStream);
		assertEquals(HttpStatus.SC_OK, response.getStatus());
		assertNotNull(response.getOutputStream());
		assertEquals(MediaType.APPLICATION_XML_VALUE, response.getContentType());
		
		IagoResponse.getResponse(Formats.CSV.getValor(), response, DATASET_NAME, inputStream);
		assertEquals(HttpStatus.SC_OK, response.getStatus());
		assertNotNull(response.getOutputStream());
		assertNotNull(response.getHeader(CONTENTDISPOSION));
		
		IagoResponse.getResponse("", response, DATASET_NAME, inputStream);
		assertEquals(HttpStatus.SC_OK, response.getStatus());
		assertNotNull(response.getOutputStream());
		assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
		
	}
	
	@Test
	public void redirectTest(){
		MvcResult mvcResult;
		try {
			mvcResult = mockMvc.perform(get("/documentacao")).andReturn();
			assertEquals(HttpStatus.SC_MOVED_TEMPORARILY, mvcResult.getResponse().getStatus());
		} catch (Exception e) {
			log.error(e);
		}
		
	}
}
