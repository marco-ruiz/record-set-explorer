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

import org.gwe.rse.FieldMeta;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 * @author Marco Ruiz
 * @since Dec 12, 2009
 */
public class JSONFieldMetadataList extends JSONAwareList {

	public JSONFieldMetadataList(List<? extends FieldMeta> source) {
		for (FieldMeta ele : source) add(ele);
	}
	
	public void add(final FieldMeta source) {
		super.add(new JSONAware() {
			public String toJSONString() {
				JSONObject obj = new JSONObject();
				obj.put("caption", source.getCaption());
				obj.put("filter", source.isFilter());
				obj.put("renderingFunctionName", source.getRenderingFunctionName());
			    return obj.toJSONString();
			}
		});
	}
}

