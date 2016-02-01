package com.server.db;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TransactionRequired
@Interceptor
public class TransactionIntercept {
	private static final Logger logger = LoggerFactory.getLogger(TransactionIntercept.class);
	@Inject
	private EntityManager manager;

	@AroundInvoke
	public Object transactional(InvocationContext ctx) {
		Object r = null;
		
		if (!manager.getTransaction().isActive()) {
			logger.info("Starting transaction");
			manager.getTransaction().begin();
			
			try {
				r = ctx.proceed();
			} catch (Exception e) {
				manager.getTransaction().rollback();
			}
			
			logger.info("Committing transaction");
			manager.getTransaction().commit();
		}

		return r;
	}
}
