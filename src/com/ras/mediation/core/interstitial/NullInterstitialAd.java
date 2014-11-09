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
 * This class is used to represent an interstitial ad when you really do not want to show an ad.
 * 
 * @author Richard So
 */
public class NullInterstitialAd implements IInterstitialAd {
	private AdRequestMetaData[] m_metaData;
	
	public NullInterstitialAd(AdRequestMetaData[] metaData) {
		m_metaData = metaData;
	}

	@Override
	public void requestAd(Activity activity) {
	}

	@Override
	public boolean displayAd() {
		return false;
	}

	@Override
	public boolean isAdReady() {
		return false;
	}

	@Override
	public void cancelRequest() {
	}

	@Override
	public void setListener(IInterstitialListener listener) {
	}

	@Override
	public AdRequestMetaData[] getRequestMetaData() {
		return m_metaData;
	}
}
