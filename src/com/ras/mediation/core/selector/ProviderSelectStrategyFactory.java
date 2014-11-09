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

import java.util.Set;

/**
 * This class is a factory for IAdProviderSelectStrategy depending on the strategy name passed in
 * 
 * @author Richard So
 */
public class ProviderSelectStrategyFactory {
	private static final String STRATEGY_ROUND_ROBIN = "round_robin";
	private static final String STRATEGY_PRIORITY = "priority";
	private static final String STRATEGY_WEIGHTED = "weighted";
	
	/**
	 * @param strategy The text representation of the strategy to create (round_robin, priority, weighted)
	 * @param availableProviders The ad providers that are available to be used by this application
	 * @param providerValue String metadata that represents which ad providers to use as well as any information needed to create the strategy
	 * @return The IProviderSelectStrategy associated with the information passed in
	 */
	public static IProviderSelectStrategy create(String strategy, Set<String> availableProviders, String providerValue) {
		if (STRATEGY_ROUND_ROBIN.equals(strategy))
			return new RoundRobinProviderSelectStrategy(availableProviders, providerValue);
		else if (STRATEGY_PRIORITY.equals(strategy))
			return new PriorityProviderSelectStrategy(availableProviders, providerValue);
		else if (STRATEGY_WEIGHTED.equals(strategy))
			return new WeightedProviderSelectStrategy(availableProviders, providerValue);
		else
			//Default
			return new RoundRobinProviderSelectStrategy(availableProviders, providerValue);
	}
}
