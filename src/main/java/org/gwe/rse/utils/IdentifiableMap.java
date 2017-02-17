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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Marco Ruiz
 * @since Dec 18, 2009
 */
public class IdentifiableMap<ID_T, DATA_T extends IIdentifiable<ID_T>> extends HashMap<ID_T, DATA_T> {
	
    public static <ID_T, DATA_T extends IIdentifiable<ID_T>> Map<ID_T, TreeDS<DATA_T>> createNodesMap(TreeDS<DATA_T> root) {
	    Map<ID_T, TreeDS<DATA_T>> result = new HashMap<ID_T, TreeDS<DATA_T>>();
	    result.put(root.getElement().getId(), root);
	    for (TreeDS<DATA_T> node : root.getChildren()) result.putAll(createNodesMap(node));
	    return result;
    }
    
	public IdentifiableMap() {}

    public IdentifiableMap(Map<ID_T, DATA_T> source) {
    	if (source != null) 
    		this.putAll(source);
    }

    public IdentifiableMap(Collection<DATA_T> identifiables) {
    	for (DATA_T fld : identifiables) 
    		put(fld.getId(), fld);
	}
	
	public <META_T extends IIdentifiable<ID_T>> TreeDS<DATA_T> createTreeData(TreeDS<META_T> root) {
		DATA_T rootData = get(root.getElement().getId());
		TreeDS<DATA_T> tree = new TreeDS<DATA_T>(rootData);
	    for (TreeDS<META_T> node : root.getChildren()) 
	    	tree.addChild(createTreeData(node));
		return tree;
    }
}

