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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.gwe.rse.datasets.IDataSet;

/**
 * @author Marco Ruiz
 * @since Dec 20, 2009
 */
public class DataSetRecordProvider extends AbstractRecordProvider {

	private IDataSet dataSet;
	private Map<String, String> currentRecord = new HashMap<String, String>();
	
    public DataSetRecordProvider(String name) {
	    this(name, null);
    }

    public DataSetRecordProvider(String name, IDataSet data) {
    	super(name);
	    setDataSet(data);
	    this.dataSet = data;
    }

	public IDataSet getDataSet() {
    	return dataSet;
    }

	public void setDataSet(IDataSet data) {
    	this.dataSet = data;
    }

	public List<? extends FieldMeta> readMetadata() {
	    List<FieldMeta> result = new ArrayList<FieldMeta>();
	    for (String field : dataSet.getFields())
	        result.add(new FieldMeta(field, field, dataSet.getFieldValues(field).size() > 1, ""));
		return result;
    }

    public Set<FieldData> readStartUpFields() {
	    return setCurrentRecord(dataSet.getFirstRecord());
    }

    public Set<FieldData> readRecord(String fieldId, int valueOptionIndex, boolean filterAllValues) {
    	Map<String, String> record = dataSet.getBestMatchingRecord(currentRecord, fieldId, valueOptionIndex, filterAllValues);
		return setCurrentRecord(record);
    }

	private Set<FieldData> setCurrentRecord(Map<String, String> record) {
	    currentRecord = record;
	    Set<FieldData> result = new HashSet<FieldData>();
	    Set<String> filtered = dataSet.getFilteredFields();
		for (Entry<String, String> entry : currentRecord.entrySet()) {
	        String id = entry.getKey();
			FieldData field = new FieldData(id, entry.getValue(), dataSet.getFieldValues(id));
	        field.setLocked(filtered.contains(id));
			result.add(field);
		}
		return result;
    }
}

