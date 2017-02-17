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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URLConnection;

import sun.misc.BASE64Encoder;

/**
 * @author Marco Ruiz
 * @since Dec 19, 2009
 */
public class IOStaticUtils {

	public static String readContent(String source, Class clazz) throws IOException {
		String templateName = clazz.getPackage().getName().replace('.','/') + "/" + source;
	    InputStream is = clazz.getClassLoader().getResourceAsStream(templateName);
	    return convertStreamToString(is);
    }
    
	public static String convertStreamToString(InputStream is) throws IOException {
        if (is == null) return "";
        StringBuilder sb = new StringBuilder();
        String line;
        
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            while ((line = reader.readLine()) != null) 
                sb.append(line).append("\n");
        } finally {
            is.close();
        }
        return sb.toString();
    }
	
	public static void writeToConnection(URLConnection urlc, Reader input) throws IOException, Exception {
	    OutputStream out = urlc.getOutputStream();
	    try {
	    	Writer writer = new OutputStreamWriter(out, "UTF-8");
	    	pipe(input, writer);
	    	writer.close();
	    } catch (IOException e) {
	    	throw new Exception("Problems while posting data", e);
	    } finally {
	    	if (out != null) out.close();
	    }
    }

	public static void readFromConnection(URLConnection urlc, Writer output) throws IOException, Exception {
	    InputStream in = urlc.getInputStream();
	    try {
	    	Reader reader = new InputStreamReader(in);
	    	pipe(reader, output);
	    	reader.close();
	    } catch (IOException e) {
	    	throw new Exception("Problems while reading response", e);
	    } finally {
	    	if (in != null) in.close();
	    }
    }
	
	public static void pipe(Reader reader, Writer writer) throws IOException {
		char[] buf = new char[1024];
		int read = 0;
		while ((read = reader.read(buf)) >= 0) {
			writer.write(buf, 0, read);
		}
		writer.flush();
	}
	
	public static String buildURLAuth(String user, String password) {
	    String result = null;
		if (user != null && password != null) {
		    result = user + ":" + password;
		    result = new BASE64Encoder().encode(result.getBytes());
	    }
	    return result;
    }
}
