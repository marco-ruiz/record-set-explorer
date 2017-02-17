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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author Marco Ruiz
 * @since Dec 11, 2009
 */
public abstract class AbstractRecordProvider {

	public static final AbstractRecordProvider NULL_RECORD_PROVIDER = new AbstractRecordProvider("System Null Record Provider") {
		public Set<FieldData> readStartUpFields() { return new HashSet<FieldData>(); }
		public Set<FieldData> readRecord(String fieldId, int valueOptionIndex, boolean filterAllValues) { return new HashSet<FieldData>(); }
		public List<? extends FieldMeta> readMetadata() { return new ArrayList<FieldMeta>(); }
	};

	static {
		NULL_RECORD_PROVIDER.init();
	}

	//========
	// CLASS
	//========
	private String name;

	protected RecordProviderState state;

	public AbstractRecordProvider(String name) { this.name = name; }

	public void init() {
	    this.state = new RecordProviderState(readMetadata(), readStartUpFields());
    }

	public String getName() {
    	return name;
    }

	public final List<? extends FieldMeta> getMetadata() {
		return state.getMeta().getAsList();
	}

	public final Map<String, ? extends FieldMeta> getMetadataMap() {
		return state.getMeta().getAsMap();
	}

	public final Set<FieldData> getStartUpFields() {
		Set<FieldData> fields = state.getInitialData().getAsSet();
		setIndexes(fields);
		return fields;

	}

	public Set<FieldData> getFieldsAddingNewFilter(int fieldIndex, int valueOptionIndex, boolean filterAllPossibleValues) {
		String fieldId = state.getMeta().getAsList().get(fieldIndex).getId();
		state.updateCurrentData(fieldId, valueOptionIndex);
		String selectedOption = state.getFieldOption(fieldId, valueOptionIndex);
		Set<FieldData> fields = readRecord(fieldId, valueOptionIndex, filterAllPossibleValues);
		state.setCurrentFields(fields);
		state.updateCurrentData(fieldId, selectedOption);
		setIndexes(fields);
		return fields;
    }

	private void setIndexes(Set<FieldData> fields) {
        Map<String, ? extends FieldMeta> metaMap = state.getMeta().getAsMap();
		for (FieldData field : fields) {
			FieldMeta fldMeta = metaMap.get(field.getId());
			field.setIndex(fldMeta.getIndex());
        }
    }

	public abstract List<? extends FieldMeta> readMetadata();

	public abstract Set<FieldData> readStartUpFields();

	public abstract Set<FieldData> readRecord(String fieldId, int valueOptionIndex, boolean filterAllValues);
}

