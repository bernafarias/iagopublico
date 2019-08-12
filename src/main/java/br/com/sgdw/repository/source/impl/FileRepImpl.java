package br.com.sgdw.repository.source.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import br.com.sgdw.domain.dto.SourceConfig;
import br.com.sgdw.repository.source.FileRep;
import br.com.sgdw.util.constantes.SystemMsg;

@Repository
public class FileRepImpl implements FileRep{

	static final Logger log = Logger.getLogger(FileRepImpl.class); 

	private static final String PREFIXSOURCE = "source::";

	@Override
	public Boolean checkFileConnection(SourceConfig cfg){
		File file = null;
		String url = cfg.getUrl();

		if(url.contains(PREFIXSOURCE)){
			url = url.replace(PREFIXSOURCE, "");
			Resource resource = new ClassPathResource(url);
			try{
				file = resource.getFile();
			}catch(IOException e){
				log.error(SystemMsg.OPEN_FILE_ERROR.valor(), e);
			}
		}else{
			file = new File(cfg.getUrl());
		}
		return file.exists();
	}

	@Override
	public List<Map<String, Object>> getDataFromFile(String data, Integer startPoint, String separator){

		File file = null;
		List<Map<String, Object>> retorno = new ArrayList<>();

		try{
			if(data.contains(PREFIXSOURCE)){
				data = data.replace(PREFIXSOURCE, "");
				Resource resource = new ClassPathResource(data);
				file = resource.getFile();
			}else{
				file = new File(data);
			}
		}catch(IOException e){
			log.error(SystemMsg.OPEN_FILE_ERROR.valor(), e);
		}

		try(BufferedReader reader = new BufferedReader(new FileReader(file))){

			List<String> columns = new ArrayList<>(Arrays.asList(reader.readLine().split(separator)));
			String lineText;
			Map<String, Object> aux;

			List<String> dataLine;

			lineText = reader.readLine();

			if(lineText != null){
				do{
					dataLine = new ArrayList<>(Arrays.asList(lineText.split(separator)));

					aux = new HashMap<>();

					for(int i=0; i<columns.size();i++){
						if(i >= dataLine.size()){
							aux.put(columns.get(i), "");
						}else{
							aux.put(columns.get(i), dataLine.get(i));
						}

					}
					retorno.add(aux);

					lineText = reader.readLine();

				}while(lineText != null);
			}

			return retorno;
			
		}catch(IOException e) {
			log.error(e);
		}

		return retorno;
	}
}
