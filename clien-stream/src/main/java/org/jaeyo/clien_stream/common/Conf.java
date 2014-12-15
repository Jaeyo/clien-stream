package org.jaeyo.clien_stream.common;

import java.io.File;
import java.util.Properties;

public class Conf{
	private static Properties props = new Properties();

	public static String get(ConfKey key) {
		return props.getProperty(key.toString());
	} // get
	
	public static String get(ConfKey key, String defaultValue){
		return props.getProperty(key.toString(), defaultValue);
	} //get
	
	public static void set(ConfKey key, String value){
		props.setProperty(key.toString(), value);
	} //set
	
	public static File getPackagePath() {
		String jarPath = Conf.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		File jarFile = new File(jarPath);
		File packagePath = jarFile.getParentFile();
		return packagePath;
	} // getPackagePath
	
	public static File getWebInfPath(){
		return new File(getPackagePath(), "../WEB-INF");
	} //getWebInfPath
} // class