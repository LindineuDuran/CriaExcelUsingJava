package br.com.llduran.cria_excel;

import br.com.llduran.cria_excel.model.ServiceEnum;
import br.com.llduran.cria_excel.service.*;
import br.com.llduran.cria_excel.util.excel.ExcelManager;
import br.com.llduran.cria_excel.util.IoUtils;
import br.com.llduran.cria_excel.util.RegexUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class CriaExcelApplication implements CommandLineRunner
{
	@Autowired
	private IoUtils ioUtils;

	@Autowired
	private ExcelManager excelManager;

	@Autowired
	private ServiceContext serviceContext;

	public static void main(String[] args)
	{
		SpringApplication.run(CriaExcelApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception
	{
		// Cria arquivo Excel
		XSSFWorkbook[] excelFiles = new XSSFWorkbook[1];
		excelFiles[0] = excelManager.createExcelFile();

		// Busca lista de arquivos JSON
		List<File> arquivos = ioUtils.getFileListOf("json");

		// Obtêm nomes dos arquivos
		List<String> nomesArquivos = arquivos.stream().map(a -> a.getName()).collect(Collectors.toList());

		// Get the Stream from the List matching Pattern
		List<String> tipos = RegexUtils.getStream(nomesArquivos);

		for (String tipo:tipos)
		{
			// Filtra arquivos por tipo
			List<File> arquivosTipo = arquivos.stream().filter(a -> a.getName().contains(tipo)).collect(Collectors.toList());

			String nomeClasse = ServiceEnum.getServiceByTipoOperacao(tipo);
			serviceContext.setObjectService("br.com.llduran.cria_excel.service", nomeClasse);

			// Processa arquivos do tipo
			excelFiles[0] = serviceContext.processaListaArquivos(excelFiles[0], arquivosTipo);
		}

		// Se não tiver planilhas no arquivo
		if(!excelManager.hasSheets (excelFiles[0]))
		{
			excelManager.createSheet(excelFiles[0], "Planilha1") ;
		}

		// Salva arquivo Excel
		ioUtils.salvaExcelLocal(excelFiles[0], "ExcelTeste.xlsx");
	}
}
