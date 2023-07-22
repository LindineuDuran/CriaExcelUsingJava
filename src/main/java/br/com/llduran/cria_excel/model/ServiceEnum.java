package br.com.llduran.cria_excel.model;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ServiceEnum
{
	CompraFinalizadaService ("compra"),
	FilmeService ("filme"),
	PedidoService("pedido"),
	PessoaService ("pessoa");
	private String tipooperacao;
	private ServiceEnum (String tipoOperacao)
	{
		this.tipooperacao = tipoOperacao;
	}
	public String getTipooperacao()
	{
		return tipooperacao;
	}

	public static String getServiceByTipoOperacao(String tipoOperacao)
	{
		return Stream.of (ServiceEnum.values())
				.filter(t -> t.getTipooperacao().equals(tipoOperacao))
				.map(t -> t.name()).collect (Collectors.joining ("\r\n"));
	}
}
