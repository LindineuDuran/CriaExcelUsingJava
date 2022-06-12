package br.com.llduran.cria_excel.service;

import br.com.llduran.cria_excel.model.CompraFinalizadaDTO;
import br.com.llduran.cria_excel.util.excel.ExcelManager;
import br.com.llduran.cria_excel.util.IoUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

//@Service
public class CompraFinalizadaService implements ObjectService
{
	private IoUtils ioUtils;
	private ExcelManager excelManager;

	public CompraFinalizadaService()
	{
		this.ioUtils = new IoUtils();
		this.excelManager = new ExcelManager();
	}

	public XSSFWorkbook processaListaArquivos(XSSFWorkbook excelFile, List<File> arquivosCompra)
			throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException,
			IllegalAccessException
	{
		// Cria arquivo Excel Temporário
		XSSFWorkbook[] excelFiles = new XSSFWorkbook[1];
		excelFiles[0] = excelFile;

		// Transforma arquivos JSON em objetos CompraDTO
		List<CompraFinalizadaDTO> compraFinalizadaDTOS = new ArrayList<>();
		arquivosCompra.forEach(a -> {
			CompraFinalizadaDTO compraDTO = (CompraFinalizadaDTO) ioUtils.leArquivosJson(a.getAbsolutePath(),
																			"br.com.llduran.cria_excel.model",
																			 "CompraFinalizadaDTO",
																			     true);
			compraFinalizadaDTOS.add(compraDTO);
		});

		// Cria planilha Excel com linha de cabeçalho para CompraFinalizadaDTO
		excelFiles[0] = excelManager.CriaPlanilhaCabecalho(excelFiles[0],
												"br.com.llduran.cria_excel.model",
												"CompraFinalizadaDTO");

		// Consome dados da lista de Compras Finalizadas
		compraFinalizadaDTOS.forEach(c -> {
			// Insere linha na planilha a partir de dados do JSON lido
			excelFiles[0] = excelManager.writeDataLine(excelFiles[0], c);
		});

		return excelFiles[0];
	}
}
