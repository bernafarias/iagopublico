package br.com.sgdw.util.constantes;


public enum DatabaseDialect {

	POSTGRESQL("org.hibernate.dialect.PostgreSQLDialect"),
	MYSQL("org.hibernate.dialect.MySQLDialect"),
	ORACLE("org.hibernate.dialect.OracleDialect"),
	SQLSERVER("org.hibernate.dialect.SQLServer2008Dialect");
	
	private String valor;
	
	DatabaseDialect(String valor) {
		this.valor = valor;
	}
	
	public String valor(){
		return valor;
	}
}
