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

package org.gwe.rse.render;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.gwe.rse.provider.RecordSetProviderRepo;
import org.gwe.rse.servlet.DefineRecordSetServlet;
import org.gwe.rse.servlet.ExploreRecordSetServlet;
import org.gwe.rse.servlet.SaveRecordSetServlet;
import org.gwe.rse.utils.IOStaticUtils;

/**
 * @author Marco Ruiz
 * @since Dec 12, 2009
 */
public class RenderTemplate {
	
	private String content = "";
	
	public RenderTemplate() {}

	public RenderTemplate(String source) {
		appendContent(source);
    }

	public void appendContent(String source, boolean sourceIsFileName) throws IOException {
		appendContent(sourceIsFileName ? IOStaticUtils.readContent(source, RenderTemplate.class) : source);
    }

	public void appendContent(String content) {
    	this.content += content;
    }

	public String evaluate(Integer sourceNumber, RecordSetProviderRepo repo, Map<String, Object> model) throws Exception {
		// FIXME: Move variable 'version' to a more generalized resolution scheme
		model.put("version", "0.6.1.alpha");
		model.put("repo", repo);
		model.put("selectedSourceNumber", sourceNumber);

		// Forms
		model.put("rseGet"   , ExploreRecordSetServlet.URI);
		model.put("rsePost"  , SaveRecordSetServlet.URI);
		model.put("rseDefine", DefineRecordSetServlet.URI);
		return evaluate(model);
    }
	
	public String evaluate(Map<String, Object> model) throws Exception {
	    VelocityContext context = new VelocityContext(model);
        StringWriter writer = new StringWriter();
		Velocity.evaluate(context, writer, "", content);
		return writer.getBuffer().toString();
    }
}

