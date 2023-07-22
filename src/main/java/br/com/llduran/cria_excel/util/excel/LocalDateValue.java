package br.com.llduran.cria_excel.util.excel;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateValue implements ObjectValue
{
	@Override
	public Cell processValue(XSSFWorkbook excelFile, Cell cell, Object value)
	{
		cell.setCellValue(((LocalDate) value).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		cell.setCellStyle(setStyle(excelFile));

		return cell;
	}

	@Override
	public CellStyle setStyle(XSSFWorkbook excelFile)
	{
		CellStyle style = excelFile.createCellStyle();
		XSSFFont font = excelFile.createFont();
		font.setBold(true);
		font.setFontHeight(10);
		font.setColor(HSSFColor.GREEN.index);
		style.setFont(font);

		return style;
	}
}
