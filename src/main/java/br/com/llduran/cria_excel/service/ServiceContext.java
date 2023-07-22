package br.com.llduran.cria_excel.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Component
public class ServiceContext
{
	private ObjectService service;

	// this can be set at runtime by the application preferences
	public void setObjectService(String packageName, String classNameService)
			throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException,
			IllegalAccessException
	{
		Class MinhaClasseService = Class.forName(packageName + "." + classNameService);
		Object meuServico = MinhaClasseService.getConstructor().newInstance();
		this.service = (ObjectService) meuServico;
	}

	// use the strategy
	public XSSFWorkbook processaListaArquivos(XSSFWorkbook excelFile, List<File> arquivosTipo)
			throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException,
			IllegalAccessException
	{
		return this.service.processaListaArquivos(excelFile, arquivosTipo);
	}
}