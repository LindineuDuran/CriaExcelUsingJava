package br.com.llduran.cria_excel;

import br.com.llduran.cria_excel.exception.NegocioException;
import br.com.llduran.cria_excel.model.Filme;
import br.com.llduran.cria_excel.model.Pessoa;
import br.com.llduran.cria_excel.service.CompraFinalizadaService;
import br.com.llduran.cria_excel.service.FilmeService;
import br.com.llduran.cria_excel.service.PessoaService;
import br.com.llduran.cria_excel.util.ExcelManager;
import br.com.llduran.cria_excel.util.IoUtils;
import br.com.llduran.cria_excel.util.ObjectManipulation;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class CriaExcelApplication implements CommandLineRunner
{
	@Autowired
	private IoUtils ioUtils;

	@Autowired
	private ObjectManipulation objectManipulation;

	@Autowired
	private ExcelManager excelManager;

	@Autowired
	private CompraFinalizadaService compraFinalizadaService;

	@Autowired
	private FilmeService filmeService;

	@Autowired
	private PessoaService pessoaService;

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

		// Filtra arquivos de Compra
		List<File> arquivosCompra = arquivos.stream().filter(a -> a.getName().contains("compra"))
				.collect(Collectors.toList());

		// Processa arquivos de Compras Finalizadas
		excelFiles[0] = compraFinalizadaService.processaListaArquivos(excelFiles[0], arquivosCompra);

		// Filtra arquivos de Filme
		List<File> arquivosFilme = arquivos.stream().filter(a -> a.getName().contains("filme"))
				.collect(Collectors.toList());

		// Processa arquivos de filmes
		excelFiles[0] = filmeService.processaListaArquivos(excelFiles[0], arquivosFilme);

		// Filtra arquivos de Pessoa
		List<File> arquivosPessoa = arquivos.stream().filter(a -> a.getName().contains("pessoa"))
				.collect(Collectors.toList());

		// Processa arquivos de pessoa
		excelFiles[0] = pessoaService.processaListaArquivos(excelFiles[0], arquivosPessoa);

		// Salva arquivo Excel
		ioUtils.salvaExcelLocal(excelFiles[0], "ExcelTeste.xlsx");
	}
}
