package br.com.llduran.cria_excel.util;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 10 Ways to Create a Stream in Java
 * https://www.geeksforgeeks.org/10-ways-to-create-a-stream-in-java/
 * Create stream from a Pattern using Predicate
 **/
public class RegexUtils
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

	public static String upperCaseFirst(String val)
	{
		char[] arr = val.toCharArray();
		arr[0] = Character.toUpperCase(arr[0]);
		return new String(arr);
	}
}
