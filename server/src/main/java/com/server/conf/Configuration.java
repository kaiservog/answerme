package com.server.conf;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.inject.Singleton;

@Singleton
public class Configuration {
	private static Properties props;

	private Configuration() {
	}

	public Properties load() throws IOException {
		props = new Properties();
		FileInputStream file = new FileInputStream("/apl/conf.properties");
		props.load(file);
		return props;
	}

	public boolean isSecureEnabled() {
		return props.get("secure").equals("true");
	}

	public String getCacheAddress() {
		return (String) props.get("cache.address");
	}

	public Integer getCachePort() {
		return Integer.valueOf((String) props.get("cache.port"));
	}

	public String getKeyStorePath() {
		return (String) props.get("keystore.dir");
	}

	public String getKeyStorePass() {
		// TODO adicionar criptografia
		return (String) props.get("keystore.pass");
	}

	public String getStoredConfPath() {
		return (String) props.get("store.conf.dir");
	}

}
