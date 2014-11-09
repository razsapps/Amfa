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
package com.ras.mediation.provider.millennialmedia;

import android.app.Activity;

import com.millennialmedia.android.MMInterstitial;
import com.millennialmedia.android.MMRequest;
import com.ras.mediation.core.AdRequestMetaData;
import com.ras.mediation.core.MetaData;
import com.ras.mediation.core.MetaDataException;
import com.ras.mediation.core.interstitial.IInterstitialAd;
import com.ras.mediation.core.interstitial.IInterstitialListener;

public class MillennialMediaInterstitialAd implements IInterstitialAd {
	private MMInterstitial m_ad;
	private String m_apid;
	private IInterstitialListener m_listener;
	private AdRequestMetaData[] m_originalRequestData;
	
	public MillennialMediaInterstitialAd(AdRequestMetaData... metaDatas) {
		m_originalRequestData = metaDatas;
		m_apid = AdRequestMetaData.coalesceData(MetaData.MM_APID, metaDatas);
		
		if (m_apid == null)
			throw new MetaDataException("MillennialMediaInterstitialAd", "apid");
	}
	
	@Override
	public void requestAd(Activity activity) {
		m_ad = new MMInterstitial(activity);
		if (m_listener != null)
			m_ad.setListener(new MillennialMediaInterstitialListener(m_listener));
		MMRequest request = new MMRequest();
		m_ad.setMMRequest(request);
		m_ad.setApid(m_apid);
		m_ad.fetch();
	}

	@Override
	public boolean displayAd() {
		if (m_ad == null)
			return false;
		
		return m_ad.display();
	}

	@Override
	public boolean isAdReady() {
		if (m_ad == null)
			return false;
		
		return m_ad.isAdAvailable();
	}

	@Override
	public void cancelRequest() {
	}

	@Override
	public void setListener(IInterstitialListener listener) {
		m_listener = listener;
		
		if (m_ad != null)
			m_ad.setListener(new MillennialMediaInterstitialListener(listener));
	}

	@Override
	public AdRequestMetaData[] getRequestMetaData() {
		return m_originalRequestData;
	}
}
