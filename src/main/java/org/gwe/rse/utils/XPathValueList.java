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

package org.gwe.rse.utils;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @author Marco Ruiz
 * @since Dec 14, 2009
 */
public class XPathValueList extends ArrayList<String> {

	public XPathValueList(InputStream source, String expression) {
        XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			InputSource inputSource = new InputSource(source);
	        NodeList queryResult = (NodeList) xpath.evaluate(expression, inputSource, XPathConstants.NODESET);
	        int length = queryResult.getLength();
			for (int idx = 0; idx < length; idx++) {
		        Node item = queryResult.item(idx);
				String value = (item.getNodeType() == Node.ATTRIBUTE_NODE || expression.endsWith("text()")) ? 
						item.getTextContent() : createXMLString(item);
				value = value.replaceAll("^\\s*", "").replaceAll("\\s*$", "");
				add(value);
            }
			if (size() == 0) add("");
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

	private String createXMLString(Node source) throws TransformerFactoryConfigurationError, TransformerException {
    	Transformer transformer = TransformerFactory.newInstance().newTransformer();
    	transformer.setOutputProperty(OutputKeys.INDENT, "yes");

    	StreamResult result = new StreamResult(new StringWriter());
    	transformer.transform(new DOMSource(source), result);

    	return result.getWriter().toString();
    }
}

