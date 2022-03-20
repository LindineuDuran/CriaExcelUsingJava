package br.com.llduran.cria_excel;

import br.com.llduran.cria_excel.exception.NegocioException;
import br.com.llduran.cria_excel.model.CompraFinalizadaDTO;
import br.com.llduran.cria_excel.model.Filme;
import br.com.llduran.cria_excel.model.Pessoa;
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
		excelFiles[0] = processaComprasFinalizadas(excelFiles[0], arquivosCompra);

		// Filtra arquivos de Filme
		List<File> arquivosFilme = arquivos.stream().filter(a -> a.getName().contains("filme"))
				.collect(Collectors.toList());

		// Processa arquivos de filmes
		excelFiles[0] = processaFilmes(excelFiles[0], arquivosFilme);

		// Filtra arquivos de Pessoa
		List<File> arquivosPessoa = arquivos.stream().filter(a -> a.getName().contains("pessoa"))
				.collect(Collectors.toList());

		// Processa arquivos de pessoa
		excelFiles[0] = processaPessoas(excelFiles[0], arquivosPessoa);

		// Salva arquivo Excel
		SalvaPlanilha(excelFiles[0]);
	}

	private Object leArquivosJson(String filePath, String packageclasse, String nomeClasse, boolean temDTO)
	{
		try
		{
			// Ler arquivo Json
			String jsoncontent = ioUtils.readFile(filePath);

			// Desserializa JSON em objeto conforme o nomeClasse fornecido
			Object objeto = objectManipulation.desserializa(jsoncontent, packageclasse, nomeClasse.replace("DTO", ""));

			if (temDTO)
			{
				// Converte objeto em objeto DTO
				Object objetoDTO = objectManipulation.toDto(objeto, packageclasse, nomeClasse);
				return objetoDTO;
			}
			else
			{
				return objeto;
			}
		}
		catch (IOException e)
		{
			throw new NegocioException("Erro ao ler arquivo json!", e);
		}
		catch (ClassNotFoundException e)
		{
			throw new NegocioException("Classe não encontrada!", e);
		}
		catch (NoSuchMethodException e)
		{
			throw new NegocioException("Método não encontrado!", e);
		}
		catch (InvocationTargetException e)
		{
			throw new NegocioException("Não foi possivel construir o objeto de destino!", e);
		}
		catch (InstantiationException e)
		{
			throw new NegocioException("Não foi possivel instanciar a classe!", e);
		}
		catch (IllegalAccessException e)
		{
			throw new NegocioException("Acesso ilegal utilizando reflection!", e);
		}
		catch (NoSuchFieldException e)
		{
			throw new NegocioException("Campo ndo encontrado na classel", e);
		}
	}

	private void SalvaPlanilha(XSSFWorkbook excelFile)
	{
		String nomeArquivoExcel = ioUtils.getTargetFolder() + "\\ExcelTeste.xlsx";
		ioUtils.salvaExcelLocal(excelFile, nomeArquivoExcel);
	}

	private XSSFWorkbook processaComprasFinalizadas(XSSFWorkbook excelFile, List<File> arquivosCompra)
			throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException,
			ClassNotFoundException
	{
		// Cria arquivo Excel Temporário
		XSSFWorkbook[] excelFiles = new XSSFWorkbook[1];
		excelFiles[0] = excelFile;

		// Transforma arquivos JSON em objetos CompraDTO
		List<CompraFinalizadaDTO> compraFinalizadaDTOS = new ArrayList<>();
		arquivosCompra.forEach(a -> {
			CompraFinalizadaDTO compraDTO = (CompraFinalizadaDTO) leArquivosJson(a.getAbsolutePath(),
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
			// Insere linha na planilha à partir de dados do JSON lido
			excelFiles[0] = excelManager.writeDataLine(excelFiles[0], c);
		});

		return excelFiles[0];
	}

	private XSSFWorkbook processaFilmes(XSSFWorkbook excelFile, List<File> arquivosFilme)
			throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException,
			ClassNotFoundException
	{
		// Cria arquivo Excel Temporário
		XSSFWorkbook[] excelFiles = new XSSFWorkbook[1];
		excelFiles[0] = excelFile;

		// Transforma arquivos JSON em objetos Filme
		List<Filme> filmes = new ArrayList<>();
		arquivosFilme.forEach(a -> {
			Filme filme = (Filme) leArquivosJson(a.getAbsolutePath(),
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

	private XSSFWorkbook processaPessoas(XSSFWorkbook excelFile, List<File> arquivosPessoa)
			throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException,
			ClassNotFoundException
	{
		// Cria arquivo Excel Temporário
		XSSFWorkbook[] excelFiles = new XSSFWorkbook[1];
		excelFiles[0] = excelFile;

		// Transforma arquivos JSON em objetos Filme
		List<Pessoa> pessoas = new ArrayList<>();
		arquivosPessoa.forEach(a -> {
			Pessoa pessoa = (Pessoa) leArquivosJson(a.getAbsolutePath(),
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
