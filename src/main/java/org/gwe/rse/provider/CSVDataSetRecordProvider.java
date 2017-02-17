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

import java.io.FileNotFoundException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;

import org.gwe.rse.DataSetRecordProvider;
import org.gwe.rse.datasets.CSVDataSetSource;
import org.gwe.rse.datasets.DataSet;
import org.gwe.rse.utils.HTTPRequester;

/**
 * @author Marco Ruiz
 * @since Dec 23, 2009
 */
public class CSVDataSetRecordProvider extends DataSetRecordProvider {

	public CSVDataSetRecordProvider(String name) {
		super(name);
    }

	public static String readURL(String targetURL, String user, String password, String query) throws MalformedURLException, Exception, FileNotFoundException {
	    boolean postFile = query != null;
	    HTTPRequester requester = new HTTPRequester(targetURL, postFile);
	    requester.setAuth(user, password);
		StringReader input = postFile ? new StringReader(query) : null;
        return requester.connect(new StringWriter(), input).toString();
    }

	//=======
	// CLASS
	//=======
	public void setDataSet(String targetURL, String user, String password, String queryFilePath) throws MalformedURLException, FileNotFoundException, Exception {
		setDataSet(readURL(targetURL, user, password, queryFilePath));
    }

	public void setDataSet(String source) {
	    super.setDataSet(new DataSet(new CSVDataSetSource(source)));
    }
	
	public void addRecordNumberField(String recordNumberField) {
		if (recordNumberField == null || recordNumberField.equals("")) return;
		getDataSet().addRecordNumberField(recordNumberField);
	}
}

