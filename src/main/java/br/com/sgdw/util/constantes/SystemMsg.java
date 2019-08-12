package br.com.sgdw.util.constantes;

public enum SystemMsg {

	//EXTERNAL MSG
	HISTORY_FIRST_INSERT("Primeira inserção"),
	HISTORY_AUTO_UPDATE("Atualização automática dos dados"),
	HISTORY_MANUAL_UPDATE("Atualização manual (não programada) dos dados"),
	
	//LOG - INFO
	MONGO_CONNECTING("Repository: Connecting to the Database... "),
	MONGO_GET_COLLECTION("Repository: Getting collection "),
	MONGO_INSERT_COLLECTION("Repository: Inserting into collection "),
	MONGO_GET_COLLECTION_BY_ID("Repository: Getting collection by id... "),
	MONGO_GET_COLLECTION_BY_QUERY("Repository: Gettiong by query at collection "),
	MONGO_COUNTING_ROW_NUMBERS("Repository: Counting number of rows of "),
	MONGO_UPDATING_VERSIONS("Repository: Updating versions of "),
	MONGO_CHANGING_PASSWORD("Repository: Changing password for "),
	MONGO_UPDATING_UPDATE_DATE("Repository: Changing new version date of "),
	MONGO_GET_COLLECTION_PAGE("Repository: Getting page of "),
	MONGO_INSERT_OBJ("Repository: Inserting object into collection "),
	MONGO_GET_SOURCE_CONFIG("Repository: Getting source config "),
	MONGO_UPDATING_METADATA("Repository: Updating metadata of "),
	MONGO_GET_METADATA("Repository: Getting metadata of "),
	
	//LOG - ERROR
	MONGO_ERROR("Repository: A error occured with the database... "),
	OPEN_FILE_ERROR("An error occurred while reading the file."),
	WRITE_FILE_ERROR("An error occurred while writing the file."),
	PARSE_ERROR("Parse error"),
	
	
	ARQUIVO_BANCO_NAO_ENCONTRADO("O arquivo de configuração do banco de origem não foi encontrado"),
	ERRO_DESCONHECIDO("Um erro desconhecido ocorreu"),
	ERRO_NULLPOINTER_CONF("Um erro ocorreu na conexão com o SGBD, verifique o arquivo de configuração do banco de origem está preenchido corretamente"),
	ERRO_CONEXAO_MONGO("Um erro ocorroreu na conexão com o MongoDB"),
	ERRO_HIBERNATE("Um erro ocorreu na consulta sql, verifique se a sintaxe está correta"),
	NAO_ENCONTRADO_MONGO("Este elemento não foi encontrado"),
	CONECTANDO_MONGO("Conectando com o MongoDB..."),
	ERRO_LEITURA_ARQUIVO("Ocorreu um erro na leitura do arquivo"),
	ERRO_ESCRITA_ARQUIVO("Ocorreu um erro na escrita do arquivo"),
	ARQUIVO_NAO_ENCONTRADO("Arquivo não encontrado");
	
	private String valor;
	
	SystemMsg(String valor) {
		
		this.valor = valor;
	}
	
	public String valor(){
		return valor;
	}
}
