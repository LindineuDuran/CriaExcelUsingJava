package br.com.llduran.cria_excel.util.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class DoubleValue implements ObjectValue
{
	@Override
	public Cell processValue(XSSFWorkbook excelFile, Cell cell, Object value)
	{
		if (value instanceof Double)
		{
			DecimalFormat df = new DecimalFormat("####.##");
			cell.setCellValue(df.format((Double) value));
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

		DataFormat format = excelFile.createDataFormat();
		style.setDataFormat(format.getFormat("####.##"));

		return style;
	}
}
