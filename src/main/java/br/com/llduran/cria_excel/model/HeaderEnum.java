package br.com.llduran.cria_excel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum HeaderEnum
{
	chave("Chave"),
	codigoPassagem("Código da Passagem"),
	nroCartao("Número do Cartão"),
	codigoSegurancaCartao("Código de Segurança do Cartão"),
	valorPassagem("Valor da Passagem"),
	dataViagem("Data da Viagem"),
	mensagem("Mensagem"),
	pagamentoOK("Pagamento Realizado"),
	id("Id"),
	titulo("Título"),
	descricao("Descrição"),
	anoLancamento("Ano de Lançamento"),
	duracao("Duração"),
	textoClassificacao("Texto de Classificação"),
	primeiroNome("Primeiro Nome"),
	ultimoNome("Último Nome"),
	email("E-mail"),
	ativo("Ativo");

	private String header;

	private HeaderEnum(String header)
	{
		this.header = header;
	}

	/**
	 * Iterate the Enum Using Stream
	 * https://www.baeldung.com/java-enum-iteration
	 *
	 * @return
	 */
	public static Stream<HeaderEnum> stream()
	{
		return Stream.of(HeaderEnum.values());
	}
}
