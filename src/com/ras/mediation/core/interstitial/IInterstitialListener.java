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

/**
 * This interface serves as a wrapper for interstitial listeners from different providers.
 * Not all features are available across all ad providers
 * 
 * @author Richard So
 */
public interface IInterstitialListener {
	/**
	 * The interstitial ad has been closed
	 */
	public void handleClosed();
	
	/**
	 * There was an issue requesting your ad and you did not receive one
	 * @param e And exception from the ad provider or wrapping the error message from the ad provider
	 */
	public void handleFailed(Exception e);
	
	/**
	 * The ad has finished being received and should be ready for display
	 */
	public void handleReceived();
	
	/**
	 * The ad has been shown
	 */
	public void handleShow();
	
	/**
	 * The ad has been clicked
	 */
	public void handleClick();
}
