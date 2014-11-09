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

/**
 * An exception that is thrown when an ad request is made from an ad provider but not enough meta data
 * was provided to actually handle the ad request. This is generally the case when the developer forgets
 * to provide specific data needed by the specific ad provider such as the ad provider's ad id.
 * 
 * @author Richard So
 */
public class MetaDataException extends IllegalArgumentException {
	private static final long serialVersionUID = 1L;

	/**
	 * @param className The class name generating this exception
	 * @param dataType The type of data that was missing
	 */
	public MetaDataException(String className, String dataType) {
		super(className + " failed to create because there was no " + dataType + " available. Please specify on in the AdRequestMetaData or your AndroidManifest.xml");
	}
}
