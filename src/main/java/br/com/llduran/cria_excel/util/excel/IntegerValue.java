package br.com.llduran.cria_excel.util.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class IntegerValue implements ObjectValue
{
	@Override
	public Cell processValue(XSSFWorkbook excelFile, Cell cell, Object value)
	{
		if (value instanceof Integer)
		{
			cell.setCellValue((Integer) value);
			cell.setCellStyle(setStyle(excelFile));
		}

		return cell;
	}

	@Override
	public CellStyle setStyle(XSSFWorkbook excelFile)
	{
		CellStyle style = excelFile.createCellStyle();
		XSSFFont font = excelFile.createFont();
		font.setFontHeight(10);
		style.setFont(font);

		return style;
	}
}
