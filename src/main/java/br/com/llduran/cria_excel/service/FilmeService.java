package br.com.llduran.cria_excel.service;

import br.com.llduran.cria_excel.model.Filme;
import br.com.llduran.cria_excel.util.ExcelManager;
import br.com.llduran.cria_excel.util.IoUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FilmeService implements ObjectService
{
	@Autowired
	private IoUtils ioUtils;

	@Autowired
	private ExcelManager excelManager;

	public XSSFWorkbook processaListaArquivos(XSSFWorkbook excelFile, List<File> arquivosFilme)
			throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException,
			ClassNotFoundException
	{
		// Cria arquivo Excel Temporário
		XSSFWorkbook[] excelFiles = new XSSFWorkbook[1];
		excelFiles[0] = excelFile;

		// Transforma arquivos JSON em objetos Filme
		List<Filme> filmes = new ArrayList<>();
		arquivosFilme.forEach(a -> {
			Filme filme = (Filme) ioUtils.leArquivosJson(a.getAbsolutePath(),
											"br.com.llduran.cria_excel.model",
											 "Filme",
											    false);
			filmes.add(filme);
		});

		// Cria planilha Excel com linha de cabeçalho para Filme
		excelFiles[0] = excelManager.CriaPlanilhaCabecalho(excelFiles[0],
												"br.com.llduran.cria_excel.model",
												"Filme");

		// Consome dados da lista de Filmes
		filmes.forEach(c -> {
			// Insere linha na planilha à partir de dados do JSON lido
			excelFiles[0] = excelManager.writeDataLine(excelFiles[0], c);
		});

		return excelFiles[0];
	}
}
