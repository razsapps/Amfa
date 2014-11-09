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

import com.ras.mediation.core.interstitial.IInterstitialAd;

/**
 * This is a wrapper for an Ad Provider that will return proprietary ad units wrapped in the mediation framework
 * 
 * @author Richard So
 */
public interface IAdProvider {
	/**
	 * @return The name of the ad provider
	 */
	public String getProviderName();
	
	/**
	 * @param metaDatas specific request meta data
	 * @return An interstitial ad from the provider wrapped in the mediation framework
	 */
	public IInterstitialAd createInterstitialAd(AdRequestMetaData... metaDatas);
	
	/**
	 * @return A full class path string representing a class that means this provider should be used because it is included in the project
	 */
	public String getIdentifierClass();
}
