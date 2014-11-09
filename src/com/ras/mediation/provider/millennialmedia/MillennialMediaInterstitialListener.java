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

import com.millennialmedia.android.MMAd;
import com.millennialmedia.android.MMException;
import com.millennialmedia.android.RequestListener;
import com.ras.mediation.core.interstitial.IInterstitialListener;

public class MillennialMediaInterstitialListener implements RequestListener {
	private IInterstitialListener m_listener;
	
	public MillennialMediaInterstitialListener(IInterstitialListener listener) {
		if (listener == null)
			throw new IllegalArgumentException("MillennialMediaInterstitialListener - IInterstitialListener cannot be null");
		m_listener = listener;
	}

	@Override
	public void MMAdOverlayClosed(MMAd ad) {
		m_listener.handleClosed();
	}

	@Override
	public void MMAdOverlayLaunched(MMAd ad) {
		m_listener.handleShow();
	}

	@Override
	public void MMAdRequestIsCaching(MMAd ad) {
		//We won't handle this
		
	}

	@Override
	public void onSingleTap(MMAd ad) {
		m_listener.handleClick();
	}

	@Override
	public void requestCompleted(MMAd ad) {
		m_listener.handleReceived();
	}

	@Override
	public void requestFailed(MMAd ad, MMException e) {
		m_listener.handleFailed(e);
	}
}
