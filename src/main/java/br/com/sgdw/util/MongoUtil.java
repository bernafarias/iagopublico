package br.com.sgdw.util;

import java.util.Arrays;

import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;

import br.com.sgdw.util.constantes.SystemMsg;

public class MongoUtil {
	
	static final Logger log = Logger.getLogger(MongoUtil.class); 
	
	private static final int WAIT_TIME = 120000;
	
	private static DB db;
	
	private MongoUtil() {
		
	}
	
	public static DB getDB(){
	
		try {
			MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
			builder.maxConnectionIdleTime(WAIT_TIME);
			builder.maxWaitTime(WAIT_TIME);
			builder.maxConnectionLifeTime(WAIT_TIME);
			builder.socketKeepAlive(true);
			builder.retryWrites(true);
			MongoClientOptions opts = builder.build();
			
			if(db == null){	
				MongoClient mongoClient;
				if(MongoCfg.getEnableAuth()){
					MongoCredential credential = MongoCredential.createCredential(MongoCfg.getUsername(), MongoCfg.getDbName(), MongoCfg.getPassword().toCharArray());
					mongoClient = new MongoClient(new ServerAddress(MongoCfg.getDbHost(), MongoCfg.getPort()), Arrays.asList(credential), opts);
				}else{
					mongoClient = new MongoClient(MongoCfg.getDbHost(), opts); 
				}
				log.info(SystemMsg.CONECTANDO_MONGO.valor());
				db = mongoClient.getDB(MongoCfg.getDbName());
			}	
			return db;
			
		}catch (MongoException e) {
			log.error(SystemMsg.ERRO_CONEXAO_MONGO.valor(), e);
		}
		
		return db;
	}
}
