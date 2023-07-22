package br.com.llduran.cria_excel.service;

import br.com.llduran.cria_excel.model.Filme;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmeServiceTest
{
	@Spy
	private final IoUtilsService ioUtils = new IoUtilsService();

	private FilmeService filmeService;

	private List<File> arquivos;
	private List<String> nomesArquivos;

	@BeforeEach
	public void setup() throws IOException
	{
		/*
		  Mock an Autowired @Value field in Spring with Junit Mockito
		  https://roytuts.com/mock-an-autowired-value-field-in-spring-with-junit-mockito/
		 */
		ReflectionTestUtils.setField(ioUtils, "sourceFolder", "D:\\GitHub\\CriaExcelUsingJava\\dados_teste");

		// Busca lista de arquivos JSON
		arquivos = ioUtils.getFileListOf("json");

		// Obtêm nomes dos arquivos
		nomesArquivos = ioUtils.obtemNomesArquicos(arquivos);

		filmeService = new FilmeService();
	}

	@Test
	public void deveRetornarListaDeArquivosJsonDeFilmes() throws IOException
	{
		//cenário

		//ação
		// Filtra arquivos por tipo
		List<File> arquivosTipo = arquivos.stream().filter(a -> a.getName().contains("filme")).collect(Collectors.toList());

		// Obtêm nomes dos arquivos
		List<String> nomesArquivosTipo = ioUtils.obtemNomesArquicos(arquivosTipo);

		//verificação
		assertEquals(arquivosTipo.size(), 500);
		assertTrue(nomesArquivosTipo.contains("filme000.json"));
		assertTrue(nomesArquivosTipo.contains("filme010.json"));
		assertTrue(nomesArquivosTipo.contains("filme100.json"));
	}

	@Test
	public void deveRetornarListDeArquivosFilme() throws IOException
	{
		//cenário
		// Filtra arquivos por tipo
		List<File> arquivosFilme = arquivos.stream().filter(a -> a.getName().contains("filme")).collect(Collectors.toList());

		//ação
		// Transforma arquivos JSON em objetos CompraDTO
		List<Filme> filmes = new ArrayList<>();
		arquivosFilme.forEach(a ->
		{
			Filme filme = (Filme) ioUtils.leArquivosJson(a.getAbsolutePath(), "br.com.llduran.cria_excel.model", "Filme", true);
			filmes.add(filme);
		});

		//verificação
		assertEquals(filmes.size(), 500);
		assertTrue(filmes.get(0).getId() == 1);
		assertTrue(filmes.get(0).getTitulo().equals("Tellus Aenean Inc."));
		assertTrue(filmes.get(0).getDescricao().equals("lectus quis massa. Mauris vestibulum, neque sed dictum eleifend, nunc"));
	}

	@Test
	public void deveRetornarUmaInstanciaDeXSSFWorkbookComPlanilhaComDadosDeFilmes() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException
	{
		//cenário
		// Filtra arquivos por tipo
		List<File> arquivosFilme = arquivos.stream().filter(a -> a.getName().contains("filme")).collect(Collectors.toList());

		//ação
		XSSFWorkbook excelFiles = new XSSFWorkbook();
		excelFiles = filmeService.processaListaArquivos(excelFiles ,arquivosFilme);

		//verificação
		assertThat(excelFiles.getCTWorkbook().getSheets().getSheetList().size(), is(equalTo(1)));
		assertTrue(excelFiles.getCTWorkbook().getSheets().getSheetList().stream().filter(sheet -> sheet.getName().equals("Filme")).count() > 0);
		assertThat(excelFiles.getSheet("Filme").getPhysicalNumberOfRows(), is(equalTo(501)));
		assertThat(excelFiles.getSheet("Filme").getRow(1).getCell(1).toString(), is(equalTo("Tellus Aenean Inc.")));
		assertThat(excelFiles.getSheet("Filme").getRow(2).getCell(3).toString(),  is(equalTo("2022")));
		assertThat(excelFiles.getSheet("Filme").getRow(3).getCell(5).toString(), is(equalTo("imperdiet ornare. In")));
	}
}
