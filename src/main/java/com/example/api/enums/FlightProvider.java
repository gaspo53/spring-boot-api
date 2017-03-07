/**
 * 
 * @author Gaspar Rajoy <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 */
package com.example.api.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * Enum to identify a provider.
 * 
 * @author gaspar
 *
 */
public enum FlightProvider {

	SKYSCANNER,
	GOOGLE_QPX,
	WEGO,
	SKY_PICKER,
	TRAVELPORT,
	DESPEGAR,
	DANIELLE;
	
	public static FlightProvider from(ApiComponent apiComponent){
		String componentValue = apiComponent.toString();
		FlightProvider flightProvider = FlightProvider.valueOf(componentValue);
		
		return flightProvider;
	}
	
	public static final String availableValues(){
		String availableValues = StringUtils.join(FlightProvider.values(), ",");
		
		return availableValues;
	}
}
