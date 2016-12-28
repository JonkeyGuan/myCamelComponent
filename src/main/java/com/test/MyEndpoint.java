package com.test;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

public class MyEndpoint extends DefaultEndpoint {

	public MyEndpoint(String uri, Component component) {
		super(uri, component);
	}

	public Consumer createConsumer(Processor processor) throws Exception {
		return new MyConsumer(this, processor);
	}

	public Producer createProducer() throws Exception {
		return new MyProducer(this);
	}

	public boolean isSingleton() {
		return false;
	}

}
