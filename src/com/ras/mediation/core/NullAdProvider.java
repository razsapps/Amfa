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
import com.ras.mediation.core.interstitial.NullInterstitialAd;

/**
 * An ad provider that will do nothing other than prevent NullPointerExceptions. Use this
 * if you want to mimic having an ad provider, but you really do not have one.
 * 
 * @author Richard So
 */
public class NullAdProvider implements IAdProvider {
	@Override
	public String getProviderName() {
		return "none";
	}

	@Override
	public IInterstitialAd createInterstitialAd(AdRequestMetaData... metaDatas) {
		return new NullInterstitialAd(metaDatas);
	}

	@Override
	public String getIdentifierClass() {
		return NullAdProvider.class.getName();
	}
}
