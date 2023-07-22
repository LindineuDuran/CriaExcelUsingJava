package br.com.llduran.cria_excel.service;

import br.com.llduran.cria_excel.model.CompraFinalizadaDTO;
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

public class CompraFinalizadaServiceTest
{
	@Spy
	private final IoUtilsService ioUtils = new IoUtilsService();

	private CompraFinalizadaService compraFinalizada;

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

		compraFinalizada = new CompraFinalizadaService();
	}

	@Test
	public void deveRetornarListaDeArquivosJsonDeCompraFinalizada() throws IOException
	{
		//cenário

		//ação
		// Filtra arquivos por tipo
		List<File> arquivosTipo = arquivos.stream().filter(a -> a.getName().contains("compra")).collect(Collectors.toList());

		// Obtêm nomes dos arquivos
		List<String> nomesArquivosTipo = ioUtils.obtemNomesArquicos(arquivosTipo);

		//verificação
		assertEquals(arquivosTipo.size(), 500);
		assertTrue(nomesArquivosTipo.contains("compra000.json"));
		assertTrue(nomesArquivosTipo.contains("compra010.json"));
		assertTrue(nomesArquivosTipo.contains("compra100.json"));
	}

	@Test
	public void deveRetornarListDeArquivosCompraFinalzada() throws IOException
	{
		//cenário
		// Filtra arquivos por tipo
		List<File> arquivosCompra = arquivos.stream().filter(a -> a.getName().contains("compra")).collect(Collectors.toList());

		//ação
		// Transforma arquivos JSON em objetos CompraDTO
		List<CompraFinalizadaDTO> compraFinalizadaDTOS = new ArrayList<>();
		arquivosCompra.forEach(a ->
		{
			CompraFinalizadaDTO compraDTO = (CompraFinalizadaDTO) ioUtils.leArquivosJson(a.getAbsolutePath(), "br.com.llduran.cria_excel.model", "CompraFinalizadaDTO", true);
			compraFinalizadaDTOS.add(compraDTO);
		});

		//verificação
		assertEquals(compraFinalizadaDTOS.size(), 500);
		assertTrue(compraFinalizadaDTOS.get(0).getChave().equals("DBA18CD0-A11A-5E7D-D64E-6601093D4C17"));
		assertTrue(compraFinalizadaDTOS.get(0).getCompraCodigoPassagem() == 6840);
		assertTrue(compraFinalizadaDTOS.get(0).getCompraNroCartao().equals("6334238874556822539"));
	}

	@Test
	public void deveRetornarUmaInstanciaDeXSSFWorkbookComPlanilhaComDadosDeCompras() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException
	{
		//cenário
		// Filtra arquivos por tipo
		List<File> arquivosCompra = arquivos.stream().filter(a -> a.getName().contains("compra")).collect(Collectors.toList());

		//ação
		XSSFWorkbook excelFiles = new XSSFWorkbook();
		excelFiles = compraFinalizada.processaListaArquivos(excelFiles ,arquivosCompra);

		//verificação
		assertThat(excelFiles.getCTWorkbook().getSheets().getSheetList().size(), is(equalTo(1)));
		assertTrue(excelFiles.getCTWorkbook().getSheets().getSheetList().stream().filter(sheet -> sheet.getName().equals("CompraFinalizada")).count() > 0);
		assertThat(excelFiles.getSheet("CompraFinalizada").getPhysicalNumberOfRows(), is(equalTo(501)));
		assertThat(excelFiles.getSheet("CompraFinalizada").getRow(1).getCell(0).toString(), is(equalTo("DBA18CD0-A11A-5E7D-D64E-6601093D4C17")));
		assertThat(excelFiles.getSheet("CompraFinalizada").getRow(2).getCell(3).toString(), is(equalTo("597")));
		assertThat(excelFiles.getSheet("CompraFinalizada").getRow(3).getCell(6).toString(), is(equalTo("KAY23JOR9XJ")));
	}
}
