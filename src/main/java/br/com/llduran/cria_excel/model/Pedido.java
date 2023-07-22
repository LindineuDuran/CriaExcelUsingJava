package br.com.llduran.cria_excel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pedido
{
	@JsonProperty("id")
	private Long id;

	@JsonProperty("codigo")
	private String codigo;

	@JsonProperty("subtotal")
	private BigDecimal subtotal;

	@JsonProperty("taxa_frete")
	private Double taxaFrete;

	@JsonProperty("valor_total")
	private BigDecimal valorTotal;

	@JsonProperty("status")
	private String status;

	@JsonProperty("data_criacao")
	private LocalDateTime dataCriacao;

	@JsonProperty("data_confirmacao")
	private LocalDateTime dataConfirmacao;

	@JsonProperty("data_cancelamento")
	private LocalDateTime dataCancelamento;

	@JsonProperty("data_entrega")
	private LocalDateTime dataEntrega;
}