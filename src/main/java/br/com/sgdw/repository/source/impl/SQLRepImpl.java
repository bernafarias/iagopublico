package br.com.sgdw.repository.source.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.sgdw.domain.dto.SourceConfig;
import br.com.sgdw.repository.source.SQLRep;
import br.com.sgdw.util.HibernateUtil;

@Repository
public class SQLRepImpl implements SQLRep{

	static final Logger log = Logger.getLogger(SQLRepImpl.class);

	private static final int MAXRESULT = 500;

	@Override
	public List<Map<String, Object>> executarSql(String sql, int inicio, SourceConfig cfg){	
		List<Map<String, Object>> dados = null;

		SessionFactory sessionFactory = this.initSession(cfg);
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		try{
			SQLQuery query = session.createSQLQuery(sql);
			query.setFirstResult(inicio);
			query.setMaxResults(MAXRESULT);
			query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

			dados = query.list();
		}catch(HibernateException e){
			log.error(e);
		}

		return dados;
	}

	@Override
	public Boolean checarConexaoSQL(SourceConfig cfg){	
		SessionFactory sessionFactory = this.initSession(cfg);
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Boolean resposta = false;
		try{
			resposta = true;
		}catch(HibernateException e){
			log.error(e);
			resposta = false;
		}
		return resposta;
	}

	@Override
	public Integer contarLinhas(String sql, SourceConfig cfg){
		Integer numeroDeLinhas = null;
		String sqlCount = this.montarCount(sql);

		SessionFactory sessionFactory = this.initSession(cfg);
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		try{
			numeroDeLinhas = ((Number) session.createSQLQuery(sqlCount).uniqueResult()).intValue();
		}catch(HibernateException e){
			log.error(e);
			e.printStackTrace();
		}
		return numeroDeLinhas;
	}

	private String montarCount(String sql){

		StringBuilder sqlCount = new StringBuilder();
		String[] words = sql.split(" ");
		Boolean achou = false;

		sqlCount.append("SELECT COUNT(*) ");

		for(String i:words){
			if("FROM".equalsIgnoreCase(i)){
				achou = true;
			}
			if(achou){
				sqlCount.append(" "+i);
			}
		}

		return sqlCount.toString();
	}

	private SessionFactory initSession(SourceConfig cfg){
		SessionFactory sessionFactory;

		Properties hibernateConf = new Properties();
		String sgbd = cfg.getSourceIdName();
		String url = cfg.getUrl();
		String username = cfg.getUser();
		String password = cfg.getPassword();

		hibernateConf.setProperty("hibernate.connection.driver_class", HibernateUtil.getDriver(sgbd));
		hibernateConf.setProperty("hibernate.dialect", HibernateUtil.getDialect(sgbd));
		hibernateConf.setProperty("hibernate.connection.url", url);
		hibernateConf.setProperty("hibernate.connection.username", username);
		hibernateConf.setProperty("hibernate.connection.password", password);

		Configuration conf = new Configuration();

		conf.setProperties(hibernateConf);

		ServiceRegistry registry = new StandardServiceRegistryBuilder()
				.applySettings(conf.getProperties()).build();

		sessionFactory= conf.buildSessionFactory(registry);

		return sessionFactory;
	}

}
