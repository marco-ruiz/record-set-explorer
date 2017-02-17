/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gwe.rse.servlet.jetty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.servlet.Context;

/**
 * @author Marco Ruiz
 * @since Jan 25, 2008
 */
public class WebServerApp {

	private static Log log = LogFactory.getLog(WebServerApp.class);

	public static void main(String[] args) throws Exception {
		List<String> argsList = getAsList(args);
		String confArg = argsList.remove(0);
		int port = (argsList.size() >= 1) ? Integer.parseInt(argsList.get(0)) : 8088;
//		String confArg = (argsList.size() >= 2) ? argsList.get(1) : "";
		WebServerApp app = new WebServerApp(port);
		app.startWebApp(confArg);
    }

	private static List<String> getAsList(String[] args) {
	    return new ArrayList<String>(Arrays.asList(args));
    }
	
    private int port;
	private Server server;
	private Context context;

    public WebServerApp(int port) {
    	this.port = port;
    }

	private void startWebApp(String confArg) throws Exception {
		header();

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		context = new RSEServletContext(confArg, contexts);
		
		System.out.println("Launching...");
	    server = new Server(port);
		server.addHandler(contexts);
		server.addHandler(new DefaultHandler());
		
		server.setHandler(context);
		server.start();

		footer();
//		openURL(getURL());
		server.join();
	}

	private void header() {
	    System.out.println("\n==============================================================\n");
		System.out.println("\tWelcome to the RSE Web Application");
		System.out.println("\tURL: " + getURL() + " \n");
		System.out.println("\n==============================================================\n");
    }
	
	private void footer() {
	    System.out.println("RSE Application Launched!");
		System.out.println("Point your browser to " + getURL() + " to access it...");
    }

	private String getURL() {
	    return "'http://localhost:" + port + "'";
    }
}

