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
 *    Marlon W. Santos (Federal University of Pará) - add Observer
 *                                                  - add Menu
 *                                                  - add Functions
 *                                                  - add ErrorMsg
 *                                                  - add UrlMotes
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
				
				}else {
				System.out.println("No response received.");
				}			
			client.shutdown();
			}
		
	public static int Menu() {
		
		Scanner keyboard;
		keyboard = new Scanner ( System.in );
		
		System.out.println("Observer ---------- [1]");
		System.out.println("Get --------------- [2]");
		System.out.print("Informe um valor: ");
		
		return keyboard.nextInt();
	}
	
	public static void ErrorMsg() {
		System.out.println("Faltando uma URL");
		System.out.println("Modo de usar:");
		System.out.println("CoapObserver.jar + coap://[URL]:5683");
		System.out.println("Mais de uma URL:");
		System.out.println("CoapObserver.jar + n +coap://[URL]:5683");
		System.out.println("URL: Endereco remoto do mote");
		System.out.println("n: Número de motes da rede");
	}
	
	public static void Functions(int opcao,String[] url){
		
		switch (opcao) {
		case 1: 
			Observer(url);
			break;
		case 2:
			Get(url[0]);
			break;
		default:
			System.out.println("Número inválido");
			break;			
		}
		
	}

	public static void Observer(String[] url) {
		
		int i=0;
			
		CoapClient[] client = new CoapClient[url.length];
		
		for (i=0;i<url.length;i++) {
			client[i] = new CoapClient(url[i]);
		}
		
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

				System.out.println("OBSERVE (press enter to exit)");
				
				CoapObserveRelation[] relation = new CoapObserveRelation[url.length];
				
				for(i=0;i<url.length;i++) {
				
					relation[i] = client[i].observe(new CoapHandler() {
						@Override
						public void onLoad(CoapResponse response) {
							System.out.println(response.getResponseText());
						}
						@Override
						public void onError() {
							System.err.println("Failed");
						}
					});
				}
							
		// wait for user
				try { br.readLine(); } catch (IOException e) { }
				
				System.out.println("CANCELLATION");
				  
	};

	public static String[] UrlMotes(String motes,String url) {
		
		int NumMotes = Integer.parseInt(motes);
		String[] ListUrl = new String[NumMotes];
		String index;
		
		for(int i=0;i<NumMotes;i++) {
			index = String.valueOf(i+2);
			ListUrl[i] = url.replace(":2]",":"+index+"]");
		}
		return ListUrl;
	}
	
	
	public static void main(String[] args) {
		
		int opcao;
		String[] url = new String[args.length];
		
		if (args.length==1) {
			
				
			url[0] = args[0];
	
			opcao=Menu();

			Functions(opcao,url);
	
		}else if(args.length>1){
			
			url = UrlMotes(args[0],args[1]);
			
			opcao=Menu();

			Functions(opcao,url);
						
		}
		else{ErrorMsg();}
	}
}
