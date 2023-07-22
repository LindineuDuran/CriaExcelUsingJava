package br.com.llduran.cria_excel.service;

import br.com.llduran.cria_excel.model.Filme;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FilmeService implements ObjectService
{
	private IoUtilsService ioUtils;

	private ExcelManagerService excelManager;

	public FilmeService()
	{
		this.ioUtils = new IoUtilsService();
		this.excelManager = new ExcelManagerService();
	}

	public XSSFWorkbook processaListaArquivos(XSSFWorkbook excelFile, List<File> arquivosFilme) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		// Cria arquivo Excel Temporário
		XSSFWorkbook[] excelFiles = new XSSFWorkbook[1];
		excelFiles[0] = excelFile;

		List<Filme> filmes = TransformaJsonEmObjetosFilme(arquivosFilme);

		// Cria planilha Excel com linha de cabeçalho para Filme
		excelFiles[0] = excelManager.createSheetWithHeader(excelFiles[0], "br.com.llduran.cria_excel.model", "Filme");

		// Consome dados da lista de Filmes
		filmes.forEach(c ->
		{
			// Insere linha na planilha à partir de dados do JSON lido
			excelFiles[0] = excelManager.writeDataLine(excelFiles[0], c);
		});

		return excelFiles[0];
	}

	public List<Filme> TransformaJsonEmObjetosFilme(List<File> arquivosFilme)
	{
		// Transforma arquivos JSON em objetos Filme
		List<Filme> filmes = new ArrayList<>();
		arquivosFilme.forEach(a ->
		{
			Filme filme = (Filme) ioUtils.leArquivosJson(a.getAbsolutePath(), "br.com.llduran.cria_excel.model", "Filme", false);
			filmes.add(filme);
		});

		return filmes;
	}
}