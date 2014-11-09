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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import android.util.Log;

/**
 * The weighted provider strategy returns a provider based off the percent chain that a provider can be selected.
 * The probability a provider is selected is (Provider Weight) / (Total Weight)
 * 
 * Examples:
 * 
 * Provider A: Weight of 2
 * Provider B: Weight of 5
 * Provider C: Weight of 3
 * 
 * 
 * Given these numbers the total weight is a 10. 
 * This means when requesting an ad provider A has a 20% chance of being selected (2/10), provider B has a 
 * 50% chance of being selected (5/10), and provider C has a 30% chance of being selected (3/10)
 * 
 * @author Richard So
 */
public class WeightedProviderSelectStrategy implements IProviderSelectStrategy {
	private int m_maxWeight;
	private String[] m_providers;
	private int[] m_weight;
	private Random m_random = new Random();
	
	public WeightedProviderSelectStrategy(Set<String> availableProviders, String providerValue) {
		if (providerValue == null)
			initializeViaDefaults(availableProviders);
		else
			intializeViaMetaData(availableProviders, providerValue);
	}
	
	private void log(String msg) {
		Log.d("WeightedProviderSelectStrategy", msg);
	}
	
	private void intializeViaMetaData(Set<String> availableProviders, String providerValue) {
		String[] providerDetails = providerValue.split(",");
		
		HashMap<String, Integer> weightMap = new HashMap<String, Integer>();
		for (String providerDetail: providerDetails) {
			String[] parts = providerDetail.split("=");
			
			if (parts.length != 2) {
				log(providerDetail + " is not a correct provider detail so we are skipping it.");
				continue;
			}
			
			if (!availableProviders.contains(parts[0])) {
				log(parts[0] + " is not a registered provider.");
				continue;
			}
			
			try {
				Integer weight = Integer.valueOf(parts[1]);
				
				if (weight.intValue() <= 0) {
					log(weight.toString() + " is not a valid weight.");
					continue;
				}
				
				weightMap.put(parts[0], weight);
			}
			catch (NumberFormatException e) {
				log(providerDetail + " did not contain a valid integer weight.");
			}
		}
		
		if (weightMap.isEmpty()) {
			log("The provider meta data was invalid and we could not load any data. Resolving to defaults.");
			initializeViaDefaults(availableProviders);
		}
		else {
			//We will add the one later on. Right now this is initialized this way to ensure the range in the array
			//will be a bit more efficient in binary searching the value from random
			m_maxWeight = -1;
			int providerCount = weightMap.size();
			m_providers = new String[providerCount];
			m_weight = new int[providerCount];
			
			int index = 0;
			for (String key: weightMap.keySet()) {
				Integer weight = weightMap.get(key);
				m_maxWeight += weight.intValue();
				m_providers[index] = key;
				m_weight[index] = m_maxWeight;
			}
			m_maxWeight++;
		}
	}
	
	private void initializeViaDefaults(Set<String> availableProviders) {
		log("No providers provided resolving to default distribution");
		m_providers = availableProviders.toArray(new String[availableProviders.size()]);
		m_weight = new int[availableProviders.size()];
		
		//Initialize standard distribution
		for (int i = 0; i < m_weight.length; i++)
			m_weight[i] = i;
		
		m_maxWeight = m_weight.length;
	}
	
	@Override
	public String selectProvider(boolean isBackupRequest) {
		//Random is not always thread safe, but there is no real negative side effect to that in this case
		int provider = m_random.nextInt(m_maxWeight);
		int selected = Arrays.binarySearch(m_weight, provider);
		
		if (selected < 0)
			selected = Math.abs(selected) - 1;
		
		return m_providers[selected];
	}
}
