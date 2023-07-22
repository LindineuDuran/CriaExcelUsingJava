package br.com.llduran.cria_excel.service;

import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IoUtilsServiceTest
{
	@Spy
	private final IoUtilsService ioUtils = new IoUtilsService();

	@Test
	public void deveRetornarListaDeArquivosJSON() throws Exception
	{
		//cenário
		/*
		  Mock an Autowired @Value field in Spring with Junit Mockito
		  https://roytuts.com/mock-an-autowired-value-field-in-spring-with-junit-mockito/
		 */
		ReflectionTestUtils.setField(ioUtils, "sourceFolder", "D:\\eclipse-workspace\\cria_excel\\dados_teste");

		//ação
		List<File> arquivos = ioUtils.getFileListOf("json");
		List<String> nomesArquivos = ioUtils.obtemNomesArquicos(arquivos);

		//verificação
		assertEquals(nomesArquivos.size(), 2000);
		assertTrue(nomesArquivos.contains("compra000.json"));
		assertTrue(nomesArquivos.contains("filme010.json"));
		assertTrue(nomesArquivos.contains("pessoa100.json"));
	}
}
