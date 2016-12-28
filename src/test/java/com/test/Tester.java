package com.test;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.language.SimpleExpression;

public class Tester {

	public static void main(String[] args) {
		CamelContext context = new DefaultCamelContext();
		try {
			context.addRoutes(new RouteBuilder() {
				public void configure() {
					from("my:server").to("log:server").setBody(body().prepend("Echo "));
				}
			});

			context.addRoutes(new RouteBuilder() {
				public void configure() {
					from("timer:fire?period=5000").setBody(new SimpleExpression("hello~")).to("my:client")
							.to("log:client");
				}
			});

			context.start();
			// Thread.sleep(10000);
			// context.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
