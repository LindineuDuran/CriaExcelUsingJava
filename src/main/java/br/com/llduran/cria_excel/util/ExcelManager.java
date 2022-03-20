package br.com.llduran.cria_excel.util;

import br.com.llduran.cria_excel.exception.NegocioException;
import br.com.llduran.cria_excel.model.HeaderEnum;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExcelManager
{
	public XSSFWorkbook createExcelFile()
	{
		XSSFWorkbook excelFile = new XSSFWorkbook();
		return excelFile;
	}

	public boolean sheetExists(XSSFWorkbook excelFile, String sheetname)
	{
		boolean exists = false;
		List<CTSheet> ctSheetArray = excelFile.getCTWorkbook().getSheets().getSheetList();
		if (ctSheetArray.stream().filter(sheet -> sheet.getName().equals(sheetname)).count() > 0)
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

	private CellStyle createHeaderStyle(XSSFWorkbook excelFile)
	{
		CellStyle style = excelFile.createCellStyle();
		XSSFFont font = excelFile.createFont();
		font.setBold(true);
		font.setFontHeight(12);
		style.setFont(font);
		return style;
	}

	private CellStyle createDataStyle (XSSFWorkbook excelFile)
	{
		CellStyle style = excelFile.createCellStyle();
		XSSFFont font = excelFile.createFont();
		font.setFontHeight(10);
		style.setFont(font);

		return style;
	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style)
	{
		XSSFSheet sheet = (XSSFSheet) row.getSheet();
		Cell cell = row.createCell(columnCount);

		if (value == null || value.toString().trim() == "NULL" || value.toString().trim() == "null")
		{
			cell.setCellValue(sheet.getWorkbook().getCreationHelper().createRichTextString(""));
		}
		else if (value instanceof BigDecimal)
		{
			DecimalFormat df = new DecimalFormat("####.##");
			cell.setCellValue(((BigDecimal) value).doubleValue());
		}
		else if (value instanceof Date)
		{
			cell.setCellValue((Date) value);
		}
		else if (value instanceof Calendar)
		{
			cell.setCellValue((Calendar) value);
		}
		else
		{
			// fixing possible CR/LF problem
			String fixedValue = value.toString();
			if (fixedValue != null)
			{
				fixedValue = fixedValue.replaceAll("\r\n", "\n");
			}
				cell.setCellValue(sheet.getWorkbook().getCreationHelper().createRichTextString(fixedValue));
			}

			cell.setCellStyle(style);
		}

		public XSSFWorkbook CriaPlanilhaCabecalho(XSSFWorkbook excelFile, String packageclasse, String nomeClasse)
				throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException,
				ClassNotFoundException
		{
			String nomeCompletoClasse = packageclasse + "." + nomeClasse;
			Class MinhaClasse = Class.forName(nomeCompletoClasse);
			Object meuObjeto = MinhaClasse.getConstructor().newInstance();

			excelFile = createSheet(excelFile, nomeClasse.replace("DTO",""));
			excelFile = writeHeaderLine(excelFile, meuObjeto.getClass());

			return excelFile;
		}

		public XSSFWorkbook writeHeaderLine(XSSFWorkbook excelFile, Class classe)
		{
			String sheetName = classe.getSimpleName().replace("DTO", "");
			Row row = excelFile.getSheet(sheetName).createRow(0);
			CellStyle style = createHeaderStyle(excelFile);

			int[] columnCount = new int[1];
			columnCount[0] = 0;
			Field[] atributos = classe.getDeclaredFields();
			Arrays.stream(atributos).forEach(a ->
			{
				String header = HeaderEnum
						.stream()
						.filter(h -> h.name().equals(a.getName()))
						.map(h -> h.getHeader())
						.collect(Collectors.joining("\r\n"));

				createCell(row, columnCount[0]++, header, style);
			});

			return excelFile;
		}

		public XSSFWorkbook writeDataLine(XSSFWorkbook excelFile, Object obj)
		{
			String sheetname = obj.getClass().getSimpleName().replace("DTO", "");

			int rowIndex = excelFile.getSheet(sheetname).getLastRowNum();
			Row row = excelFile.getSheet(sheetname).createRow(++rowIndex);

			CellStyle style = createDataStyle(excelFile);

			int[] columnCount = new int[1];
			columnCount[0] = 0;
			Field[] atributos = obj.getClass().getDeclaredFields();
			Arrays.stream(atributos).forEach(a ->
			{
				try
				{
					a.setAccessible(true);
					createCell(row, columnCount[0]++, a.get(obj), style);
				}
				catch (IllegalAccessException e)
				{
					throw new NegocioException("Acesso ilegal utilizando reflection!", e);
				}
			});

			return excelFile;
		}
	}