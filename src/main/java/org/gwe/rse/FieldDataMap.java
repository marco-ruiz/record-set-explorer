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

import java.util.Collection;
import java.util.Map;

import org.gwe.rse.utils.IdentifiableMap;

/**
 * @author Marco Ruiz
 * @since Dec 19, 2009
 */
public class FieldDataMap extends IdentifiableMap<String, FieldData> {

	public FieldDataMap() {
	    super();
    }

	public FieldDataMap(Collection<FieldData> fields) {
	    super(fields);
    }

	public FieldDataMap(Map<String, FieldData> source) {
	    super(source);
    }
}
