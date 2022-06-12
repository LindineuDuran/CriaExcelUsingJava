package br.com.llduran.cria_excel.service;

import br.com.llduran.cria_excel.model.Pessoa;
import br.com.llduran.cria_excel.util.excel.ExcelManager;
import br.com.llduran.cria_excel.util.IoUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PessoaService implements ObjectService
{
	private IoUtils ioUtils;
	private ExcelManager excelManager;

	public PessoaService()
	{
		this.ioUtils = new IoUtils();
		this.excelManager = new ExcelManager();
	}

	public XSSFWorkbook processaListaArquivos(XSSFWorkbook excelFile, List<File> arquivosPessoa)
			throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException,
			ClassNotFoundException
	{
		// Cria arquivo Excel Temporário
		XSSFWorkbook[] excelFiles = new XSSFWorkbook[1];
		excelFiles[0] = excelFile;

		// Transforma arquivos JSON em objetos Filme
		List<Pessoa> pessoas = new ArrayList<>();
		arquivosPessoa.forEach(a -> {
			Pessoa pessoa = (Pessoa) ioUtils.leArquivosJson(a.getAbsolutePath(),
												"br.com.llduran.cria_excel.model",
												 "Pessoa",
												    false);
			pessoas.add(pessoa);
		});

		// Cria planilha Excel com linha de cabeçalho para Filme
		excelFiles[0] = excelManager.CriaPlanilhaCabecalho(excelFiles[0],
												"br.com.llduran.cria_excel.model",
												 "Pessoa");

		// Consome dados da lista de Pessoa
		pessoas.forEach(c -> {
			// Insere linha na planilha à partir de dados do JSON lido
			excelFiles[0] = excelManager.writeDataLine(excelFiles[0], c);
		});

		return excelFiles[0];
	}
}
