package com.server.http;

import javax.inject.Inject;

import org.jboss.weld.context.RequestContext;
import org.jboss.weld.context.unbound.UnboundLiteral;
import org.jboss.weld.environment.se.WeldContainer;

public class Resource {
	@Inject
	private WeldContainer container;
	
	public void startResquestScope() {
		container.instance().select(RequestContext.class, UnboundLiteral.INSTANCE)
		.get().activate();
	}

}
