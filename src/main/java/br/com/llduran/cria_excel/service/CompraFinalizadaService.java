package br.com.llduran.cria_excel.service;

import br.com.llduran.cria_excel.model.CompraFinalizadaDTO;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompraFinalizadaService implements ObjectService
{
	private IoUtilsService ioUtils;

	private ExcelManagerService excelManager;

	public CompraFinalizadaService()
	{
		this.ioUtils = new IoUtilsService();
		this.excelManager = new ExcelManagerService();
	}

	public XSSFWorkbook processaListaArquivos(XSSFWorkbook excelFile, List<File> arquivosCompra) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException
	{
		// Cria arquivo Excel Temporário
		XSSFWorkbook[] excelFiles = new XSSFWorkbook[1];
		excelFiles[0] = excelFile;

		List<CompraFinalizadaDTO> compraFinalizadaDTOS = TransformaJsonEmObjetosCompraDTO(arquivosCompra);

		// Cria planilha Excel com linha de cabeçalho para CompraFinalizadaDTO
		excelFiles[0] = excelManager.createSheetWithHeader(excelFiles[0], "br.com.llduran.cria_excel.model", "CompraFinalizadaDTO");

		// Consome dados da lista de Compras Finalizadas
		compraFinalizadaDTOS.forEach(c ->
		{
			// Insere linha na planilha a partir de dados do JSON lido
			excelFiles[0] = excelManager.writeDataLine(excelFiles[0], c);
		});

		return excelFiles[0];
	}

	public List<CompraFinalizadaDTO> TransformaJsonEmObjetosCompraDTO(List<File> arquivosCompra)
	{
		// Transforma arquivos JSON em objetos CompraDTO
		List<CompraFinalizadaDTO> compraFinalizadaDTOS = new ArrayList<>();
		arquivosCompra.forEach(a ->
		{
			CompraFinalizadaDTO compraDTO = (CompraFinalizadaDTO) ioUtils.leArquivosJson(a.getAbsolutePath(), "br.com.llduran.cria_excel.model", "CompraFinalizadaDTO", true);
			compraFinalizadaDTOS.add(compraDTO);
		});

		return compraFinalizadaDTOS;
	}
}