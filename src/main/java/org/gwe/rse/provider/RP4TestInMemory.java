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

package org.gwe.rse.provider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gwe.rse.AbstractRecordProvider;
import org.gwe.rse.FieldData;
import org.gwe.rse.FieldMeta;

/**
 * @author Marco Ruiz
 * @since Dec 11, 2009
 */
public class RP4TestInMemory extends AbstractRecordProvider {

	private int counter = 0;
	
	public RP4TestInMemory(String name) {
		super(name);
    }

	public List<? extends FieldMeta> readMetadata() {
		List<FieldMeta> meta = new ArrayList<FieldMeta>();
		meta.add(new FieldMeta("id"   , "ID"     , false, "text"));
		meta.add(new FieldMeta("name" , "NAME"   , false, "text"));
		meta.add(new FieldMeta("i"    , "index 1", true , "text"));
		meta.add(new FieldMeta("image", "Image"  , false, "img"));
		meta.add(new FieldMeta("j"    , "Index 2", true , "text"));
		meta.add(new FieldMeta("k"    , "Index 3", true , "text"));
		meta.add(new FieldMeta("cmd"  , "Command", false, "text"));
		meta.add(new FieldMeta("l"    , "Index 4", true , "text"));
		return meta;
    }
	
	public Set<FieldData> readStartUpFields() {
		return createRecord("echo 'test'", 99);
    }

    public Set<FieldData> readRecord(String fieldId, int valueOptionIndex, boolean filterAllValues) {
		return createRecord(fieldId, valueOptionIndex);
    }
	
	private Set<FieldData> createRecord(Object cmdValue, int limit) {
		Set<FieldData> data = new HashSet<FieldData>();
		data.add(new FieldData("id"   , Math.random() + ""));
		data.add(new FieldData("name" , "Marco"   ));
		data.add(new FieldData("i"    , "11"         , range(0, limit * 50, 2)));
		data.add(new FieldData("image", counter++ + ".jpg"  ));
		data.add(new FieldData("j"    , "22"         , range(0, 88, 11)));
		data.add(new FieldData("k"    , "33"         , range(20, 50, 1)));
		data.add(new FieldData("cmd"  , cmdValue.toString()));
		data.add(new FieldData("l"    , "44"         , range(4, 100, 4)));
	    return data;
    }

	public List<String> range(int start, int end, int step) {
		List<String> result = new ArrayList<String>();
		for (int idx = start; idx < end; idx += step) result.add(idx + "");
		return result;
	}
}

