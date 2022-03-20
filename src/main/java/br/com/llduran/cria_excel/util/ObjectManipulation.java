package br.com.llduran.cria_excel.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class ObjectManipulation
{
	@Autowired
	private ModelMapper modelMapper;

	public Object desserializa(String json, String packageName, String className)
			throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException,
			IllegalAccessException, NoSuchFieldException, JsonProcessingException
	{
		Class MinhaClasse = Class.forName(packageName + "." + className);
		Object meuobjeto = MinhaClasse.getConstructor().newInstance();

		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
		Object objeto = (Object) mapper.readValue(json, meuobjeto.getClass());

		return objeto;
	}

	public Object toDto(Object obj, String packageName, String className)
			throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException,
			IllegalAccessException
	{
		Class MinhaClasse = Class.forName(packageName + "." + className);
		Object meuObjeto = MinhaClasse.getConstructor().newInstance();

		return modelMapper.map(obj, meuObjeto.getClass());
	}
}
