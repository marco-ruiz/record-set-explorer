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

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gwe.rse.FieldData;
import org.gwe.rse.FieldDataMap;
import org.gwe.rse.FieldMeta;
import org.gwe.rse.utils.TreeDS;

/**
 * @author Marco Ruiz
 * @since Dec 11, 2009
 */
public class RP4LocalFS extends TreeBasedRecordProvider {
	
	private String rootPath = "/Users";
	
    public RP4LocalFS(String name) {
    	super(name);
    }

	public TreeDS<FieldMeta> readMetadataTree() {
	    TreeDS<FieldMeta> result = new TreeDS<FieldMeta>(new FieldMeta("root", "Root", true, ""));
	    TreeDS<FieldMeta> currNode = result; 
	    
	    currNode = currNode.addChild(new FieldMeta("level1A", "Level 1-A", true, ""));
	    currNode = currNode.addChild(new FieldMeta("level2A", "Level 2-A", true, ""));
	    currNode = currNode.addChild(new FieldMeta("level3A", "Level 3-A", true, ""));
	    
	    currNode = result;
	    currNode = currNode.addChild(new FieldMeta("level1B", "Level 1-B", true, ""));
	    currNode = currNode.addChild(new FieldMeta("level2B", "Level 2-B", true, ""));
	    
		return result;
    }

    public Set<FieldData> readStartUpFields() {
    	List<FieldData> ancestors = new ArrayList<FieldData>();
    	
	    Set<FieldData> result = new HashSet<FieldData>();
	    FieldData rootData = addDirData("root",    ancestors, "admin", result);
	    addDirData("level1A", ancestors, "work", result);
	    addDirData("level2A", ancestors, "eclipse-ws", result);
	    addDirData("level3A", ancestors, "gwe-core", result);
	    
	    ancestors.clear();
	    ancestors.add(rootData);
	    
	    addDirData("level1B", ancestors, "gwe-daemon", result);
	    addDirData("level2B", ancestors, "gwe-0.7.3.alpha", result);
		return result;
    }

	private FieldData addDirData(String id, List<FieldData> ancestors, String path, Set<FieldData> result) {
	    FieldData fldData = createDirData(id, ancestors, path);
		result.add(fldData);
	    ancestors.add(fldData);
	    return fldData;
    }

    public FieldData getFieldUpdate(TreeDS<FieldData> changedNode, FieldDataMap filters, String fieldId) {
		return createDirData(fieldId, changedNode.getAncestorsAsList(true), "");
    }
    
    private FieldData createDirData(String id, List<FieldData> ancestors, String path) {
    	String fullPath = rootPath;
    	for (FieldData fldData : ancestors) 
    		fullPath += "/" + fldData.getValue();

    	List<String> subDirs = new ArrayList<String>();
		for (File file : new File(fullPath).listFiles()) 
	        if (file.isDirectory()) 
	        	subDirs.add(file.getName());

        return new FieldData(id, path, subDirs.toArray(new String[subDirs.size()]));
    }
}


