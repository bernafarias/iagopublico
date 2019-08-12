package br.com.sgdw.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.sgdw.domain.dto.SourceConfig;
import br.com.sgdw.repository.source.SQLRep;
import br.com.sgdw.util.constantes.SourceType;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
@EnableAutoConfiguration(exclude={EmbeddedMongoAutoConfiguration.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SQLServTest {
	
	private static final EmbeddedPostgres postgres = new EmbeddedPostgres();
	private static String url;
	
	@Autowired
	private SQLRep rep;

	@BeforeClass
	public static void init() throws IOException, SQLException {
		url = postgres.start("localhost", 5433, "dbName", "userName", "password");
		
		String sqlCreate = FileUtils.readFileToString(new File("src/main/resources/tests/testtable.sql")); 
		
		Connection con = DriverManager.getConnection(url);
		con.createStatement().execute(sqlCreate);
	}
	
	
	
	@Test
	public void testEmbeddedSQL() throws SQLException {
		
		SourceConfig cfg = new SourceConfig();
		cfg.setBdName("dbName");
		cfg.setDescription("Banco de Testes");
		cfg.setSourceIdName("1");
		cfg.setUrl("jdbc:postgresql://localhost:5433/dbName");
		cfg.setUser("userName");
		cfg.setPassword("password");
		cfg.setSourceType(SourceType.RELACIONAL_DATABASE);
		
		List<Map<String, Object>> a = rep.executarSql("SELECT * FROM teste", 0, cfg);
		
		for(Map<String, Object> m : a) {
			System.out.println(m.toString());
		}
		
	}
	
	@AfterClass
	public static void end() {
		postgres.stop();
	}
}
