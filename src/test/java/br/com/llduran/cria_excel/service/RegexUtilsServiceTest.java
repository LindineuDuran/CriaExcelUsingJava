package br.com.llduran.cria_excel.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegexUtilsServiceTest
{
	private RegexUtilsService regexUtils;

	@BeforeEach
	public void setup()
	{
		regexUtils = new RegexUtilsService();
	}

	@Test
	public void deveRetornarListaDeTiposDeJSON() throws Exception
	{
		//cenário
		List<String> tipos = Arrays.asList("compra", "filme", "pedido", "pessoa");
		List<String> nomesArquivos = Arrays.asList("compra000.json", "compra001.json", "compra002.json", "compra003.json",
				                                   "filme000.json", "filme001.json", "filme002.json", "filme003.json",
												   "pedido000.json", "pedido001.json", "pedido002.json", "pedido003.json",
												   "pessoa000.json", "pessoa001.json", "pessoa002.json", "pessoa003.json");

		//ação
		List<String> tiposTest = regexUtils.getStream(nomesArquivos);

		//verificação
		assertEquals(tiposTest.size(), tipos.size());
		assertTrue(CollectionUtils.isEqualCollection(tiposTest, tipos));
	}
}
