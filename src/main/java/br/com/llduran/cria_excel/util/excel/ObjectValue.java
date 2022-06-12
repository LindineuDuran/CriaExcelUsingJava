package br.com.llduran.cria_excel.util.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface ObjectValue
{
	public Cell processValue(XSSFWorkbook excelFile, Cell cell, Object value);
	public CellStyle setStyle(XSSFWorkbook excelFile);
}
