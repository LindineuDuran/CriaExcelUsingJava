package br.com.llduran.cria_excel.service;

import br.com.llduran.cria_excel.exception.NegocioException;
import br.com.llduran.cria_excel.model.HeaderEnum;
import br.com.llduran.cria_excel.util.excel.ObjectValueContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

@Service
public class ExcelManagerService
{
	private ObjectValueContext objectValueContext;

	public ExcelManagerService()
	{
		objectValueContext = new ObjectValueContext();
	}

	public XSSFWorkbook createExcelFile()
	{
		XSSFWorkbook excelFile = new XSSFWorkbook();
		return excelFile;
	}

	public boolean hasSheets(XSSFWorkbook excelFile)
	{
		boolean exists = false;
		List<CTSheet> ctSheetArray = getSheetList(excelFile);
		if (ctSheetArray.stream().count() > 0)
		{
			exists = true;
		}
		return exists;
	}

	public XSSFWorkbook createSheet(XSSFWorkbook excelFile, String sheetname)
	{
		if (!sheetExists(excelFile, sheetname))
		{
			excelFile.createSheet(sheetname);
		}

		return excelFile;
	}

	public boolean sheetExists(XSSFWorkbook excelFile, String sheetname)
	{
		boolean exists = false;
		List<CTSheet> ctSheetArray = getSheetList(excelFile);
		if (ctSheetArray.stream().filter(sheet -> sheet.getName().equals(sheetname)).count() > 0)
		{
			exists = true;
		}

		return exists;
	}

	public XSSFWorkbook createSheetWithHeader(XSSFWorkbook excelFile, String packageclasse, String nomeClasse)
			throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException,
			ClassNotFoundException
	{
		String nomeCompletoClasse = packageclasse + "." + nomeClasse;
		Class MinhaClasse = Class.forName(nomeCompletoClasse);
		Object meuObjeto = MinhaClasse.getConstructor().newInstance();

		excelFile = createSheet(excelFile, nomeClasse.replace("DTO", ""));
		excelFile = writeHeaderLine(excelFile, meuObjeto.getClass());

		return excelFile;
	}

	public XSSFWorkbook writeDataLine(XSSFWorkbook excelFile, Object obj)
	{
		String sheetname = obj.getClass().getSimpleName().replace("DTO", "");

		int rowIndex = excelFile.getSheet(sheetname).getLastRowNum();
		Row row = excelFile.getSheet(sheetname).createRow(++rowIndex);

		int[] columnCount = new int[1];
		columnCount[0] = 0;
		Field[] atributos = obj.getClass().getDeclaredFields();
		Arrays.stream(atributos).forEach(a -> {
			try
			{
				a.setAccessible(true);
				createCell(excelFile, row, columnCount[0]++, a.get(obj), "Data");
			}
			catch (IllegalAccessException e)
			{
				throw new NegocioException("Acesso ilegal utilizando reflection!", e);
			}
		});

		return excelFile;
	}

	private List<CTSheet> getSheetList(XSSFWorkbook excelFile)
	{
		return excelFile.getCTWorkbook().getSheets().getSheetList();
	}

	private void createCell(XSSFWorkbook excelFile, Row row, int columnCount, Object value, String tipoLinha)
	{
		XSSFSheet sheet = (XSSFSheet) row.getSheet();
		Cell cell = row.createCell(columnCount);

		String typeName = value.getClass().getTypeName();
		if(tipoLinha.equals("Header")) { typeName = tipoLinha;}
		try
		{
			objectValueContext.setObjectValue(typeName);
			cell = objectValueContext.processValue(excelFile, cell, value);
		}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
		catch (NoSuchMethodException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
		catch (InstantiationException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}

	private XSSFWorkbook writeHeaderLine(XSSFWorkbook excelFile, Class classe)
	{
		String sheetName = classe.getSimpleName().replace("DTO", "");
		Row row = excelFile.getSheet(sheetName).createRow(0);

		int[] columnCount = new int[1];
		columnCount[0] = 0;
		Field[] atributos = classe.getDeclaredFields();
		Arrays.stream(atributos).forEach(a -> createCell(excelFile, row, columnCount[0]++, HeaderEnum.getHeaderByName(a.getName()), "Header"));

		return excelFile;
	}
}
