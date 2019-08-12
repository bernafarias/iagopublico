package br.com.sgdw.util;

import br.com.sgdw.util.constantes.DatabaseDialect;
import br.com.sgdw.util.constantes.DatabaseDriver;


public class HibernateUtil {
	
	private HibernateUtil(){
		
	}

	public static String getDriver(String valor){
		
		String driver;
		
		switch (valor) {
		case "1":
				driver = DatabaseDriver.POSTGRESQL.valor();
				break;

		case "2":
				driver = DatabaseDriver.MYSQL.valor();
				break;

		case "3":
				driver = DatabaseDriver.ORACLE.valor();
				break;

		case "4":
				driver = DatabaseDriver.SQLSERVER.valor();
				break;

		default:
				driver = null;
				break;
		}
		
		return driver;
	}

	public static String getDialect(String valor){
		
		String dialect;
		
		switch (valor) {
		case "1":
				dialect = DatabaseDialect.POSTGRESQL.valor();
				break;

		case "2":
				dialect = DatabaseDialect.MYSQL.valor();
				break;

		case "3":
				dialect = DatabaseDialect.ORACLE.valor();
				break;

		case "4":
				dialect = DatabaseDialect.SQLSERVER.valor();
				break;

		default:
				dialect = null;
				break;
		}
		
		return dialect;
	}
}
