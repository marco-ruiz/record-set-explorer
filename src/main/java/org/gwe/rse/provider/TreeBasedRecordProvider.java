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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gwe.rse.AbstractRecordProvider;
import org.gwe.rse.FieldData;
import org.gwe.rse.FieldDataMap;
import org.gwe.rse.FieldMeta;
import org.gwe.rse.utils.IdentifiableMap;
import org.gwe.rse.utils.TreeDS;

/**
 * @author Marco Ruiz
 * @since Dec 15, 2009
 */
public abstract class TreeBasedRecordProvider extends AbstractRecordProvider {

	protected Map<String, TreeDS<FieldMeta>> treeNodes;
	protected TreeDS<FieldMeta> treeMetadata;
	
	public TreeBasedRecordProvider(String name) {
		super(name);
    }

	public List<? extends FieldMeta> readMetadata() {
		treeMetadata = readMetadataTree();
		treeNodes = IdentifiableMap.createNodesMap(treeMetadata);
    	return treeMetadata.getDescendantsAsList(true);
    }
    
    public Set<FieldData> readRecord(String fieldId, int valueOptionIndex, boolean filterAllValues) {
		FieldDataMap resultMap = (state != null) ? state.getCurrentData().getAsMap() : new FieldDataMap();
		TreeDS<FieldData> changedNode = resultMap.createTreeData(treeNodes.get(fieldId));
		FieldData changedElement = changedNode.getElement();
		changedElement.setValue(changedElement.getOptions().get(valueOptionIndex));
		for (FieldData field : getBranchUpdates(changedNode, new HashSet<FieldData>()))
    		resultMap.put(field.getId(), field);
		return new HashSet<FieldData>(resultMap.values());
    }

	private Set<FieldData> getBranchUpdates(TreeDS<FieldData> changedNode, Set<FieldData> result) {
		FieldDataMap ancestors = new FieldDataMap(changedNode.getAncestorsAsList(true));
		for (TreeDS<FieldData> child : changedNode.getChildren()) {
			FieldData childEle = getFieldUpdate(child, ancestors, child.getElement().getId());
			child.setElement(childEle);
			result.add(childEle);
	        result.addAll(getBranchUpdates(child, result));
		}
		return result;
	}

	public abstract TreeDS<FieldMeta> readMetadataTree();

	public abstract FieldData getFieldUpdate(TreeDS<FieldData> changedNode, FieldDataMap filters, String fieldId);
}


