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

package org.gwe.rse.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @author Marco Ruiz
 * @since Dec 20, 2009
 */
public class HTTPRequester {
	
	private URL targetURL;
	private String auth = null;
	private String requestMethod;
	
	public HTTPRequester(String targetURL) throws MalformedURLException {
		this(targetURL, true);
    }

	public HTTPRequester(String targetURL, boolean postMethod) throws MalformedURLException {
		this.targetURL = new URL(targetURL);
    	this.requestMethod = postMethod ? "POST" : "GET";
    }

	public void setAuth(String user, String password) throws Exception {
	    this.auth = IOStaticUtils.buildURLAuth(user, password);
	}

	public Writer connect() throws Exception {
		return connect(new StringWriter(), null);
	}

	public Writer connect(Writer output) throws Exception {
		return connect(output, null);
	}
	
	public Writer connect(Writer output, Reader input) throws Exception {
		HttpURLConnection urlc = null;
		try {
			urlc = createPostConnection();
            if (input  != null) IOStaticUtils.writeToConnection(urlc, input);
            if (output != null) IOStaticUtils.readFromConnection(urlc, output);
		} catch (IOException e) {
			throw new Exception("Cannot connect to " + targetURL, e);
		} finally {
			if (urlc != null) urlc.disconnect();
		}
		return output;
	}

	private HttpURLConnection createPostConnection() throws IOException, Exception {
	    HttpURLConnection conn = (HttpURLConnection) targetURL.openConnection();
	    if (auth != null && !auth.equals(""))
	    	conn.setRequestProperty("Authorization", "Basic " + auth);

	    try {
	    	conn.setRequestMethod(requestMethod);
	    } catch (ProtocolException e) {
	    	throw new Exception(requestMethod + " not supported", e);
	    }
	    conn.setDoOutput(true);
	    conn.setDoInput(true);
	    conn.setUseCaches(false);
	    conn.setAllowUserInteraction(false);
	    conn.setRequestProperty("Content-type", "text/xml; charset=" + "UTF-8");
	    return conn;
    }
}


