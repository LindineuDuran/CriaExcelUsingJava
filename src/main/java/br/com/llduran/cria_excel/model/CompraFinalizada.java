package br.com.llduran.cria_excel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompraFinalizada
{
	@JsonProperty("compra_chave")
	private CompraChave compraChave;

	@JsonProperty("mensagem")
	private String mensagem;

	@JsonProperty("pagamento_ok")
	private boolean pagamentoOK;
}