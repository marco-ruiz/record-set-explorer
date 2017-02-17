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

import java.util.HashMap;
import java.util.Map;

import org.gwe.rse.AbstractRecordProvider;

/**
 * @author Marco Ruiz
 * @since Dec 11, 2009
 */
public class RecordProviderFactory {

	private Map<Class<? extends AbstractRecordProvider>, ProviderCreator<? extends AbstractRecordProvider>> provCreators = 
		new HashMap<Class<? extends AbstractRecordProvider>, ProviderCreator<? extends AbstractRecordProvider>>();
	
	public RecordProviderFactory() {
		provCreators.put(RP4CSVLocal.class, new ProviderCreator<RP4CSVLocal>() {
			public RP4CSVLocal create(String name, Map<String, String> params) throws Exception {
	            return new RP4CSVLocal(name, params.get("sourceCSV"), "Record Number");
            }
		});
		
		provCreators.put(RP4CSVRemote.class, new ProviderCreator<RP4CSVRemote>() {
			public RP4CSVRemote create(String name, Map<String, String> params) throws Exception {
	            return new RP4CSVRemote(name, params.get("sourceURL"), params.get("sourceUser"), params.get("sourcePassword"), params.get("sourcePost"), "Record Number");
            }
		});
		
		provCreators.put(RP4REST.class, new ProviderCreator<RP4REST>() {
			public RP4REST create(String name, Map<String, String> params) throws Exception {
	            return new RP4REST(name);
            }
		});
		
		provCreators.put(RP4LocalFS.class, new ProviderCreator<RP4LocalFS>() {
			public RP4LocalFS create(String name, Map<String, String> params) throws Exception {
	            return new RP4LocalFS(name);
            }
		});
		
		provCreators.put(RP4TestInMemory.class, new ProviderCreator<RP4TestInMemory>() {
			public RP4TestInMemory create(String name, Map<String, String> params) throws Exception {
	            return new RP4TestInMemory(name);
            }
		});
	}
	
	public <PROV_T extends AbstractRecordProvider> PROV_T create(Class<PROV_T> provClass, Map<String, String> params) {
		PROV_T result = null;
        try {
        	result = (PROV_T) provCreators.get(provClass).create(params.get("sourceName"), params);
        	result.init();
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return result;
	}
}

interface ProviderCreator<PROV_T extends AbstractRecordProvider> {
	PROV_T create(String name, Map<String, String> params) throws Exception;
}

