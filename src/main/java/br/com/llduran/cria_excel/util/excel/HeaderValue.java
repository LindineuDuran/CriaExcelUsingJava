package br.com.llduran.cria_excel.util.excel;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class HeaderValue implements ObjectValue
{
	@Override
	public Cell processValue(XSSFWorkbook excelFile, Cell cell, Object value)
	{
		cell.setCellValue(value.toString());
		cell.setCellStyle(setStyle(excelFile));

		return cell;
	}

	@Override
	public CellStyle setStyle(XSSFWorkbook excelFile)
	{
		CellStyle style = excelFile.createCellStyle();
		XSSFFont font = excelFile.createFont();
		font.setBold(true);
		font.setFontHeight(12);
		font.setColor(HSSFColor.WHITE.index);
		style.setFont(font);
		style.setFillForegroundColor(IndexedColors.DARK_BLUE.index);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);

		return style;
	}
}