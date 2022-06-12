package br.com.llduran.cria_excel.util.excel;

import br.com.llduran.cria_excel.model.ServiceEnum;
import br.com.llduran.cria_excel.service.ObjectService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class ObjectValueContext
{
	private ObjectValue objectValue;

	private String PACKAGE = "br.com.llduran.cria_excel.util.excel";

	public void setObjectValue(String typeName)
			throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException,
			IllegalAccessException
	{
		String nomeClasse = DataTypeEnum.getDataTypeValueByClassName(typeName);
		if(nomeClasse.equals("")) {nomeClasse =  "StringValue";}

		Class MinhaClasseService = Class.forName(PACKAGE + "." + nomeClasse);
		Object meuObjectValue = MinhaClasseService.getConstructor().newInstance();
		this.objectValue = (ObjectValue) meuObjectValue;
	}

	public Cell processValue(XSSFWorkbook excelFile, Cell cell, Object value)
	{
		return this.objectValue.processValue(excelFile, cell, value);
	}
}
