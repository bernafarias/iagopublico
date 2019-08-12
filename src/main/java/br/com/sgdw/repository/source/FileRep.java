package br.com.sgdw.repository.source;

import java.util.List;
import java.util.Map;

import br.com.sgdw.domain.dto.SourceConfig;

public interface FileRep {

	Boolean checkFileConnection(SourceConfig cfg);

	List<Map<String, Object>> getDataFromFile(String data, Integer startPoint, String separator);
}
