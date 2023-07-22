package br.com.llduran.cria_excel.service;

import br.com.llduran.cria_excel.model.Pedido;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService implements ObjectService
{
	private IoUtilsService ioUtils;

	private ExcelManagerService excelManager;

	public PedidoService()
	{
		this.ioUtils = new IoUtilsService();
		this.excelManager = new ExcelManagerService();
	}

	public XSSFWorkbook processaListaArquivos(XSSFWorkbook excelFile, List<File> arquivosPedido) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		// Cria arquivo Excel Temporário
		XSSFWorkbook[] excelFiles = new XSSFWorkbook[1];
		excelFiles[0] = excelFile;

		List<Pedido> pedidos = TransformaJsonEmObjetosPedido(arquivosPedido);

		// Cria planilha Excel com linha de cabeçalho para Pedido
		excelFiles[0] = excelManager.createSheetWithHeader(excelFiles[0], "br.com.llduran.cria_excel.model", "Pedido");

		// Consome dados da lista de Pedido
		pedidos.forEach(c ->
		{
			// Insere linha na planilha à partir de dados do JSON lido
			excelFiles[0] = excelManager.writeDataLine(excelFiles[0], c);
		});

		return excelFiles[0];
	}

	public List<Pedido> TransformaJsonEmObjetosPedido(List<File> arquivosPedido)
	{
		// Transforma arquivos JSON em objetos Pedido
		List<Pedido> pedidos = new ArrayList<>();
		arquivosPedido.forEach(a ->
		{
			Pedido pedido = (Pedido) ioUtils.leArquivosJson(a.getAbsolutePath(), "br.com.llduran.cria_excel.model", "Pedido", false);
			pedidos.add(pedido);
		});

		return pedidos;
	}
}