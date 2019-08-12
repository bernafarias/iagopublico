package br.com.sgdw.util;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import br.com.sgdw.util.constantes.SystemMsg;

@Component
public class IagoCfg {

	static final Logger log = Logger.getLogger(IagoCfg.class); 

	private static String path;
	private static Boolean isSsh;
	private static String host;
	private static String username;
	private static String password;
	private static int port;
	private static String tempPath;

	@Autowired
	private IagoCfg(@Value("${app.path}") String path,
			@Value("${app.path.isSsh}") Boolean isSsh,
			@Value("${app.path.host}") String host,
			@Value("${app.path.username}") String username,
			@Value("${app.path.password}") String password,
			@Value("${app.path.port}") int port){		
		IagoCfg.path = path;
		IagoCfg.isSsh = isSsh;
		IagoCfg.host = host;
		IagoCfg.username = username;
		IagoCfg.password = password;
		IagoCfg.port = port;

		Resource resource = new ClassPathResource("temp/");
		try {
			IagoCfg.tempPath = resource.getFile().getPath();
		} catch (IOException e) {
			log.error(SystemMsg.OPEN_FILE_ERROR.valor(), e);
		}
	}

	public static String getPath() {
		return path;
	}

	public static Boolean getIsSsh() {
		return isSsh;
	}

	public static String getHost() {
		return host;
	}

	public static String getUsername() {
		return username;
	}

	public static String getPassword() {
		return password;
	}

	public static int getPort() {
		return port;
	}

	public static String getTempPath() {
		return tempPath;
	}
}
