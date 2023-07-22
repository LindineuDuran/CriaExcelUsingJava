package br.com.llduran.cria_excel.service;

import br.com.llduran.cria_excel.exception.NegocioException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class IoUtilsService
{
	@Value("${llduran.storage.source-folder}")
	private String sourceFolder;

	@Value("${llduran.storage.target-folder}")
	private String targetFolder;

	@Autowired
	private ObjectManipulationService objectManipulation;

	public IoUtilsService()
	{
		this.objectManipulation = new ObjectManipulationService();
	}

	public String getSourceFolder()
	{
		File diretorio = new File(sourceFolder);

		if (!diretorio.exists())
		{
			diretorio.mkdirs(); // mkdir() cria somente um diretório, mkdirs() cria diretórios e subdiretórios.
		}

		return sourceFolder;
	}

	public String getTargetFolder()
	{
		File diretorio = new File(targetFolder);

		if (!diretorio.exists())
		{
			diretorio.mkdirs(); // mkdir() cria somente um diretório, mkdirs() cria diretórios e subdiretórios.
		}

		return targetFolder;
	}

	public List<File> getFileListOf(String extensao) throws IOException
	{
		String nomeDiretorio = getSourceFolder();
		File file = new File(nomeDiretorio);

		// Filtra lista de arquivos JSON
		List<File> arquivos = Arrays.stream(file.listFiles())
				.filter(f -> f.getName().contains(extensao))
				.collect(Collectors.toList());

		return arquivos;
	}

	public List<String> obtemNomesArquicos(List<File> arquivos)
	{
		// Obtêm nomes dos arquivos
		List<String> nomesArquivos = arquivos.stream().map(a -> a.getName()).collect(Collectors.toList());

		return nomesArquivos;
	}

	public List<File> filtraArquivosPorTipo(List<File> arquivos, String tipo)
	{
		// Filtra arquivos por tipo
		List<File> arquivosTipo = arquivos.stream().filter(a -> a.getName().contains(tipo)).collect(Collectors.toList());

		return arquivosTipo;
	}

	public String readFile(String filePath) throws IOException
	{
		Path path = Paths.get(filePath);

		String listFile = Files.lines(path, StandardCharsets.UTF_8).collect(Collectors.joining("\r\n"));

		return listFile;
	}

	public void writeStream(String filePath, String dados) throws IOException
	{
		Path path = Paths.get(filePath);
		if (dados.length() > 0)
		{
			try
			{
				Files.write(path, Collections.singleton(dados));
			}
			catch (IOException e)
			{
				throw new NegocioException("Erro ao gravar arquivo!", e);
			}
		}
	}

	public Object leArquivosJson(String filePath, String packageclasse, String nomeClasse, boolean temDTO)
	{
		try
		{
			// Ler arquivo Json
			String jsoncontent = readFile(filePath);

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

	public void salvaExcelLocal(XSSFWorkbook excelWorkBook, String nomeArquivo)
	{
		String filePath = getTargetFolder() + "\\" + nomeArquivo;

		apagaSeExiste(filePath);

		FileOutputStream fileOut = null;
		try
		{
			fileOut = new FileOutputStream(filePath);
			excelWorkBook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		}
		catch (FileNotFoundException e)
		{
			throw new NegocioException("Arquivo não encontrado!", e);
		}
		catch (IOException e)
		{
			throw new NegocioException("Erro ao gravar arquivo excel!", e);
		}
	}

	private static void apagaSeExiste(String nomeArquivo)
	{
		Path path = Paths.get(nomeArquivo);
		if (Files.exists(path))
		{
			try
			{
				Files.delete(path);
			}
			catch (IOException e)
			{
				throw new NegocioException("Erro ao apagar arquivo!", e);
			}
		}
	}
}
