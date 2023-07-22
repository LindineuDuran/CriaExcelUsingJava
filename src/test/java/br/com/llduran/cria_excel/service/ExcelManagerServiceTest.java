package br.com.llduran.cria_excel.service;

import br.com.llduran.cria_excel.model.CompraFinalizadaDTO;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static br.com.llduran.cria_excel.builders.CompraFinalizadaBuilder.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExcelManagerServiceTest
{
	private ExcelManagerService excelManager;

	@BeforeEach
	public void setup()
	{
		excelManager = new ExcelManagerService();
	}

	@Test
	public void deveRetornarUmaInstanciaDeXSSFWorkbook()
	{
		//cenário
		//XSSFWorkbook excelFile = new XSSFWorkbook();

		//ação
		XSSFWorkbook excelFileTest = excelManager.createExcelFile();

		//verificação
		assertThat(excelFileTest, instanceOf(XSSFWorkbook.class));
	}

	@Test
	public void deveRetornarUmaInstanciaDeXSSFWorkbookComPlanilhaComCabecalho() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException
	{
		//cenário
		XSSFWorkbook[] excelFiles = new XSSFWorkbook[1];
		excelFiles[0] = excelManager.createExcelFile();

		//ação
		XSSFWorkbook excelFileTest = excelManager.createSheetWithHeader(excelFiles[0], "br.com.llduran.cria_excel.model", "CompraFinalizadaDTO");

		//verificação
		assertThat(excelFileTest.getCTWorkbook().getSheets().getSheetList().size(), is(equalTo(1)));
		assertTrue(excelFileTest.getCTWorkbook().getSheets().getSheetList().stream().filter(sheet -> sheet.getName().equals("CompraFinalizada")).count() > 0);
		assertThat(excelFileTest.getSheet("CompraFinalizada").getPhysicalNumberOfRows(), is(equalTo(1)));
		assertThat(excelFileTest.getSheet("CompraFinalizada").getRow(0).getCell(0).toString(), is(equalTo("Chave")));
		assertThat(excelFileTest.getSheet("CompraFinalizada").getRow(0).getCell(3).toString(), is(equalTo("Código de Segurança do Cartão")));
		assertThat(excelFileTest.getSheet("CompraFinalizada").getRow(0).getCell(7).toString(), is(equalTo("Pagamento Realizado")));
	}

	@Test
	public void deveRetornarUmaInstanciaDeXSSFWorkbookComPlanilhaComDados() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException
	{
		//cenário
		XSSFWorkbook[] excelFiles = new XSSFWorkbook[1];
		excelFiles[0] = excelManager.createExcelFile();

		XSSFWorkbook excelFileTest = excelManager.createSheetWithHeader(excelFiles[0], "br.com.llduran.cria_excel.model", "CompraFinalizadaDTO");
		List<CompraFinalizadaDTO> compraFinalizadaDTOS = new ArrayList<>();

		compraFinalizadaDTOS.add(umaCompraFinalizada().agora());
		compraFinalizadaDTOS.add(outraCompraFinalizada().agora());
		compraFinalizadaDTOS.add(maisOutraCompraFinalizada().agora());

		//ação
		compraFinalizadaDTOS.forEach(c ->
		{
			// Insere linha na planilha a partir de dados do JSON lido
			excelFiles[0] = excelManager.writeDataLine(excelFiles[0], c);
		});

		//verificação
		assertThat(excelFileTest.getCTWorkbook().getSheets().getSheetList().size(), is(equalTo(1)));
		assertTrue(excelFileTest.getCTWorkbook().getSheets().getSheetList().stream().filter(sheet -> sheet.getName().equals("CompraFinalizada")).count() > 0);
		assertThat(excelFileTest.getSheet("CompraFinalizada").getPhysicalNumberOfRows(), is(equalTo(4)));
		assertThat(excelFileTest.getSheet("CompraFinalizada").getRow(1).getCell(0).toString(), is(equalTo("DBA18CD0-A11A-5E7D-D64E-6601093D4C17")));
		assertThat(excelFileTest.getSheet("CompraFinalizada").getRow(2).getCell(3).toString(), is(equalTo("597")));
		assertThat(excelFileTest.getSheet("CompraFinalizada").getRow(3).getCell(6).toString(), is(equalTo("KAY23JOR9XJ")));
	}
}
