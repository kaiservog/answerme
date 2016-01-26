package com.server.conf;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
	private static Properties props;

	private Configuration() {

	}

	public static Properties load() throws IOException {
		props = new Properties();
		FileInputStream file = new FileInputStream("/apl/conf.properties");
		props.load(file);
		return props;
	}

	public static boolean isSecureEnabled() {
		return props.get("secure").equals("true");
	}
	
	public static String getCacheAddress() {
		return (String) props.get("cache.address");
	}
	
	public static Integer getCachePort() {
		return Integer.valueOf((String) props.get("cache.port"));
	}
	
	public static String getKeyStorePath() {
		return (String) props.get("keystore.dir");
	}
	
	public static String getKeyStorePass() {
		//TODO adicionar criptografia
		return (String) props.get("keystore.pass");
	}
	
	public static String getStoredConfPath() {
		return (String) props.get("store.conf.dir");
	}
	
}
