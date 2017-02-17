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

package org.gwe.rse.provider.xnat;

import java.util.Map;

import org.gwe.rse.FieldMeta;
import org.gwe.rse.utils.IdentifiableMap;
import org.gwe.rse.utils.TreeDS;

/**
 * @author Marco Ruiz
 * @since Dec 19, 2009
 */
public class XNATMetadata {
	
	private static String URL_PREFIX = "${server}/REST/projects";
	
/*
	${XNAT_SERVER} = http://central.xnat.org
	${PROJECT}     = CENTRAL_OASIS_CS
	${SUBJECT}     = OAS1_0014
	${EXPERIMENT}  = OAS1_0014_MR1
	${SCAN}        = mpr-1

	${XNAT_SERVER}/REST/projects?format=xml
	${XNAT_SERVER}/REST/projects/${PROJECT}/subjects?format=xml
	${XNAT_SERVER}/REST/projects/${PROJECT}/subjects/${SUBJECT}/experiments?format=xml
	${XNAT_SERVER}/REST/projects/${PROJECT}/subjects/${SUBJECT}/experiments/${EXPERIMENT}/scans?format=xml
	${XNAT_SERVER}/REST/projects/${PROJECT}/subjects/${SUBJECT}/experiments/${EXPERIMENT}/scans/${SCAN}/files?format=xml

	http://central.xnat.org/REST/projects/CENTRAL_OASIS_CS/subjects/OAS1_0014/experiments/OAS1_0014_MR1/scans/mpr-1/files?format=xml
 */

	private TreeDS<FieldMeta> root; 
	private Map<String, TreeDS<FieldMeta>> nodesMap; 

	public XNATMetadata() {
		this.root = createNode("project", "Project", "?format=xml", "//row/cell[1]/text()");
		TreeDS<FieldMeta> currNode = root;
		currNode = currNode.addChild(createNode("subject", "Subject",       "/${project}/subjects?format=xml", 
				"//row/cell[2]/text()"));
		
		currNode = currNode.addChild(createNode("experiment", "Experiment", "/${project}/subjects/${subject}/experiments?format=xml", 
				"//row/cell[2]/text()"));
		
		currNode = currNode.addChild(createNode("scan", "Scan",             "/${project}/subjects/${subject}/experiments/${experiment}/scans?format=xml", 
				"//row/cell[1]/text()"));
		
		currNode = currNode.addChild(createNode("file", "File",             "/${project}/subjects/${subject}/experiments/${experiment}/scans/${scan}/files?format=xml", 
				"//row/cell[3]/text()"));
		
		this.nodesMap = IdentifiableMap.createNodesMap(this.root);
    }

	private TreeDS<FieldMeta> createNode(String elementId, String caption, String sufixURLTemplate, String xpathExpr) {
		return new TreeDS<FieldMeta>(new XNATFieldMeta(elementId, caption, "", URL_PREFIX + sufixURLTemplate, xpathExpr));
    }

	public TreeDS<FieldMeta> getRoot() {
    	return root;
    }

	public void setRoot(TreeDS<FieldMeta> root) {
    	this.root = root;
    }

	public XNATFieldMeta getFieldMeta(String fieldId) {
		return (XNATFieldMeta) this.nodesMap.get(fieldId).getElement();
    }
}


