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
 * This is equal distribution between all your different Ad Providers
 * 
 * Example:
 * 
 * Providers A, B, C
 * 
 * The first time the provider is selected it will return A
 * The second time the provider is selected whether a new chain or a backup request provider B is selected
 * The third time the provider is selected whether a new chain or a backup request provider C is selected
 * 
 * @author Richard So
 */
public class RoundRobinProviderSelectStrategy implements IProviderSelectStrategy {
	private String[] m_providers;
	private int m_lastIndex = 0;

	public RoundRobinProviderSelectStrategy(Set<String> availableProviders, String providerValue) {
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
		Log.d("RoundRobinProviderSelectStrategy", msg);
	}

	@Override
	public String selectProvider(boolean isBackupRequest) {
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
