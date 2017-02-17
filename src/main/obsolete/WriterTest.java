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

package org.gwe.sde.web;

import java.util.ArrayList;
import java.util.List;

import org.gwe.sde.model.FieldDescriptor;
import org.gwe.sde.model.FieldMetadata;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 * @author Marco Ruiz
 * @since Dec 12, 2009
 */
public class WriterTest {

	public static void main(String[] args) {
		List<FieldMetadata> fldMeta = new ArrayList<FieldMetadata>();
		fldMeta.add(new FieldMetadata("id", "ID", false, "text"));
		fldMeta.add(new FieldMetadata("name", "NAME", false, "text"));
		fldMeta.add(new FieldMetadata("i", "index 1", true, "text"));
		fldMeta.add(new FieldMetadata("image", "Image", false, "img"));
		fldMeta.add(new FieldMetadata("j", "Index 2", true, "text"));
		fldMeta.add(new FieldMetadata("k", "Index 3", true, "text"));
		fldMeta.add(new FieldMetadata("cmd", "Command", false, "text"));
		fldMeta.add(new FieldMetadata("l", "Index 4", true, "text"));
		
        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("FieldMetadata", FieldMetadata.class);
        xstream.alias("FieldDescriptor", FieldDescriptor.class);

        String jsonStr = xstream.toXML(fldMeta);
		System.out.println(jsonStr);
        
		List<FieldMetadata> copy = (List<FieldMetadata>) xstream.fromXML(jsonStr);
	}

}

