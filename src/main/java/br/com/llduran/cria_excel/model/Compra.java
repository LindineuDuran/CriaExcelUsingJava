package br.com.llduran.cria_excel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Compra
{
	@JsonProperty("codigo_passagem")
	private int codigoPassagem;

	@JsonProperty("nro_cartao")
	private String nroCartao;

	@JsonProperty("codigo_seguranca_cartao")
	private int codigoSegurancaCartao;

	@JsonProperty("valor_passagem")
	private String valorPassagem;

	@JsonProperty("data_viagem")
	private LocalDate dataViagem;
}
