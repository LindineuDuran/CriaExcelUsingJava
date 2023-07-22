package br.com.llduran.cria_excel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompraFinalizadaDTO
{
	private String chave;
	private int compraCodigoPassagem;
	private String compraNroCartao;
	private int compraCodigoSegurancaCartao;
	private String compraValorPassagem;
	private LocalDate compraDataViagem;
	private String compraMensagem;
	private boolean compraPagamentoOK;
}