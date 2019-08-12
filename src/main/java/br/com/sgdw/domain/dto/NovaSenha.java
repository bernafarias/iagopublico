package br.com.sgdw.domain.dto;

import org.hibernate.validator.constraints.NotBlank;

public class NovaSenha {
	
	@NotBlank
	private String usuarioAlvo;
	
	@NotBlank
	private String senhaNova;
	

	public String getUsuarioAlvo() {
		return usuarioAlvo;
	}

	public void setUsuarioAlvo(String usuarioAlvo) {
		this.usuarioAlvo = usuarioAlvo;
	}

	public String getSenhaNova() {
		return senhaNova;
	}

	public void setSenhaNova(String senhaNova) {
		this.senhaNova = senhaNova;
	}
}
