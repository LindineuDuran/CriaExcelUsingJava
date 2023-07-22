package br.com.llduran.cria_excel.service;

import br.com.llduran.cria_excel.model.Pedido;
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

public class PedidoServiceTest
{
	@Spy
	private final IoUtilsService ioUtils = new IoUtilsService();

	private PedidoService pedidoService;

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

		pedidoService = new PedidoService();
	}

	@Test
	public void deveRetornarListaDeArquivosJsonDePedidos() throws IOException
	{
		//cenário

		//ação
		// Filtra arquivos por tipo
		List<File> arquivosTipo = arquivos.stream().filter(a -> a.getName().contains("pedido")).collect(Collectors.toList());

		// Obtêm nomes dos arquivos
		List<String> nomesArquivosTipo = ioUtils.obtemNomesArquicos(arquivosTipo);

		//verificação
		assertEquals(arquivosTipo.size(), 500);
		assertTrue(nomesArquivosTipo.contains("pedido000.json"));
		assertTrue(nomesArquivosTipo.contains("pedido010.json"));
		assertTrue(nomesArquivosTipo.contains("pedido100.json"));
	}

	@Test
	public void deveRetornarListDeArquivosPedido() throws IOException
	{
		//cenário
		// Filtra arquivos por tipo
		List<File> arquivosPedido = arquivos.stream().filter(a -> a.getName().contains("pedido")).collect(Collectors.toList());

		//ação
		// Transforma arquivos JSON em objetos CompraDTO
		List<Pedido> pedidos = new ArrayList<>();
		arquivosPedido.forEach(a ->
		{
			Pedido pedido = (Pedido) ioUtils.leArquivosJson(a.getAbsolutePath(), "br.com.llduran.cria_excel.model", "Pedido", true);
			pedidos.add(pedido);
		});

		//verificação
		assertEquals(pedidos.size(), 500);
		assertTrue(pedidos.get(0).getId() == 1);
		assertTrue(pedidos.get(0).getCodigo().equals("C4748812-74C6-E758-E0F1-B4EE19E0E84D"));
		assertTrue(pedidos.get(0).getStatus().equals("CRIADO"));
	}

	@Test
	public void deveRetornarUmaInstanciaDeXSSFWorkbookComPlanilhaComDadosDePedidos() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException
	{
		//cenário
		// Filtra arquivos por tipo
		List<File> arquivosPedido = arquivos.stream().filter(a -> a.getName().contains("pedido")).collect(Collectors.toList());

		//ação
		XSSFWorkbook excelFiles = new XSSFWorkbook();
		excelFiles = pedidoService.processaListaArquivos(excelFiles ,arquivosPedido);

		//verificação
		assertThat(excelFiles.getCTWorkbook().getSheets().getSheetList().size(), is(equalTo(1)));
		assertTrue(excelFiles.getCTWorkbook().getSheets().getSheetList().stream().filter(sheet -> sheet.getName().equals("Pedido")).count() > 0);
		assertThat(excelFiles.getSheet("Pedido").getPhysicalNumberOfRows(), is(equalTo(501)));
		assertThat(excelFiles.getSheet("Pedido").getRow(1).getCell(1).toString(), is(equalTo("C4748812-74C6-E758-E0F1-B4EE19E0E84D")));
		assertThat(excelFiles.getSheet("Pedido").getRow(2).getCell(3).toString(),  is(equalTo("4,18")));
		assertThat(excelFiles.getSheet("Pedido").getRow(3).getCell(6).toString(), is(equalTo("02/09/2021 18:06:54")));
	}
}
