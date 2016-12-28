package com.test;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyComponent extends DefaultComponent {

	private final static Logger log = LoggerFactory.getLogger(MyComponent.class);

	public MyComponent() {
		log.debug("Creating My Component");
	}

	public MyComponent(CamelContext camelContext) {
		super(camelContext);
		log.debug("Creating My Component with CamelContext");
	}

	@Override
	protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
		log.debug("Creating My Camel Endpoint");
		MyEndpoint myEndpoint = new MyEndpoint(uri, this);
		setProperties(myEndpoint, parameters);
		return myEndpoint;
	}

}
