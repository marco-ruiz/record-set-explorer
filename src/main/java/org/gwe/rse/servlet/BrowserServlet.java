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
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.gwe.rse.AbstractRecordProvider;
import org.gwe.rse.provider.RecordSetProviderRepo;
import org.gwe.rse.render.BrowserTemplate;


/**
 * @author Marco Ruiz
 * @since Dec 13, 2008
 */
public abstract class BrowserServlet extends BaseRSEServlet {

    private static final String ATTR_BROWSER_TEMPLATE = "RSE_BROWSER_TEMPLATE";
    protected static final String SOURCE_NUMBER = "sourceNumber";

	public void init() throws ServletException {
	    super.init();
	    
	    ServletContext ctx = getServletContext();
		synchronized (ctx) {
			if (getVelocityTemplate() == null)
	            try {
	                ctx.setAttribute(ATTR_BROWSER_TEMPLATE, new BrowserTemplate());
                } catch (IOException e) {
                	throw new ServletException(e);
                }
        }
    }

	protected String buildOutput(Class providerClass, Map<String, String> params, RecordSetProviderRepo repo) throws ServletException {
	    repo.add(createProvider(providerClass, params));
		return buildOutput(repo.size() - 1, repo);
    }
	
	protected String buildOutput(int sourceNumber, RecordSetProviderRepo repo) throws ServletException {
        AbstractRecordProvider provider = (sourceNumber < 0 || sourceNumber > repo.size()) ? 
        		AbstractRecordProvider.NULL_RECORD_PROVIDER : repo.get(sourceNumber);
	    try {
            return getVelocityTemplate().evaluate(sourceNumber, repo,
            		provider.getMetadata(), 
            		provider.getStartUpFields(), 
            		JSONRecordServlet.URI);
        } catch (Exception e) {
        	throw new ServletException("Problems parsing velocity template", e);
        }
    }
	
	public BrowserTemplate getVelocityTemplate() {
		return getCtxAttribute(ATTR_BROWSER_TEMPLATE);
    }
}

