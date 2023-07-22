package br.com.llduran.cria_excel;

import br.com.llduran.cria_excel.model.ServiceEnum;
import br.com.llduran.cria_excel.service.ExcelManagerService;
import br.com.llduran.cria_excel.service.IoUtilsService;
import br.com.llduran.cria_excel.service.RegexUtilsService;
import br.com.llduran.cria_excel.service.ServiceContext;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.List;

@SpringBootApplication
public class CriaExcelApplication implements CommandLineRunner
{

	@Autowired
	private IoUtilsService ioUtils;

	@Autowired
	private ExcelManagerService excelManager;

	@Autowired
	private RegexUtilsService regexUtils;

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

		// Obtêm lista de arquivos JSON
		List<File> arquivos = ioUtils.getFileListOf("json");

		// Obtêm nomes dos arquivos
		List<String> nomesArquivos = ioUtils.obtemNomesArquicos(arquivos);

		// Get the Stream from the List matching Pattern
		List<String> tipos = regexUtils.getStream(nomesArquivos);

		for (String tipo:tipos)
		{
			// Filtra arquivos por tipo
			List<File> arquivosTipo = ioUtils.filtraArquivosPorTipo(arquivos, tipo);

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
