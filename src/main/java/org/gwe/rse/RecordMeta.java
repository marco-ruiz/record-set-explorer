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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.gwe.rse.utils.IdentifiableMap;


/**
 * @author Marco Ruiz
 * @since Dec 17, 2009
 */
public class RecordMeta {
	
	private List<? extends FieldMeta> metadata;
	private IdentifiableMap<String, ? extends FieldMeta> metadataMap;
	
	public <META_T extends FieldMeta> RecordMeta(List<META_T> metadata) {
		for (int idx = 0; idx < metadata.size(); idx++) metadata.get(idx).setIndex(idx);
	    this.metadata    = Collections.unmodifiableList(metadata);
		this.metadataMap = new IdentifiableMap<String, META_T>(metadata);
    }
	
	public List<? extends FieldMeta> getAsList() {
    	return metadata;
    }
	
	public Map<String, ? extends FieldMeta> getAsMap() {
    	return metadataMap;
    }
}

