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
import java.util.Arrays;
import java.util.List;

import org.gwe.rse.utils.IIdentifiable;

/**
 * @author Marco Ruiz
 * @since Dec 12, 2009
 */
public class FieldData implements IIdentifiable<String> {
	
	private int index;
	private String id;
	private String value;
	private boolean locked = false;
	private List<String> options;

	public FieldData(String id, String value) {
		this(id, value, new ArrayList<String>());
    }

	public FieldData(String id, String value, String... options) {
		this(id, value, new ArrayList<String>(Arrays.asList(options)));
	}
	
	public FieldData(String id, String value, List<String> options) {
		setId(id);
		setValue(value);
	    setOptions(options);
    }

	public int getIndex() {
    	return index;
    }

	public void setIndex(int index) {
    	this.index = index;
    }

	public String getId() {
    	return id;
    }

	public void setId(String name) {
		if (name == null) name = "";
    	this.id = name;
    }

	public String getValue() {
    	return value;
    }

	public void setValue(String value) {
		if (value == null) value = "";
    	this.value = value;
    }
	
	public void setLocked(boolean locked) {
	    this.locked = locked;
    }

	public boolean isLocked() {
	    return locked;
    }

	public List<String> getOptions() {
    	return options;
    }
	
	public void setOptions(List<String> options) {
		if (options == null) options = new ArrayList<String>();
    	this.options = options;
    }
	
	public String toString() {
		return value;
	}
}

