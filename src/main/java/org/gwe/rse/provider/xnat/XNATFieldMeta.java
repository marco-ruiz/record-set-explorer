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

package org.gwe.rse.provider.xnat;

import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.gwe.rse.FieldData;
import org.gwe.rse.FieldDataMap;
import org.gwe.rse.FieldMeta;
import org.gwe.rse.render.RenderTemplate;
import org.gwe.rse.utils.XPathValueList;

/**
 * @author Marco Ruiz
 * @since Dec 19, 2009
 */
public class XNATFieldMeta extends FieldMeta {
	
	private RenderTemplate urlTemplate;
	private String xpathExpr;

	public XNATFieldMeta(String id, String caption, String renderingFunctionName, String urlTemplate, String xpathExpr) {
		super(id, caption, true, renderingFunctionName);
		this.urlTemplate = new RenderTemplate(urlTemplate);
		this.xpathExpr = xpathExpr;
	}
	
	public FieldData getData(String server, FieldDataMap filter, String currValue) throws Exception {
		return getData(server, filter, currValue, null);
	}
	
	public FieldData getData(String server, FieldDataMap filter, String currValue, String urlAuth) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("server", server);
		for (Entry<String, FieldData> entry : filter.entrySet()) 
			model.put(entry.getKey(), entry.getValue().getValue());
		
		URL url = new URL(urlTemplate.evaluate(model));
		URLConnection conn = url.openConnection();
        if (urlAuth != null && !urlAuth.equals(""))
        	conn.setRequestProperty("Authorization", "Basic " + urlAuth);
        
		XPathValueList options = new XPathValueList(conn.getInputStream(), xpathExpr);
		String value = options.contains(currValue) ? currValue : options.get(0);
		return new FieldData(getId(), value, options);
	}
}

