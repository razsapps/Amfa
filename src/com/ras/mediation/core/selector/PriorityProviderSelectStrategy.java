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
package com.ras.mediation.core.selector;

import java.util.ArrayList;
import java.util.Set;

import android.util.Log;

/**
 * This provider select strategy will take the most important provider first and work its way down the chain if the ad requests fail.
 * 
 * Example:
 * Providers: A, B, C
 * 
 * On first ad request Provider A is returned
 * If Provider A fails and the user attempts a backup request Provider B is returned
 * On the second ad request (not in the same chain) Provider A is returned
 * 
 * @author Richard So
 */
public class PriorityProviderSelectStrategy implements IProviderSelectStrategy {
	private String[] m_providers;
	private int m_lastIndex = 0;

	public PriorityProviderSelectStrategy(Set<String> availableProviders, String providerValue) {
		if (providerValue != null) {
			ArrayList<String> providers = new ArrayList<String>();
			String[] providerDetails = providerValue.split(",");
			for (String providerDetail: providerDetails) {
				if (!availableProviders.contains(providerDetail)) {
					log(providerDetail + " is not a registered provider.");
					continue;
				}
				providers.add(providerDetail);
			}
			
			if (!providers.isEmpty()) {
				m_providers = providers.toArray(new String[providers.size()]);
			}
			else
				log("The provider meta data was invalid and we could not load any data. Resolving to defaults.");
		}
		
		if (m_providers == null)
			m_providers = availableProviders.toArray(new String[availableProviders.size()]);
	}

	private void log(String msg) {
		Log.d("PriorityProviderSelectStrategy", msg);
	}

	@Override
	public String selectProvider(boolean isBackupRequest) {
		//If first request in the change reset back to the original index
		if (!isBackupRequest)
			m_lastIndex = 0;
		
		String provider = null;
		synchronized(this) {
			provider = m_providers[m_lastIndex];
			m_lastIndex++;
			if (m_lastIndex == m_providers.length)
				m_lastIndex = 0;
		}
		return provider;
	}
}
