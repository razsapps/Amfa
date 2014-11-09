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
package com.ras.mediation.core.interstitial;

import android.app.Activity;

import com.ras.mediation.core.AdRequestMetaData;

/**
 * This interface serves as a wrapper for interstitial ads from different providers
 * 
 * @author Richard So
 */
public interface IInterstitialAd {
	/**
	 * This will send an ad request to the ad provider to start caching the ad
	 * @param activity The visible activity you are requesting an ad for
	 */
	public void requestAd(Activity activity);
	
	/**
	 * Displays the ad that was received from the ad provider if there was an ad
	 * @return true if the ad was displayed or false otherwise
	 */
	public boolean displayAd();
	
	/**
	 * @return true if an ad has been received from the ad provider and is ready or false otherwise
	 */
	public boolean isAdReady();
	
	/**
	 * Cancel your request for an ad from the ad provider since you won't be showing it
	 */
	public void cancelRequest();
	
	/**
	 * @param listener the listener used as a callback for this ad request
	 */
	public void setListener(IInterstitialListener listener);
	
	/**
	 * @return the metadata used on this specific ad request
	 */
	public AdRequestMetaData[] getRequestMetaData();
}
