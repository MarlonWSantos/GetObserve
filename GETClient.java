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
	
	public static void Observer(String args) {
		
	
		String url[]= {"coap://[aaaa::200:0:0:2]:5683/test/hello",
						"coap://[aaaa::200:0:0:3]:5683/test/hello",
						"coap://[aaaa::200:0:0:4]:5683/test/hello"};
		int i=0;
		System.out.println("Url:"+url[i]);
		CoapClient client = new CoapClient(url[i]);
		i++;
		System.out.println("Url:"+url[i]);
		CoapClient client2 = new CoapClient(url[i]);
		i++;
		System.out.println("Url:"+url[i]);
		CoapClient client3 = new CoapClient(url[i]);
		
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
		
		CoapObserveRelation relation2 = client2.observe(new CoapHandler() {
			@Override
			public void onLoad(CoapResponse response) {
				System.out.println(response.getResponseText());
			}
			@Override
			public void onError() {
				System.err.println("Failed");
			}
		});
		
		CoapObserveRelation relation3 = client3.observe(new CoapHandler() {
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
	
	
	public static void Get(String args) {
		NetworkConfig config = NetworkConfig.createWithFile(CONFIG_FILE, CONFIG_HEADER, DEFAULTS);
		NetworkConfig.setStandard(config);
		 
		CoapClient client = new CoapClient(args);

			CoapResponse response = null;
				
				try {
					response = client.get();
				} catch (ConnectorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				if (response!=null) {

				
				System.out.println(response.getCode());
				System.out.println(response.getOptions());
					System.out.println(response.getResponseText());
					
					System.out.println(System.lineSeparator() + "ADVANCED" + System.lineSeparator());
					// access advanced API with access to more details through
					// .advanced()
					System.out.println(Utils.prettyPrint(response));
				
			}else { }			
			client.shutdown();
			}
		
	
	
	public static void main(String[] args) {
		Scanner keyboard;
		keyboard = new Scanner ( System.in );
		int opcao;
		//String url = "coap://[aaaa::200:0:0:7]:5683/test/hello";
		//String url = "coap://coap.me/hello";
		if (args.length==0) {
		//String url = args[0];
		//String[] url = new String[2]
		String url = null;
		//String url = "coap://[aaaa::200:0:0:2]:5683/test/hello";
		//String url = "coap://[aaaa::200:0:0:3]:5683/test/hello";

		System.out.println("Observer ---------- [1]");
		System.out.println("Get --------------- [2]");
		System.out.print("Informe um valor: "); 
		opcao = keyboard.nextInt();
		System.out.println( "Opção : " + opcao );
		switch (opcao) {
		case 1: 
			Observer(url);

			break;
		case 2:
			Get(url);

			break;
		default:
			System.out.println("Número inválido");
			break;			
		}
		}else {System.out.println("Faltando a url");}
	}
}
