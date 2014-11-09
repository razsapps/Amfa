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

/**
 * Meta Data constants.  These are the values that will be loaded into your AndroidManifest.xml
 * 
 * @author Richard So
 */
public enum MetaData {
	/**
	 * Specific strategy to select the next ad provider
	 * available options: round_robin, weighted, priority 
	 * Default: round_robin
	 */
	PROVIDER_STRATEGY, 
	/**
	 * This value represents data used to build the provider strategy
	 * It should contain the providers the developer desires to use 
	 * as well as any metadata needed to initialize the provider strategy
	 * Default: All available providers
	 */
	PROVIDER_VALUE, 
	/**
	 * If one ad network fails to provide an ad the mediation framework can
	 * reattempt an ad request up to max_ad_requests times
	 * Default: 1 which means do not reattempt ad requests
	 */
	MAX_AD_REQUESTS,
	/**
	 * The developer can limit how often ads will show in his application.
	 * This value is an integer from 0 - 100 where 0 means show no ads,
	 * 50 means show ads 50% or half the time, and 100 means show ads 
	 * 100% of the time
	 * Default: 100
	 */
	SHOW_AD_PERCENT, 
	/**
	 * This will allow a developer to specify meta data for specific ad requests.
	 * The meta key is a alphanumeric string that is the id and the value is
	 * the meta data in the format <metadata_key>=<value>;<metadata_key>=<value>
	 * Default: There is no default value
	 */
	AD_IDS, 
	/**
	 * A url to a text file that contains meta data to override what is provided
	 * in AndroidManifest.xml thus allowing the developer to dynamically configure
	 * their ad mediation. The text file is delimited per meta data and each line
	 * is in the format <metadata key>=<value>
	 * Default: There is no default value
	 */
	DYNAMIC_LOAD_URL,
	//Admob specific
	/**
	 * This the admob ad_unit_id which you can find from admob directly when setting
	 * up your account
	 */
	ADMOB_AD_UNIT_ID, 
	/**
	 * This is your Millennial Media apid which you can find from Millennial Media directly
	 * when setting up your account
	 */
	MM_APID, 
	/**
	 * This is your MoPub ad_unit_id which you can find from MoPub directly when setting up
	 * your account
	 */
	MOPUB_AD_UNIT_ID;
	
	private static HashMap<String, MetaData> m_metaDataMap = new HashMap<String, MetaData>();
	
	//Create a mapping for String to MetaData lookups
	static {
		for (MetaData metaData: MetaData.values())
			m_metaDataMap.put(metaData.getKey(), metaData);
	}
	
	/**
	 * @return The String representation of this metadata key that is used in the AndroidManifest.xml
	 */
	public String getKey() {
		return toString().toLowerCase();
	}
	
	/**
	 * @param value The string representation of a metadata key used in AndroidManifest.xml
	 * @return The MetaData enum for the string value providedS
	 */
	public static MetaData getMetaData(String value) {
		return m_metaDataMap.get(value);
	}
}
