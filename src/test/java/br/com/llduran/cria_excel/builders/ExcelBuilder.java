package br.com.llduran.cria_excel.builders;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Arrays;

public class ExcelBuilder
{
	private XSSFWorkbook arquivoExcel;

	private ExcelBuilder(){}

	public static ExcelBuilder umArquivoExcelComCabecalho()
	{
		ExcelBuilder builder = new ExcelBuilder();
		builder.arquivoExcel = new XSSFWorkbook();
		builder.arquivoExcel.createSheet("CompraFinalizada");
		Row row = builder.arquivoExcel.getSheet("CompraFinalizada").createRow(0);

		int[] columnCount = new int[1];
		columnCount[0] = 0;
		String[] atributos = {"Chave", "Código da Passagem", "Número do Cartão", "Código de Segurança do Cartão", "Valor da Passagem", "Data da Viagem", "Mensagem", "Pagamento Realizado"};

		Arrays.stream(atributos).forEach(a ->
		{
			Cell cell = row.createCell(columnCount[0]++);
			cell.setCellValue(a.toString());

			CellStyle style = builder.arquivoExcel.createCellStyle();
			XSSFFont font = builder.arquivoExcel.createFont();
			font.setBold(true);
			font.setFontHeight(12);
			style.setFont(font);

			cell.setCellStyle(style);
		});


		return builder;
	}

	public static ExcelBuilder umArquivoExcelComDados()
	{
		ExcelBuilder builder = umArquivoExcelComCabecalho();


		return builder;
	}

	public XSSFWorkbook agora() { return arquivoExcel; }
}
