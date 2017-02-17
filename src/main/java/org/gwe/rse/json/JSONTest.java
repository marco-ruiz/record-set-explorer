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


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gwe.rse.FieldData;
import org.gwe.rse.FieldMeta;

/**
 * @author Marco Ruiz
 * @since Dec 12, 2009
 */
public class JSONTest {

	public static void main(String[] args) {
		List<FieldMeta> meta = new ArrayList<FieldMeta>();
		meta.add(new FieldMeta("id", "ID", false, "text"));
		meta.add(new FieldMeta("name", "NAME", false, "text"));
		meta.add(new FieldMeta("i", "index 1", true, "text"));
		meta.add(new FieldMeta("image", "Image", false, "img"));
		meta.add(new FieldMeta("j", "Index 2", true, "text"));
		meta.add(new FieldMeta("k", "Index 3", true, "text"));
		meta.add(new FieldMeta("cmd", "Command", false, "text"));
		meta.add(new FieldMeta("l", "Index 4", true, "text"));
		
	    System.out.println(new JSONFieldMetadataList(meta).toJSONString());
		
		Set<FieldData> data = new HashSet<FieldData>();
		data.add(new FieldData("cmd", "echo $i"));
		data.add(new FieldData("id", "4", "1", "2", "3", "4", "5", "6", "7", "8"));
		data.add(new FieldData("cmd-alt", "echo $j"));
		data.add(new FieldData("name", "Marco", "Marco", "Antonio", "Ruiz", "Huapaya"));
		data.add(new FieldData("img", "whatever", "whatever"));

	    System.out.println(new JSONFieldDescriptorList(data).toJSONString());
	}
}

