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

package org.gwe.rse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author Marco Ruiz
 * @since Dec 17, 2009
 */
public class RecordData {

	private static Map<String, Integer> validateValuesAndResolveFilters(RecordMeta metadata, Set<FieldData> fields) {
	    Map<String, Integer> filter = new HashMap<String, Integer>();
		for (FieldData data : fields) {
	        FieldMeta fldMeta = metadata.getAsMap().get(data.getId());
	        if (fldMeta.isFilter()) 
	        	filter.put(data.getId(), validateValueOptionAndGetIndex(data));
        }
		return filter;
    }
	
	private static int validateValueOptionAndGetIndex(FieldData data) {
	    List<String> options = data.getOptions();
	    int dataIndex = options.indexOf(data.getValue());
		if (dataIndex == -1) {
			dataIndex = 0;
	    	data.setValue(options.size() == 0 ? "" : options.get(0));
		}
		return dataIndex;
    }
	
	private Set<FieldData> fields;
	private FieldDataMap fieldsMap;
	
	public RecordData(RecordMeta metadata, Set<FieldData> fields) {
		this(fields);
		validateValuesAndResolveFilters(metadata, fields);
	}
	
	private RecordData(Set<FieldData> fields) {
		this.fields = new HashSet<FieldData>(fields);
		this.fieldsMap = new FieldDataMap(fields);
	}
	
	public Set<FieldData> getAsSet() {
    	return fields;
    }

	public FieldDataMap getAsMap() {
    	return fieldsMap;
    }
	
	public void updateField(String fieldId, Integer valueOptionIndex) {
		FieldData data = this.fieldsMap.get(fieldId);
		data.setValue(data.getOptions().get(valueOptionIndex));
	}
	
	public RecordData clone() {
		return new RecordData(fields);
	}
}


