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

import java.io.File;
import java.io.PrintStream;

import org.gwe.rse.servlet.DefineRecordSetServlet;
import org.gwe.rse.servlet.ExploreRecordSetServlet;
import org.gwe.rse.servlet.ImageServlet;
import org.gwe.rse.servlet.JSONRecordServlet;
import org.gwe.rse.servlet.SaveRecordSetServlet;
import org.mortbay.jetty.HandlerContainer;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.DefaultServlet;
import org.mortbay.jetty.servlet.HashSessionManager;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.servlet.SessionHandler;

/**
 * @author Marco Ruiz
 * @since Dec 14, 2008
 */
public class RSEServletContext extends Context {

	public RSEServletContext(String installRoot, HandlerContainer contexts) throws Exception {
		super(contexts, "/"); 
		if (!installRoot.endsWith("/")) installRoot += "/";

		System.setErr(new PrintStream(new File((installRoot + ".jetty.log"))));
		setResourceBase((installRoot + "web"));
		this.setSessionHandler(new SessionHandler(new HashSessionManager()));
//		setWelcomeFiles(new String[] {"index.html"});
		
		// Register servlets
		addServlet(new ServletHolder(new DefaultServlet()),          "/");
		addServlet(new ServletHolder(new DefineRecordSetServlet()),  DefineRecordSetServlet.URI);
		addServlet(new ServletHolder(new ExploreRecordSetServlet()), ExploreRecordSetServlet.URI);
		addServlet(new ServletHolder(new SaveRecordSetServlet()),    SaveRecordSetServlet.URI);
		addServlet(new ServletHolder(new ImageServlet("")),          ImageServlet.URI);
		addServlet(new ServletHolder(new JSONRecordServlet()),       JSONRecordServlet.URI);
	}
}

