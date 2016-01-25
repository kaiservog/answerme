package com.server.db.manage;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBTest {
	private static final Logger logger = LoggerFactory.getLogger(DBTest.class);

	@Inject private EntityManager manager;
	
	public void test() {
		logger.info("teste db " + manager);
	}
}
