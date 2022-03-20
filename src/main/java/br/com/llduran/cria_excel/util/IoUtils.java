package br.com.llduran.cria_excel.util;

import lombok.Data;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
@PropertySource("classpath:application.properties")
public class IoUtils
{
	@Value("${llduran.storage.source-folder}")
	private String sourceFolder;

	@Value("${llduran.storage.target-folder}")
	private String targetFolder;

	@Autowired
	private ObjectManipulation objectManipulation;

	public String getSourceFolder()
	{
		// // Pegar o diretorio "documents and settings" do usuário logado
		// String usuarioAtual = System.getProperty("user.home");

		String nomeDiretorio = sourceFolder;

		File diretorio = new File(nomeDiretorio);

		if (!diretorio.exists())
		{
			diretorio.mkdirs(); // mkdir() cria somente um diretório, mkdirs() cria diretórios e subdiretórios.
		}

		return nomeDiretorio;
	}

	public String getTargetFolder()
	{
		// // Pegar o diretorio "documents and settings" do usuário logado
		// String usuarioAtual = System.getProperty("user.home");

		String nomeDiretorio = targetFolder;

		File diretorio = new File(nomeDiretorio);

		if (!diretorio.exists())
		{
			diretorio.mkdirs(); // mkdir() cria somente um diretório, mkdirs() cria diretórios e subdiretórios.
		}

		return nomeDiretorio;
	}

	public List<File> getFileListOf(String extensao) throws IOException
	{
		String nomeDiretorio = getSourceFolder();
		File file = new File(nomeDiretorio);

		List<File> arquivos = Arrays.stream(file.listFiles())
				                    .filter(f -> f.getName().contains(extensao))
				                    .collect(Collectors.toList());

		return arquivos;
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
				e.printStackTrace();
			}
		}
	}

	public void salvaExcelLocal(XSSFWorkbook excelWorkBook, String nomeArquivo)
	{
		// creating local file just for testing. This part of code is working as expected.
		try
		{
			apagaSeExiste(nomeArquivo);

			FileOutputStream fileOut = new FileOutputStream(nomeArquivo);
			excelWorkBook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
				e.printStackTrace();
			}
		}
	}
}
