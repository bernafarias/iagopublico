package br.com.sgdw.repository.source;

import java.util.List;
import java.util.Map;

import br.com.sgdw.domain.dto.SourceConfig;

public interface SQLRep {
		
	List<Map<String, Object>> executarSql(String query, int inicio, SourceConfig cfg);

	Integer contarLinhas(String sql, SourceConfig cfg);
	
	Boolean checarConexaoSQL(SourceConfig cfg);
}
