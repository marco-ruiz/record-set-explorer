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

import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;

import org.gwe.rse.AbstractRecordProvider;
import org.gwe.rse.FieldData;
import org.gwe.rse.json.JSONFieldDescriptorList;
import org.gwe.rse.provider.RecordSetProviderRepo;


/**
 * @author Marco Ruiz
 * @since Dec 13, 2008
 */
public class JSONRecordServlet extends BaseRSEServlet {
	
	public static final String URI = "/jsonRecord"; 

    protected String getOutput(Map<String, String> params, RecordSetProviderRepo repo) throws ServletException {
    	int sourceNumber = Integer.parseInt(params.get("sourceNumber"));
		Set<FieldData> rec = queryNewRecord(params, repo.get(sourceNumber));
		JSONFieldDescriptorList jsonList = new JSONFieldDescriptorList(rec);
		return jsonList.toJSONString();
    }

	public Set<FieldData> queryNewRecord(Map<String, String> params, AbstractRecordProvider provider) {
    	int     filterIdx = Integer.parseInt(params.get("filterIndex"));
    	int     filterVal = Integer.parseInt(params.get("filterVal"));
    	boolean filterAll = Boolean.parseBoolean(params.get("filterAll"));
		return provider.getFieldsAddingNewFilter(filterIdx, filterVal, filterAll);
    }
}

