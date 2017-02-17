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

package org.gwe.rse.datasets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * @author Marco Ruiz
 * @since Dec 20, 2009
 */
public class DataSet implements IDataSet {

	private final Record fields;
	private final List<Record> records = new ArrayList<Record>();
	private FieldValues fieldValues;
	
	public DataSet(DataSetSource source) {
		this(source.getFields(), source.getRecords());
	}
	
	public DataSet(List<String> fields, List<List<String>> records) {
		this.fields = new Record(fields);
		for (List<String> record : records) 
			this.records.add(new Record(record));
		fieldValues = new FieldValues();
	}
	
	public void addRecordNumberField(String id) {
		fields.add(0, id);
		for (int idx = 0; idx < records.size(); idx++) 
			records.get(idx).add(0, String.valueOf(idx + 1));
		fieldValues.putDistinctValuesOfField(0);
	}

	public List<String> getFields()                    { return fields;                       }
    public Map<String, String> getFirstRecord()        { return records.get(0).getAsMap();    }
	public List<String> getFieldValues(String fieldId) { return fieldValues.get(fieldId);     }
	public Set<String> getFilteredFields()             { return fieldValues.filteredFields; }

	public Map<String, String> getBestMatchingRecord(Map<String, String> masterRecord, String fieldId, int valueOptionIndex, boolean filterAllValues) {
		Record master = new Record(masterRecord);
		int fieldIdx = fields.indexOf(fieldId);
		String fieldVal = fieldValues.get(fieldId).get(valueOptionIndex);
		List<Record> recordSet = fieldValues.getFilteredRecordSet(fieldId, valueOptionIndex, filterAllValues);
		return getBestMatchingRecord(master, recordSet, fieldIdx, fieldVal).getAsMap();
    }
    
    private Record getBestMatchingRecord(Record master, List<Record> recordSet, int fieldIdx, String fieldVal) {
    	Match best = new Match(null, 0);
		for (int idx = 0; idx < recordSet.size(); idx++) {
			Match current = new Match(recordSet.get(idx), 0);
			if (current.getRecord().get(fieldIdx).equals(fieldVal)) {
				current.computeScoreAgainst(master);
				if (best.getRecord() == null || current.getScore() > best.getScore()) 
					best = current;
			}
		}
		return best.getRecord();
    }
    
    //====================
    // FIELD VALUES CLASS
    //====================
    class FieldValues extends HashMap<String, List<String>> {
    	
    	private List<Record> subRecords = new ArrayList<Record>();
		private Map<Integer, String> filters = new HashMap<Integer, String>();
		private Set<String> filteredFields = new HashSet<String>();

		public FieldValues() { computeFilteredRecords(); }
		
    	public List<Record> getFilteredRecordSet(String fieldId, int valueOptionIndex, boolean filterAllValues) {
			if (filterAllValues) {
				this.filters.put(fields.indexOf(fieldId), get(fieldId).get(valueOptionIndex));
				this.filteredFields.add(fieldId);
			} else {
				this.filters.remove(fields.indexOf(fieldId));
				this.filteredFields.remove(fieldId);
			}

			computeFilteredRecords();
    		return subRecords;
        }

		private void computeFilteredRecords() {
	        // Compute sub records
	        this.subRecords.clear();
			for (Record record : records) 
				if (passesFilters(record)) subRecords.add(record);
			
    		for (int idx = 0; idx < fields.size(); idx++) 
    			putDistinctValuesOfField(idx);
        }
		
		private boolean passesFilters(Record record) {
	        for (Entry<Integer, String> entry : filters.entrySet())
	        	if (!record.get(entry.getKey()).equals(entry.getValue())) return false;
	        return true;
        }
    	
    	public void putDistinctValuesOfField(int idx) {
    	    List<String> repo = new ArrayList<String>();
    	    put(fields.get(idx), repo);
    	    for (Record record : subRecords) {
    	        String recFieldVal = record.get(idx);
    	    	if (!repo.contains(recFieldVal)) repo.add(recFieldVal);
    	    }
        }
    }
    
    //=============
    // MATCH CLASS
    //=============
	class Match {
		private Record record = null;
		private int score = 0;

		public Match(Record record, int score) {
	        this.record = record;
	        this.score = score;
        }
		
		public void computeScoreAgainst(List<String> master) {
			score = 0;
			for (int idx = 0; idx < fields.size(); idx++) 
				if (master.get(idx).equals(record.get(idx))) score++;
        }

		public int getScore()     { return score;  }
		public Record getRecord() { return record; }
	}

    //==============
    // RECORD CLASS
    //==============
	class Record extends ArrayList<String> {

		public Record(Collection<? extends String> values) { super(values); }
		public Record(String line)                         { super(Arrays.asList(line.split(","))); }
		
		public Record(Map<String, String> values) { 
	    	for (String field : fields) 
	    		add(values.get(field));
		}
		
		public Map<String, String> getAsMap() {
		    Map<String, String> result = new HashMap<String, String>();
			for (int idx = 0; idx < fields.size(); idx++) 
				result.put(fields.get(idx), get(idx));
			return result;
	    }
	}
}

