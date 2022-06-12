package br.com.llduran.cria_excel.util.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeValue implements ObjectValue
{
	@Override
	public Cell processValue(XSSFWorkbook excelFile, Cell cell, Object value)
	{
		cell.setCellValue(((LocalDateTime) value).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
		cell.setCellStyle(setStyle(excelFile));

		return cell;
	}

	@Override
	public CellStyle setStyle(XSSFWorkbook excelFile)
	{
		CellStyle style = excelFile.createCellStyle();
		XSSFFont font = excelFile.createFont();
		font.setFontHeight(10);
		style.setFont(font);

		CreationHelper createHelper = excelFile.getCreationHelper();
		style.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy HH:mm:ss"));

		return style;
	}
}
