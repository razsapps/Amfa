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

import com.ras.mediation.core.MediationHandler;
import com.ras.mediation.core.AdRequestMetaData;

import android.app.Activity;

/**
 * This interstitial ad can request multiple ads in the case of an ad failure.
 * 
 * @author Richard So
 */
public class MultiRequestInterstitialAd implements IInterstitialAd {
	private IInterstitialAd m_ad;
	private MultiRequestInterstitialListener m_listener = new MultiRequestInterstitialListener();
	private Activity m_activity;
	private int m_maxRequests;
	private int m_currentRequests = 0;

	public MultiRequestInterstitialAd(IInterstitialAd ad, int maxRequests) {
		if (ad == null)
			throw new IllegalArgumentException("The starting ad cannot be null");
		else if (maxRequests <= 0)
			throw new IllegalArgumentException("The maxRequests must be an integer greater than 0");

		m_ad = ad;
		m_ad.setListener(m_listener);
		m_maxRequests = maxRequests;
	}

	@Override
	public void requestAd(Activity activity) {
		m_activity = activity;
		m_currentRequests++;
		m_ad.requestAd(activity);
	}

	@Override
	public boolean displayAd() {
		return m_ad.displayAd();
	}

	@Override
	public boolean isAdReady() {
		return m_ad.isAdReady();
	}

	@Override
	public void cancelRequest() {
		m_ad.cancelRequest();
	}

	@Override
	public void setListener(IInterstitialListener listener) {
		m_listener.setListener(listener);
	}

	@Override
	public AdRequestMetaData[] getRequestMetaData() {
		return m_ad.getRequestMetaData();
	}

	/**
	 * Class to wrap the listener and handle requesting backup ads when the original ad fails
	 * 
	 * @author Richard So
	 */
	private class MultiRequestInterstitialListener implements IInterstitialListener {
		private IInterstitialListener m_listener;

		private void setListener(IInterstitialListener listener) {
			m_listener = listener;
		}

		@Override
		public void handleClosed() {
			if (m_listener != null)
				m_listener.handleClosed();
		}

		@Override
		public void handleFailed(Exception e) {
			if (m_listener != null)
				m_listener.handleFailed(e);
			
			//If we still have some more requests left let's ask for a new ad
			if (m_currentRequests < m_maxRequests) {
				m_ad = MediationHandler.getInstance(m_activity).getBackupInterstitialAd(m_ad.getRequestMetaData());
				m_ad.setListener(this);
				requestAd(m_activity);
			}
		}

		@Override
		public void handleReceived() {
			if (m_listener != null)
				m_listener.handleReceived();
		}

		@Override
		public void handleShow() {
			if (m_listener != null)
				m_listener.handleShow();
		}

		@Override
		public void handleClick() {
			if (m_listener != null)
				m_listener.handleClick();
		}
	}
}
