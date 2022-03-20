package br.com.llduran.cria_excel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Filme
{
	@JsonProperty("id")
	private long id;

	@JsonProperty("titulo")
	private String titulo;

	@JsonProperty("descricao")
	private String descricao;

	@JsonProperty("ano_lancamento")
	private String anoLancamento;

	@JsonProperty("duracao")
	private String duracao;

	@JsonProperty("texto_classificacao")
	private String textoClassificacao;
}
