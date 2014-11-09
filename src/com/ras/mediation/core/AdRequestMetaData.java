/*******************************************************************************
 * Copyright 2014 Richard So
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.ras.mediation.core;

import java.util.HashMap;
import java.util.StringTokenizer;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;

/**
 * This is class is a meta data container that is to be served on an individual request level.
 * 
 * @author Richard So
 */
public class AdRequestMetaData extends HashMap<MetaData, String> {
	private static final long serialVersionUID = 1L;

	/**
	 * Returns the first non null value from the meta data passed in in order.
	 * @param key The value desired
	 * @param metaDatas The list of meta data to attempt to look for data in order of return
	 * @return The first non null value found or null if it is not found
	 */
	public static String coalesceData(MetaData key, AdRequestMetaData... metaDatas) {
		for (AdRequestMetaData metaData: metaDatas) {
			String value = metaData.get(key);
			if (value != null)
				return value;
		}
		return null;
	}
	
	/**
	 * This method will load meta data values for a line delimited string
	 * @param metaDataText The list of meta data in line delimited string for each property and key=value format on each line
	 * @param adRequestMetaData The meta data to load the global meta data information into
	 * @param individualMetaData A hashmap to load any individual ad id meta data into
	 */
	public static void loadMetaData(String metaDataText, AdRequestMetaData adRequestMetaData, HashMap<String, AdRequestMetaData> individualMetaData) {
		//Verify data was actually returned
		if (metaDataText == null || metaDataText.trim().equals(""))
			return;

		StringTokenizer st = new StringTokenizer(metaDataText);
		
		while (st.hasMoreTokens()) {
			String line = st.nextToken();
			if (line.trim().length() == 0)
				continue;
			
			String[] parts = line.split("=");
			String key = parts[0];
			MetaData metaData = MetaData.getMetaData(key);
			if (metaData != null)
				adRequestMetaData.put(metaData, parts[1]);
			else {
				AdRequestMetaData individual = individualMetaData.get(key);
				if (individual == null) {
					individual = new AdRequestMetaData();
					individualMetaData.put(key, individual);
				}
				loadIndividualData(individual,parts[1]);
			}
		}
	}
	
	/**
	 * Load meta data from AndroidManifest.xml
	 * @param context The context of your application that will allow access to AndroidManifest.xml meta data
	 * @param adRequestMetaData The meta data to load the global meta data information into
	 * @param individualMetaData A hashmap to load any individual ad id meta data into
	 */
	public static void readMetaData(Context context, AdRequestMetaData adRequestMetaData, HashMap<String, AdRequestMetaData> individualMetaData) {
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
		    Bundle bundle = ai.metaData;
		    
		    if (bundle == null) {
		    	Log.d("AdRequestMetaData", "No meta data detected. Resolving to defaults.");
		    	return;
		    }
		    
		    for (MetaData metaData: MetaData.values()) {
		    	String key = metaData.getKey();
				String value = bundle.getString(key);
		    	if (value != null)
		    		adRequestMetaData.put(metaData, value);
		    }
		    
		    String value = adRequestMetaData.get(MetaData.AD_IDS);
		    
		    if (value != null) {
		    	String[] ids = value.split(";");
		    	
		    	for (String id: ids) {
		    		AdRequestMetaData individual = new AdRequestMetaData();
		    		individualMetaData.put(id, individual);
		    		loadIndividualData(individual, bundle.getString(id));
		    	}
		    }
		} 
		catch (NameNotFoundException e) {
			Log.e("AdRequestMetaData", "Unable to load metadata from context resolving to defaults", e);
		}
	}
	
	private static void loadIndividualData(AdRequestMetaData individual, String value) {
		String[] parts = value.split(";");
		for (String part: parts) {
			String[] data = part.split("=");
			MetaData metaData = MetaData.getMetaData(data[0]);
			if (metaData == null)
				Log.d("AdRequestMetaData", part + " is not valid meta data for an individual ad.");
			else
				individual.put(metaData, data[1]);
		}
	}
}
