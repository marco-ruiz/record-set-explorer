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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gwe.rse.FieldData;
import org.gwe.rse.FieldMeta;
import org.gwe.rse.json.JSONFieldDescriptorList;
import org.gwe.rse.json.JSONFieldMetadataList;
import org.gwe.rse.provider.RecordSetProviderRepo;

/**
 * @author Marco Ruiz
 * @since Dec 12, 2009
 */
public class BrowserTemplate extends RenderTemplate {

	private static final String RSE_BROWSER_TABLE_ID = "RSEBrowserTable";

	public BrowserTemplate() throws IOException {
		appendContent("browser.vm", true);
		appendContent("define.vm", true);
		appendContent("skeleton.vm", true);
    }
	
	public String evaluate(int sourceNumber, RecordSetProviderRepo repo, List<? extends FieldMeta> metadata, Set<FieldData> record, String jsonURI) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();

		// RSE Specific Parameters
		model.put("selectedSourceNumber", sourceNumber);
		model.put("metadata", new JSONFieldMetadataList(metadata));
		model.put("firstRec", new JSONFieldDescriptorList(record));
		model.put("tableId" , RSE_BROWSER_TABLE_ID);
		model.put("jsonURI" , jsonURI);
		return evaluate(sourceNumber, repo, model);
	}
}

