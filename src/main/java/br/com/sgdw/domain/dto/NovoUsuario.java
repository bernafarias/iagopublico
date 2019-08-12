package br.com.sgdw.domain.dto;

import org.hibernate.validator.constraints.NotBlank;

public class NovoUsuario {
	
	@NotBlank
	private String novoNome;
	
	@NotBlank
	private String usuarioNovo;
	
	@NotBlank
	private String novaSenha;
	
	
	public String getNovoNome() {
		return novoNome;
	}

	public void setNovoNome(String novoNome) {
		this.novoNome = novoNome;
	}

	public String getUsuarioNovo() {
		return usuarioNovo;
	}

	public void setUsuarioNovo(String usuarioNovo) {
		this.usuarioNovo = usuarioNovo;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}
}
