package br.com.llduran.cria_excel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompraFinalizadaDTO
{
	private String chave;
	private int codigoPassagem;
	private String nroCartao;
	private int codigoSegurancaCartao;
	private String valorPassagem;
	private LocalDate dataViagem;
	private String mensagem;
	private boolean pagamentoOK;
}
