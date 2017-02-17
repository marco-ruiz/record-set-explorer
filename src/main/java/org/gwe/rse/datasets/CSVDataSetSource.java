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
import java.util.List;

/**
 * @author Marco Ruiz
 * @since Dec 20, 2009
 */
public class CSVDataSetSource implements DataSetSource {
	
	private List<String>       fields;
	private List<List<String>> records = new ArrayList<List<String>>();

	public CSVDataSetSource(String source) {
	    String[] lines = source.split("\n");
		fields = createRecord(lines[0]);
		for (int idx = 1; idx < lines.length; idx++) 
			records.add(createRecord(lines[idx]));
    }

	public List<String>       getFields()  { return fields;  }
	public List<List<String>> getRecords() { return records; }

	private static List<String> createRecord(String line) {
		ArrayList<String> result = new ArrayList<String>();
		List<String> elements = Arrays.asList(line.split(","));
		for (String ele : elements) {
	        if (ele.startsWith("\"") || ele.startsWith("'")) ele = ele.substring(1);
	        if (ele.endsWith("\"")   || ele.endsWith("'"))   ele = ele.substring(0, ele.length() - 1);
	        result.add(ele);
        }
		
		return result;
	}
}

