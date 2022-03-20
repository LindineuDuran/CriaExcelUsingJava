package br.com.llduran.cria_excel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompraChave
{
	@JsonProperty("chave")
	private String chave;

	@JsonProperty("compra")
	private Compra compra;
}
