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

import com.ras.mediation.core.AdRequestMetaData;
import com.ras.mediation.core.IAdProvider;
import com.ras.mediation.core.interstitial.IInterstitialAd;

public class MoPubAdProvider implements IAdProvider {
	@Override
	public String getProviderName() {
		return "mopub";
	}
	
	@Override
	public IInterstitialAd createInterstitialAd(AdRequestMetaData... metaDatas) {
		return new MoPubInterstitialAd(metaDatas);
	}

	@Override
	public String getIdentifierClass() {
		return "com.mopub.mobileads.MoPubActivity";
	}
}
