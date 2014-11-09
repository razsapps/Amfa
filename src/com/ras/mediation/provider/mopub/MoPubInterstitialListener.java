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

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubInterstitial.InterstitialAdListener;
import com.ras.mediation.core.interstitial.IInterstitialListener;

public class MoPubInterstitialListener implements InterstitialAdListener  {
	private IInterstitialListener m_listener;
	
	public MoPubInterstitialListener(IInterstitialListener listener) {
		if (listener == null)
			throw new IllegalArgumentException("MoPubInterstitialListener - IInterstitialListener cannot be null");
		m_listener = listener;
	}

	@Override
	public void onInterstitialClicked(MoPubInterstitial ad) {
		m_listener.handleClick();
	}

	@Override
	public void onInterstitialDismissed(MoPubInterstitial ad) {
		m_listener.handleClosed();
	}

	@Override
	public void onInterstitialFailed(MoPubInterstitial ad, MoPubErrorCode ec) {
		m_listener.handleFailed(new Exception(ec.toString()));
	}

	@Override
	public void onInterstitialLoaded(MoPubInterstitial ad) {
		m_listener.handleReceived();
		
	}

	@Override
	public void onInterstitialShown(MoPubInterstitial ad) {
		m_listener.handleShow();
	}
}
