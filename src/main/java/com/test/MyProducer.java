package com.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyProducer extends DefaultProducer {

	private final static Logger log = LoggerFactory.getLogger(MyProducer.class);

	public MyProducer(Endpoint endpoint) {
		super(endpoint);
		log.debug("Creating My Producer ...");
	}

	public void process(Exchange exchange) throws Exception {
		log.debug("Processing exchange");

		String input = exchange.getIn().getBody(String.class);
		log.debug("Get input: {}", input);

		log.debug("Connecting to socket on localhost:5555");
		Socket socket = new Socket("localhost", 5555);

		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out.println(input);
		String fromServer = in.readLine();
		socket.close();
		log.debug("Get reply from server: {}", fromServer);

		log.debug("Populating the exchange");
		exchange.getIn().setBody(fromServer, String.class);

	}

}
