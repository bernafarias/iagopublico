package br.com.sgdw.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MongoCfg {

	private static String dbHost;

	private static String dbName;

	private static int port;

	private static String username;

	private static String password;

	private static Boolean enableAuth;

	@Autowired
	private MongoCfg(@Value("${app.mongodb.host}") String dbHost,
			@Value("${app.mongodb.database}") String dbName,
			@Value("${app.mongodb.port}") int port,
			@Value("${app.mongodb.username}") String username,
			@Value("${app.mongodb.password}") String password,
			@Value("${app.mongodb.enableauth}") Boolean enableAuth){
		MongoCfg.dbHost = dbHost;
		MongoCfg.dbName = dbName;
		MongoCfg.port = port;
		MongoCfg.username = username;
		MongoCfg.password = password;
		MongoCfg.enableAuth = enableAuth;
	}

	public static String getDbHost() {
		return dbHost;
	}

	public static String getDbName() {
		return dbName;
	}

	public static int getPort() {
		return port;
	}

	public static String getUsername() {
		return username;
	}

	public static String getPassword() {
		return password;
	}

	public static Boolean getEnableAuth() {
		return enableAuth;
	}

}
