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

import java.util.List;
import java.util.Set;


/**
 * @author Marco Ruiz
 * @since Dec 17, 2009
 */
public class RecordProviderState {
	
	private final RecordMeta meta;
	private final RecordData initialData;
	private RecordData currentData;
	
	public RecordProviderState(List<? extends FieldMeta> metadata, Set<FieldData> startUpFields) {
		this(new RecordMeta(metadata), startUpFields);
    }

	public RecordProviderState(RecordMeta metadata, Set<FieldData> startUpFields) {
		this(metadata, new RecordData(metadata, startUpFields));
    }

	public RecordProviderState(RecordMeta meta, RecordData initialData) {
	    this.meta = meta;
	    this.initialData = initialData;
	    this.currentData = this.initialData.clone();
    }

	public RecordMeta getMeta() {
    	return meta;
    }
	
	public RecordData getInitialData() {
    	return initialData;
    }

	public RecordData getCurrentData() {
    	return currentData;
    }
	
	public String getCurrentFieldValue(String fieldId) {
	    FieldData fldData = currentData.getAsMap().get(fieldId);
        return (fldData == null) ? "" : fldData.getValue();
    }

	public String getFieldOption(String fieldId, int valueOptionIndex) {
	    FieldData fldData = currentData.getAsMap().get(fieldId);
        return (fldData == null) ? "" : fldData.getOptions().get(valueOptionIndex);
    }

	public void setCurrentData(RecordData currentData) {
    	this.currentData = currentData;
    }

	public void setCurrentFields(Set<FieldData> fields) {
		setCurrentData(new RecordData(meta, fields));
    }

	public void updateCurrentData(String fieldId, String selectedOption) {
	    FieldData fldData = currentData.getAsMap().get(fieldId);
	    updateCurrentData(fieldId, (fldData == null) ? 0 : fldData.getOptions().indexOf(selectedOption));
    }

	public void updateCurrentData(String fieldId, Integer valueOptionIndex) {
    	currentData.updateField(fieldId, valueOptionIndex);
    }
}

