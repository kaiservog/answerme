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
		return getProps().get("secure").equals("true");
	}

	public String getCacheAddress() {
		return (String) getProps().get("cache.address");
	}

	public Integer getCachePort() {
		return Integer.valueOf((String) getProps().get("cache.port"));
	}

	public String getKeyStorePath() {
		return (String) getProps().get("keystore.dir");
	}

	public String getKeyStorePass() {
		// TODO adicionar criptografia
		return (String) getProps().get("keystore.pass");
	}

	public String getStoredConfPath() {
		return (String) getProps().get("store.conf.dir");
	}

	private Properties getProps() {
		try {
			if(props == null) load();
		}catch(IOException e) {
			
		}
		return props;
	}

}
