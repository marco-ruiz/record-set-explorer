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

package org.gwe.rse.json;

import java.util.List;
import java.util.Set;

import org.gwe.rse.FieldData;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 * @author Marco Ruiz
 * @since Dec 12, 2009
 */
public class JSONFieldDescriptorList extends JSONAwareList {

	public JSONFieldDescriptorList(Set<FieldData> source) {
		for (FieldData ele : source) add(ele);
	}
	
	public void add(final FieldData source) {
		super.add(new JSONAware() {
			public String toJSONString() {
				List<String> options = source.getOptions();
				JSONObject obj = new JSONObject();
				obj.put("index", source.getIndex());
				obj.put("fieldVal", source.getValue());
				obj.put("sliderVal", computeSliderVal(options.indexOf(source.getValue())));
				obj.put("sliderMax", options.size() - 1);
			    return obj.toJSONString();
			}

			private int computeSliderVal(int sliderVal) {
	            return sliderVal < 0 ? 0 : sliderVal;
            }
		});
	}
}

