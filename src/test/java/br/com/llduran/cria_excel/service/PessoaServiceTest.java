package br.com.llduran.cria_excel.service;

import br.com.llduran.cria_excel.model.Pessoa;
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

public class PessoaServiceTest
{
	@Spy
	private final IoUtilsService ioUtils = new IoUtilsService();

	private PessoaService pessoaService;

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

		pessoaService = new PessoaService();
	}

	@Test
	public void deveRetornarListaDeArquivosJsonDePessoas() throws IOException
	{
		//cenário

		//ação
		// Filtra arquivos por tipo
		List<File> arquivosTipo = arquivos.stream().filter(a -> a.getName().contains("pessoa")).collect(Collectors.toList());

		// Obtêm nomes dos arquivos
		List<String> nomesArquivosTipo = ioUtils.obtemNomesArquicos(arquivosTipo);

		//verificação
		assertEquals(arquivosTipo.size(), 500);
		assertTrue(nomesArquivosTipo.contains("pessoa000.json"));
		assertTrue(nomesArquivosTipo.contains("pessoa010.json"));
		assertTrue(nomesArquivosTipo.contains("pessoa100.json"));
	}

	@Test
	public void deveRetornarListDeArquivosPessoa() throws IOException
	{
		//cenário
		// Filtra arquivos por tipo
		List<File> arquivosPessoa = arquivos.stream().filter(a -> a.getName().contains("pessoa")).collect(Collectors.toList());

		//ação
		// Transforma arquivos JSON em objetos CompraDTO
		List<Pessoa> pessoas = new ArrayList<>();
		arquivosPessoa.forEach(a ->
		{
			Pessoa pessoa = (Pessoa) ioUtils.leArquivosJson(a.getAbsolutePath(), "br.com.llduran.cria_excel.model", "Pessoa", true);
			pessoas.add(pessoa);
		});

		//verificação
		assertEquals(pessoas.size(), 500);
		assertTrue(pessoas.get(0).getId() == 1);
		assertTrue(pessoas.get(0).getPrimeiroNome().equals("Simone"));
		assertTrue(pessoas.get(0).getEmail().equals("interdum.ligula@protonmail.ca"));
	}

	@Test
	public void deveRetornarUmaInstanciaDeXSSFWorkbookComPlanilhaComDadosDePessoas() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException
	{
		//cenário
		// Filtra arquivos por tipo
		List<File> arquivosPessoa = arquivos.stream().filter(a -> a.getName().contains("pessoa")).collect(Collectors.toList());

		//ação
		XSSFWorkbook excelFiles = new XSSFWorkbook();
		excelFiles = pessoaService.processaListaArquivos(excelFiles ,arquivosPessoa);

		//verificação
		assertThat(excelFiles.getCTWorkbook().getSheets().getSheetList().size(), is(equalTo(1)));
		assertTrue(excelFiles.getCTWorkbook().getSheets().getSheetList().stream().filter(sheet -> sheet.getName().equals("Pessoa")).count() > 0);
		assertThat(excelFiles.getSheet("Pessoa").getPhysicalNumberOfRows(), is(equalTo(501)));
		assertThat(excelFiles.getSheet("Pessoa").getRow(1).getCell(1).toString(), is(equalTo("Simone")));
		assertThat(excelFiles.getSheet("Pessoa").getRow(2).getCell(3).toString(),  is(equalTo("auctor.nunc.nulla@outlook.couk")));
	}
}