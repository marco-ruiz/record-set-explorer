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

package org.gwe.rse.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.gwe.rse.AbstractRecordProvider;
import org.gwe.rse.provider.RecordProviderFactory;
import org.gwe.rse.provider.RecordSetProviderRepo;


/**
 * @author Marco Ruiz
 * @since Dec 13, 2008
 */
public abstract class BaseRSEServlet extends HttpServlet {

	protected static final String ATTR_SESSIONS_FACTORY = "ATTR_SESSIONS_FACTORY";
	protected static final String ATTR_PROVIDER_REPO    = "ATTR_PROVIDER_REPO";

	private static Map<String, String> getParams(HttpServletRequest request) throws Exception {
	    Map<String, String> result = new HashMap<String, String>();
	    
    	if (request.getMethod().equals("GET")) {
    	    Map<String, String[]> params = new HashMap<String, String[]>(request.getParameterMap());
    	    for (Entry<String, String[]> entry : params.entrySet()) 
    	    	result.put(entry.getKey(), entry.getValue()[0]);
    	} else {
        	ServletFileUpload upload = new ServletFileUpload();
        	FileItemIterator iter = upload.getItemIterator(request);
        	while (iter.hasNext()) {
        	    FileItemStream item = iter.next();
        	    result.put(item.getFieldName(), Streams.asString(item.openStream()));
        	}
    	}
    	
	    return result;
    }

	//=======
	// CLASS
	//=======
	public void init() throws ServletException {
	    super.init();
	    
	    ServletContext ctx = getServletContext();
		synchronized (ctx) {
		    if (getProviderFactory() == null) 
				ctx.setAttribute(ATTR_SESSIONS_FACTORY, new RecordProviderFactory());
        }
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doQuery(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    doQuery(request, response);
    }

	private void doQuery(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	    response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        String output;
        try {
			output = getOutput(getParams(request), getSessionRecordSetRepo(request));
        } catch (Exception e) {
        	throw new ServletException("Problems parsing parameters.", e);
        }
		response.getWriter().println(output);
    }

	private RecordSetProviderRepo getSessionRecordSetRepo(HttpServletRequest request) {
	    RecordSetProviderRepo repo = (RecordSetProviderRepo) getSessionAttribute(request, ATTR_PROVIDER_REPO);
        if (repo == null) {
        	repo = new RecordSetProviderRepo();
    	    request.getSession().setAttribute(ATTR_PROVIDER_REPO, repo);
        }
	    return repo;
    }
	
	protected abstract String getOutput(Map<String, String> params, RecordSetProviderRepo repo) throws ServletException;
	
	protected <PROV_T extends AbstractRecordProvider> PROV_T createProvider(Class<PROV_T> provClass, Map<String, String> params) {
	    return getProviderFactory().create(provClass, params);
    }

	private RecordProviderFactory getProviderFactory() {
		return getCtxAttribute(ATTR_SESSIONS_FACTORY);
    }

	protected final <RET_T> RET_T getCtxAttribute(String attrKey) {
		return (RET_T) getServletContext().getAttribute(attrKey);
    }

	protected final <RET_T> RET_T getSessionAttribute(HttpServletRequest request, String attrKey) {
		return (RET_T) request.getSession().getAttribute(attrKey);
    }
}

