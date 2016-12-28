package com.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.camel.impl.DefaultExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyConsumer extends DefaultConsumer {

	private final static Logger log = LoggerFactory.getLogger(MyConsumer.class);
	private ServerSocket serverSocket;

	public MyConsumer(Endpoint endpoint, Processor processor) throws Exception {
		super(endpoint, processor);
		serverSocket = new ServerSocket(5555);
		log.debug("Creating My Consumer ...");
	}

	@Override
	protected void doStart() throws Exception {
		log.debug("Starting My Consumer ...");
		new Thread(new AcceptThread()).start();
		super.doStart();
	}

	@Override
	protected void doStop() throws Exception {
		super.doStop();
		log.debug("Stopping My Consumer ...");
		if (serverSocket != null) {
			serverSocket.close();
		}

	}

	class AcceptThread implements Runnable {
		public void run() {
			while (true) {
				Exchange exchange = new DefaultExchange(getEndpoint(), ExchangePattern.InOut);
				Socket clientSocket = null;
				try {
					clientSocket = serverSocket.accept();
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					String inputLine = in.readLine();
					if (inputLine != null) {
						log.debug("Get input line: {}", inputLine);
						exchange.getIn().setBody(inputLine, String.class);
						getProcessor().process(exchange);
						String response = exchange.getOut().getBody(String.class);
						if (response == null) {
							response = exchange.getIn().getBody(String.class);
						}
						if (response != null) {
							out.println(response);
						}
					}
				} catch (Exception e) {
					exchange.setException(e);
				} finally {
					if (clientSocket != null) {
						try {
							clientSocket.close();
						} catch (Exception ignored) {
						}
					}
				}
			}
		}
	}
}
