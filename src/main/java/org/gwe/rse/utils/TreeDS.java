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

import java.util.ArrayList;
import java.util.List;


/**
 * @author Marco Ruiz
 * @since Dec 15, 2009
 */
public class TreeDS<ELE_T> {
	
	private ELE_T element;
	private TreeDS<ELE_T> parent = null;
	private List<TreeDS<ELE_T>> children = new ArrayList<TreeDS<ELE_T>>();

	public TreeDS(ELE_T element) {
		this.element = element;
	}
	
	public void setParent(TreeDS<ELE_T> parent) {
		this.parent = parent;
	}
	
	public TreeDS<ELE_T> addChild(ELE_T element) {
		return addChild(new TreeDS<ELE_T>(element));
	}

	public TreeDS<ELE_T> addChild(TreeDS<ELE_T> branch) {
		children.add(branch);
		branch.setParent(this);
		return branch;
	}

	public ELE_T getElement() {
    	return element;
    }
	
	public void setElement(ELE_T element) {
    	this.element = element;
    }

	public List<TreeDS<ELE_T>> getChildren() {
    	return children;
    }

	public List<ELE_T> getAncestorsAsList(boolean includeSelf) {
		List<ELE_T> result = new ArrayList<ELE_T>();
		if (parent != null) result.addAll(parent.getAncestorsAsList(true));
		if (includeSelf) result.add(element);
		return result;
	}
	
	public List<ELE_T> getDescendantsAsList(boolean includeSelf) {
		List<ELE_T> result = new ArrayList<ELE_T>();
		if (includeSelf) result.add(element);
		for (TreeDS<ELE_T> child : children)
	        result.addAll(child.getDescendantsAsList(true));
		return result;
	}

	public List<TreeDS<ELE_T>> getDescendantsAsNodeList(boolean includeSelf) {
		List<TreeDS<ELE_T>> result = new ArrayList<TreeDS<ELE_T>>();
		if (includeSelf) result.add(this);
		for (TreeDS<ELE_T> child : children)
	        result.addAll(child.getDescendantsAsNodeList(true));
		return result;
	}
	
	public boolean isParentOf(TreeDS<ELE_T> potentialChild) {
		for (TreeDS<ELE_T> child : children) 
	        if (child.equals(potentialChild) || child.isParentOf(potentialChild)) 
	        	return true;
		return false;
	}
}


