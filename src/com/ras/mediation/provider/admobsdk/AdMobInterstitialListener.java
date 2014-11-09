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
package com.ras.mediation.provider.admobsdk;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest.ErrorCode;
import com.ras.mediation.core.interstitial.IInterstitialListener;

public class AdMobInterstitialListener implements AdListener {
	private IInterstitialListener m_listener;
	
	public AdMobInterstitialListener(IInterstitialListener listener) {
		if (listener == null)
			throw new IllegalArgumentException("AdMobInterstitialListener - IInterstitialListener cannot be null");
		m_listener = listener;
	}
	
	@Override
	public void onDismissScreen(Ad ad) {
		m_listener.handleClosed();
	}

	@Override
	public void onFailedToReceiveAd(Ad ad, ErrorCode ec) {
		m_listener.handleFailed(new Exception(ec.toString()));
	}

	@Override
	public void onLeaveApplication(Ad ad) {
		//We won't handle this
	}

	@Override
	public void onPresentScreen(Ad ad) {
		m_listener.handleShow();
	}

	@Override
	public void onReceiveAd(Ad ad) {
		m_listener.handleReceived();
	}
}
