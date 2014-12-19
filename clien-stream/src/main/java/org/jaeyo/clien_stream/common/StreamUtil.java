package org.jaeyo.clien_stream.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamUtil{
	public static String readAllLine(InputStream inputStream) throws IOException{
		StringBuilder sb=new StringBuilder();
		BufferedReader input=new BufferedReader(new InputStreamReader(inputStream));
		String line=null;
		
		while((line=input.readLine())!=null)
			sb.append(line);
		
		return sb.toString();
	} //readAllLine
} //class