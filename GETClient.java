/*******************************************************************************
 * Copyright (c) 2015 Institute for Pervasive Computing, ETH Zurich and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 * 
 * Contributors:
 *    Matthias Kovatsch - creator and main architect
 *    Achim Kraus (Bosch Software Innovations GmbH) - add saving payload
 ******************************************************************************/
package org.eclipse.californium.examples;

import java.io.BufferedReader;
import java.util.Scanner;
import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.network.config.NetworkConfigDefaultHandler;
import org.eclipse.californium.core.network.config.NetworkConfig.Keys;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;

public class GETClient {

	private static final File CONFIG_FILE = new File("Californium.properties");
	private static final String CONFIG_HEADER = "Californium CoAP Properties file for Fileclient";
	private static final int DEFAULT_MAX_RESOURCE_SIZE = 2 * 1024 * 1024; // 2 MB
	private static final int DEFAULT_BLOCK_SIZE = 512;

	private static NetworkConfigDefaultHandler DEFAULTS = new NetworkConfigDefaultHandler() {

		@Override
		public void applyDefaults(NetworkConfig config) {
			config.setInt(Keys.MAX_RESOURCE_BODY_SIZE, DEFAULT_MAX_RESOURCE_SIZE);
			config.setInt(Keys.MAX_MESSAGE_SIZE, DEFAULT_BLOCK_SIZE);
			config.setInt(Keys.PREFERRED_BLOCK_SIZE, DEFAULT_BLOCK_SIZE);
		}
	};
	
	public static void Observer() {
		CoapClient client = new CoapClient("coap://[aaaa::200:0:0:2]:5683/test/hello");
		
		// wait for user
				System.out.println("Aperte Enter para iniciar: ");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				try { br.readLine(); } catch (IOException e) { }
				
				// observe

				System.out.println("OBSERVE (press enter to exit)");
		
		CoapObserveRelation relation = client.observe(new CoapHandler() {
			@Override
		public void onLoad(CoapResponse response) {
		System.out.println(response.getResponseText());
		}
		@Override
		public void onError() {
		System.err.println("Failed");
		}
		});
		
		// wait for user
				try { br.readLine(); } catch (IOException e) { }
				
				System.out.println("CANCELLATION");
				
		relation.proactiveCancel();
	};
	
	public static void Get(String[] args) {
		NetworkConfig config = NetworkConfig.createWithFile(CONFIG_FILE, CONFIG_HEADER, DEFAULTS);
		NetworkConfig.setStandard(config);
		 
		URI uri = null; // URI parameter of the request
		
		if (args.length >=0) {
			
			// input URI from command line arguments
			try {
				//uri = new URI(args[0]);
				uri = new URI("coap://californium.eclipse.org:5683");

			} catch (URISyntaxException e) {
				System.err.println("Invalid URI: " + e.getMessage());
				System.exit(-1);
			}
			
			CoapClient client = new CoapClient(uri);

			CoapResponse response = null;
			try {
				response = client.get();
				
			} catch (ConnectorException | IOException e) {
				System.err.println("Got an error: " + e);
			}

			if (response!=null) {
				
				System.out.println(response.getCode());
				System.out.println(response.getOptions());
				if (args.length > 1) {
					try (FileOutputStream out = new FileOutputStream(args[1])) {
						out.write(response.getPayload());
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println(response.getResponseText());
					
					System.out.println(System.lineSeparator() + "ADVANCED" + System.lineSeparator());
					// access advanced API with access to more details through
					// .advanced()
					System.out.println(Utils.prettyPrint(response));
				}
			} else {
				System.out.println("No response received.");
			}
			
			client.shutdown();
		} else {
			// display help
			System.out.println("Californium (Cf) GET Client");
			System.out.println("(c) 2014, Institute for Pervasive Computing, ETH Zurich");
			System.out.println();
			System.out.println("Usage : " + GETClient.class.getSimpleName() + " URI [file]");
			System.out.println("  URI : The CoAP URI of the remote resource to GET");
			System.out.println("  file: optional filename to save the received payload");
<<<<<<< HEAD:GETClient.java
		}*/
		
//*****************************************************************************		

		CoapClient client = new CoapClient("coap://"
				+ "californium.eclipse.org:5683/obs");
		
		// wait for user
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				try { br.readLine(); } catch (IOException e) { }
				
				// observe
=======
		}
	};
>>>>>>> a26cc1f1d8853b78acbfdd437d22cc977dcc1ad6:GETClient2.java

	public static void main(String args[]) {
		Scanner keyboard;
		keyboard = new Scanner ( System.in );
		int opcao;
		System.out.println("Observer ---------- [1]");
		System.out.println("Get --------------- [2]");
		System.out.print("Informe um valor: "); 
		opcao = keyboard.nextInt();
		System.out.println( "Opção : " + opcao );
		switch (opcao) {
		case 1: 
			Observer();
			break;
		case 2:
			Get(args);
			break;
		default:
			System.out.println("Número inválido");
			break;
			/*if (opcao == 1){
				Observer();
			}else if(opcao == 2){
				Get(args);
			}else{
				System.exit(0);
			}*/
		}
		}

}
