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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.gwe.rse.FieldData;
import org.gwe.rse.FieldDataMap;
import org.gwe.rse.FieldMeta;
import org.gwe.rse.provider.TreeBasedRecordProvider;
import org.gwe.rse.utils.TreeDS;

import sun.misc.BASE64Encoder;

/**
 * @author Marco Ruiz
 * @since Dec 11, 2009
 */
public class RP4XNAT extends TreeBasedRecordProvider {

	private static XNATMetadata metadata = new XNATMetadata();
	
	private String serverPrefix;
	private String auth;
	
	public RP4XNAT(String name, Map<String, String> connParams) {
		super(name);
	    this.serverPrefix = "http://" + connParams.get("server");
	    String user       = connParams.get("user");
	    String password   = connParams.get("password");
	    
	    if (user != null && password != null) {
		    this.auth = user + ":" + password;
		    this.auth = new BASE64Encoder().encode(auth.getBytes());
	    }
    }

    public TreeDS<FieldMeta> readMetadataTree() {
    	return metadata.getRoot();
    }

    public Set<FieldData> readStartUpFields() {
        try {
    		FieldDataMap repo = populate(new FieldDataMap(), (TreeDS<? extends FieldMeta>) metadata.getRoot());
		    return new HashSet<FieldData>(repo.values());
        } catch (Exception e) {}
        return new HashSet<FieldData>();
    }

	private FieldDataMap populate(FieldDataMap repo, TreeDS<? extends FieldMeta> currentNode) throws Exception {
	    String id = currentNode.getElement().getId();
		repo.put(id, getFieldData(id, repo, ""));
		for (TreeDS<? extends FieldMeta> child : currentNode.getChildren()) 
			populate(repo, child);
		return repo;
    }
	
    public FieldData getFieldUpdate(TreeDS<FieldData> changedNode, FieldDataMap ancestors, String fieldId) {
    	String currentValue = state.getCurrentFieldValue(fieldId);
		try {
			return getFieldData(fieldId, ancestors, currentValue);
        } catch (Exception e) {
        	return new FieldData(fieldId, currentValue);
        }
    }

	private FieldData getFieldData(String fieldId, FieldDataMap filters, String currentValue) throws Exception {
	    return metadata.getFieldMeta(fieldId).getData(serverPrefix, filters, currentValue, auth);
    }
}


