package br.com.llduran.cria_excel.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface ObjectService
{
	public XSSFWorkbook processaListaArquivos(XSSFWorkbook excelFile, List<File> arquivosTipo)
			throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException,
			IllegalAccessException;
}
