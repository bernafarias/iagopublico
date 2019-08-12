package br.com.sgdw.util.constantes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Formats {

	JSON("json"),
	XML("xml"),
	CSV("csv");
	
	private String valor;
	
	Formats(String valor) {
		this.valor = valor;
	}

	public String getValor(){
		return this.valor;
	}
	
	public static Formats getFormat(String f){
		
		Formats format;
		
		switch(f){
			case "json":
				format = JSON;
				break;
				
			case "xml":
				format = XML;
				break;
				
			case "csv":
				format = CSV;
				break;
				
			default:
				format = JSON;
				break;
		}
		
		return format;
	}
	
	public static List<Map<String, Object>> getListaFormats(){
		List<Map<String, Object>> formats = new ArrayList<>();
		Map<String, Object> aux;
		
		for(Formats fmt : Formats.values()){
			aux = new HashMap<>();
			aux.put("Formato", fmt.getValor());
			formats.add(aux);
		}
		
		return formats;
	}
}
