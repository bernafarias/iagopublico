package br.com.sgdw.util.constantes;

public enum DatabaseDriver {

	POSTGRESQL("org.postgresql.Driver"),
	MYSQL("com.mysql.jdbc.Driver"),
	ORACLE("oracle.jdbc.OracleDriver"),
	SQLSERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	
	private String valor;
	
	DatabaseDriver(String valor) {
		this.valor = valor;
	}
	
	public String valor(){
		return valor;
	}
	
}
