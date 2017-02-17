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

package org.gwe.rse.servlet;

import java.util.Map;

import javax.servlet.ServletException;

import org.gwe.rse.AbstractRecordProvider;
import org.gwe.rse.provider.RP4CSVLocal;
import org.gwe.rse.provider.RP4CSVRemote;
import org.gwe.rse.provider.RecordSetProviderRepo;


/**
 * @author Marco Ruiz
 * @since Dec 13, 2008
 */
public class SaveRecordSetServlet extends BrowserServlet {

	public static final String URI = "/save";

    protected String getOutput(Map<String, String> params, RecordSetProviderRepo repo) throws ServletException {
        boolean isLocal = params.remove("sourceType").equals("1");
		Class<? extends AbstractRecordProvider> clazz = isLocal ? RP4CSVLocal.class : RP4CSVRemote.class;
		return buildOutput(clazz, params, repo);
    }
}

