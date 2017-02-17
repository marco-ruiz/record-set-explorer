/*
 * Copyright 2007-2008 the original author or authors. Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a copy of the
 * License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.gwe.rse.datasets;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Marco Ruiz
 * @since Dec 21, 2009
 */
public interface IDataSet {
	
	public void addRecordNumberField(String id);
	
	public List<String> getFields();
	
	public Map<String, String> getFirstRecord();
	
	public List<String> getFieldValues(String fieldId);
	
	public Set<String> getFilteredFields();

	public Map<String, String> getBestMatchingRecord(Map<String, String> masterRecord, String fieldId, int valueOptionIndex, boolean filterAllValues);
	
}