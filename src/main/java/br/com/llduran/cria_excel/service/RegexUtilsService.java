package br.com.llduran.cria_excel.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegexUtilsService
{
	// Function convert a List into Stream
	public static List<String> getStream(List<String> list)
	{
		List<String> tipos = list
				.stream()
				.map(f -> f.replace(".json",""))
				.map(f -> f.replaceAll("[0-9]+", ""))
				.distinct()
				.collect(Collectors.toList());

		return tipos;
	}
}
