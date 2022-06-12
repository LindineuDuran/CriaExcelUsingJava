package br.com.llduran.cria_excel.util.excel;

import br.com.llduran.cria_excel.model.ServiceEnum;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum DataTypeEnum
{
	HeaderValue("Header"),
	BigDecimalValue("java.math.BigDecimal"),
	DoubleValue("java.lang.Double"),
	LocalDateValue("java.time.LocalDate"),
	LocalDateTimeValue("java.time.LocalDateTime"),
	StringValue("java.lang.String");

	String className;

	private DataTypeEnum(String className)
	{
		this.className = className;
	}

	public String getClassName()
	{
		return className;
	}
	public static String getDataTypeValueByClassName(String className)
	{
		return Stream.of (DataTypeEnum.values())
				.filter(t -> t.getClassName().equals(className))
				.map(t -> t.name()).collect (Collectors.joining ("\r\n"));
	}
}
