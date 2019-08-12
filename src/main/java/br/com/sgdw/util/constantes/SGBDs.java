package br.com.sgdw.util.constantes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum SGBDs {

	POSTGRESQL("1"),
	MYSQL("2"),
	ORACLE("3"),
	SQLSERVER("4");

	private String valor;
	
	SGBDs(String valor){
		this.valor = valor;
	}
	
	public String valor(){
		return valor;
	}
	
	public static String getLabel(Integer n){
		
		String label;
		
		switch(n.toString()){

		case "1":
				label = "PostgreSQL";
				break;

		case "2":
				label = "MySQL";
				break;

		case "3":
				label = "Oracle";
				break;

		case "4":
				label = "SQLServer";
				break;

		default:
				label = null;
				break;
		}
		return label;
	}
		
	public static List<Map<String, Object>> getListaSGBDs(){
		
		List<Map<String, Object>> sgbds = new ArrayList<>();
		int tamanho = SGBDs.values().length;
		Map<String, Object> aux;
		
		for(int i = 1; i <= tamanho; i++){
			aux = new HashMap<>();
			aux.put("SGBD", getLabel(i));
			aux.put("id", i);
			
			sgbds.add(aux);
		}
		
		return sgbds;
	}
}
