package br.com.llduran.cria_excel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Pessoa
{
	@JsonProperty("id")
	private long id;

	@JsonProperty("primeiro_nome")
	private String primeiroNome;

	@JsonProperty("ultimo_nome")
	private String ultimoNome;

	@JsonProperty("email")
	private String email;

	@JsonProperty("ativo")
	private boolean ativo;
}
