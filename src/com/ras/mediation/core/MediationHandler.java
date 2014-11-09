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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;

import android.content.Context;
import android.util.Log;

import com.ras.mediation.core.interstitial.IInterstitialAd;
import com.ras.mediation.core.interstitial.MultiRequestInterstitialAd;
import com.ras.mediation.core.interstitial.NullInterstitialAd;
import com.ras.mediation.core.selector.IProviderSelectStrategy;
import com.ras.mediation.core.selector.ProviderSelectStrategyFactory;

/**
 * This class is the main engine that loads and powers the framework
 * 
 * @author Richard So
 */
public class MediationHandler {
	private static final String[] PROVIDER_CLASSES = new String[] {
		"com.ras.mediation.provider.admobsdk.AdMobAdProvider",
		"com.ras.mediation.provider.millennialmedia.MillennialMediaAdProvider",
		"com.ras.mediation.provider.mopub.MoPubAdProvider"
	};

	private static MediationHandler s_instance = null;
	private static HashMap<String, IAdProvider> s_providers = new HashMap<String, IAdProvider>();
	private IProviderSelectStrategy m_selectStrategy;
	private AdRequestMetaData m_metaData = new AdRequestMetaData();
	private int m_maxAdRequests;
	private int m_showAdPercent;
	private Random m_random = new Random();
	private HashMap<String, AdRequestMetaData> m_individualAdMetaData = new HashMap<String, AdRequestMetaData>();

	private MediationHandler(Context context) {
		loadProviders();
		AdRequestMetaData.readMetaData(context, m_metaData, m_individualAdMetaData);
		initialize();
		new DynamicLoaderThread().start();
	}

	private void initialize() {
		//LOAD STRATEGY
		String strategy = m_metaData.get(MetaData.PROVIDER_STRATEGY);
		String provider_value = m_metaData.get(MetaData.PROVIDER_VALUE);
		m_selectStrategy = ProviderSelectStrategyFactory.create(strategy, s_providers.keySet(), provider_value);

		//LOAD MAX REQUESTS
		//1 is default which means do not reattempt to request a value
		m_maxAdRequests = 1;

		try {
			m_maxAdRequests = Integer.valueOf(m_metaData.get(MetaData.MAX_AD_REQUESTS));
		}
		catch (NumberFormatException e) {
			Log.d("MediationHandler", "max_ad_requests was not provided or did not contain a number. Using default value.", e);
		}

		//LOAD SHOW AD PERCENT
		//100 is default which means always attempt to show an ad
		m_showAdPercent = 100;

		try {
			m_showAdPercent = Integer.valueOf(m_metaData.get(MetaData.SHOW_AD_PERCENT));
		}
		catch (NumberFormatException e) {
			Log.d("MediationHandler", "show_ad_percent was not provided or did not contain a number. Using default value.", e);
		}
	}

	public static MediationHandler getInstance(Context context) {
		if (s_instance == null) {
			synchronized (MediationHandler.class) {
				if (s_instance == null)
					s_instance = new MediationHandler(context);
			}
		}
		return s_instance;
	}

	private void loadProviders() {
		for (String providerClass: PROVIDER_CLASSES) {
			try {
				//Load this class through reflection in case the developer packages only the providers he needs
				IAdProvider provider = (IAdProvider) Class.forName(providerClass).newInstance();
				String idClass = provider.getIdentifierClass();
				//If this class does not exist a ClassNotFoundException will be thrown and we'll continue
				Class.forName(idClass);
				s_providers.put(provider.getProviderName(), provider);
			}
			catch (ClassNotFoundException e) {
			} 
			catch (InstantiationException e) {
			} 
			catch (IllegalAccessException e) {
			}
		}
	}

	private IAdProvider getAdProvider(boolean isBackupRequest) {
		//If this is not a backup request and the developer has set show ad percent to be less than 100
		//Calculate if an add needs to be shown or not
		if (!isBackupRequest && m_showAdPercent < 100) {
			//Ad is not shown return NullAdProvider
			if (m_random.nextInt(100) < m_showAdPercent)
				return new NullAdProvider();
		}

		return s_providers.get(m_selectStrategy.selectProvider(isBackupRequest));
	}

	/**
	 * @param adId The specific mediation ad id (defined in the AndroidManifest's meta data) that identifies your specific ad 
	 * @return An interstitial ad for the ad id provided by the next selected ad network
	 */
	public IInterstitialAd getInterstitialAd(String adId) {
		AdRequestMetaData metaData = m_individualAdMetaData.get(adId);
		if (metaData != null)
			return getInterstitialAd(metaData);
		else {
			try {
				return getInterstitialAd((AdRequestMetaData)null);
			}
			catch (MetaDataException e) {
				Log.e("MediationHandler", "No data provided for ad id " + adId, e);
			}
			return new NullInterstitialAd(new AdRequestMetaData[]{m_metaData});
		}
	}

	/**
	 * @param metaData Specific meta data related to this ad request or null if there is none provided
	 * @return An interstitial ad for the meta data provided by the next selected ad network
	 */
	public IInterstitialAd getInterstitialAd(AdRequestMetaData metaData) {
		IInterstitialAd ad = getAdProvider(false).createInterstitialAd(metaData, m_metaData);

		//Wrap the ad so it'll query more on failure
		if (m_maxAdRequests > 1)
			ad = new MultiRequestInterstitialAd(ad, m_maxAdRequests);

		return ad;
	}

	/**
	 * This method should only be used if it is attempting to request another ad using the same exact data of
	 * an ad request that failed
	 * @param metaDatas a list of request meta data in order of priority for data
	 * @return an interstitial ad from an available ad provider
	 */
	public IInterstitialAd getBackupInterstitialAd(AdRequestMetaData... metaDatas) {
		return getAdProvider(true).createInterstitialAd(metaDatas);
	}

	/**
	 * This class will hit the url provided by dynamic_load_url in the meta data and load meta data to overwrite
	 * the current meta data.  This will allow meta data to dynamically be set for an application
	 */
	private class DynamicLoaderThread extends Thread {
		@Override
		public void run() {
			String address = m_metaData.get(MetaData.DYNAMIC_LOAD_URL);
			
			//No dynamic load url just exit
			if (address == null || address.trim().length() == 0)
				return;

			InputStreamReader in = null;
			BufferedReader br = null;
			try {
				URL url = new URL(address);
				in = new InputStreamReader(url.openStream());
				br = new BufferedReader(in);
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();
				while (line != null) {
					sb.append(line).append("\n");
					line = br.readLine();
				}
				
				AdRequestMetaData.loadMetaData(sb.toString(), m_metaData, m_individualAdMetaData);
				
				//Reinitialize global variables with new variables read in
				initialize();
			} 
			catch (IOException e) {
				Log.e("MediationHandler", "Failed to load dynamic data", e);
			}
			finally {
				try {
					if (br != null)
						br.close();
					if (in != null)
						in.close();
				} 
				catch (IOException e) {
					Log.d("MediationHandler", "Failed to close streams", e);
				}
			}
		}
	}
}
