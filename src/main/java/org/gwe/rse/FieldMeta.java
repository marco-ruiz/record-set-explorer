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

import org.gwe.rse.utils.IIdentifiable;

/**
 * @author Marco Ruiz
 * @since Dec 11, 2009
 */
public class FieldMeta implements IIdentifiable<String> {
	
	private int index;
	private String id;
	private String caption;
	private boolean filter;
	private String renderingFunctionName;

	public FieldMeta(String id, String caption, boolean filter, String renderingFunctionName) {
	    this.id = id;
	    this.caption = caption;
	    this.filter = filter;
	    this.renderingFunctionName = renderingFunctionName;
    }
	
	public int getIndex() {
    	return index;
    }

	public void setIndex(int index) {
    	this.index = index;
    }

	public String getId() {
    	return id;
    }
	
	public void setId(String name) {
    	this.id = name;
    }
	
	public String getCaption() {
    	return caption;
    }

	public void setCaption(String caption) {
    	this.caption = caption;
    }

	public boolean isFilter() {
    	return filter;
    }
	
	public void setFilter(boolean selectable) {
    	this.filter = selectable;
    }
	
	public String getRenderingFunctionName() {
    	return renderingFunctionName;
    }
	
	public void setRenderingFunctionName(String renderingFunctionName) {
    	this.renderingFunctionName = renderingFunctionName;
    }
}

