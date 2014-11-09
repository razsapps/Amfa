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
package com.ras.mediation.provider.mopub;

import android.app.Activity;

import com.mopub.mobileads.MoPubInterstitial;
import com.ras.mediation.core.AdRequestMetaData;
import com.ras.mediation.core.MetaData;
import com.ras.mediation.core.MetaDataException;
import com.ras.mediation.core.interstitial.IInterstitialAd;
import com.ras.mediation.core.interstitial.IInterstitialListener;

public class MoPubInterstitialAd implements IInterstitialAd {
	private MoPubInterstitial m_ad;
	private String m_adUnitId;
	private IInterstitialListener m_listener;
	private AdRequestMetaData[] m_originalRequestData;
	
	public MoPubInterstitialAd(AdRequestMetaData... metaDatas) {
		m_originalRequestData = metaDatas;
		m_adUnitId = AdRequestMetaData.coalesceData(MetaData.MOPUB_AD_UNIT_ID, metaDatas);
		
		if (m_adUnitId == null)
			throw new MetaDataException("MoPubInterstitialAd", "ad unit id");
	}
	
	@Override
	public void requestAd(Activity activity) {
		m_ad = new MoPubInterstitial(activity, m_adUnitId);
		if (m_listener != null)
			m_ad.setInterstitialAdListener(new MoPubInterstitialListener(m_listener));
		m_ad.load();
	}

	@Override
	public boolean displayAd() {
		if (m_ad == null)
			return false;
		
		return m_ad.show();
	}

	@Override
	public boolean isAdReady() {
		if (m_ad == null)
			return false;
		
		return m_ad.isReady();
	}

	@Override
	public void cancelRequest() {
		if (m_ad != null)
			m_ad.destroy();
	}

	@Override
	public void setListener(IInterstitialListener listener) {
		m_listener = listener;
		
		if (m_ad != null)
			m_ad.setInterstitialAdListener(new MoPubInterstitialListener(listener));
	}

	@Override
	public AdRequestMetaData[] getRequestMetaData() {
		return m_originalRequestData;
	}
}
