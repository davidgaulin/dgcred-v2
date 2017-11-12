package com.dgcdevelopment.domain.property;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dgcdevelopment.domain.Address;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

@Component
public class GoogleGeoLocator implements IGeoLocator {

	private final Logger log = LoggerFactory.getLogger(this.getClass()); 
	
	@Override
	public double[] getLongLat(Address address) {
		double[] longLat = new double[2];
		longLat[0] = 0;
		longLat[1] = 0;
		try {
			GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCKQ4AdrfylnvDVL4U-_nDcI04YejtzbTI");
			GeocodingResult[] results =  GeocodingApi.geocode(context,
			    address.getAddress1() + " " + address.getAddress2() + "," + address.getCity() + "," + address.getProvinceState() + "," + address.getCountry()).await();
			if (results[0] != null && results[0].geometry != null && results[0].geometry.location != null) {
				longLat[0] = results[0].geometry.location.lng;
				longLat[1] = results[0].geometry.location.lat;
			}
		} catch (Exception e) {
			log.error("Something is wrong with the Googe API:"
					+ e.getMessage());
		}
		return longLat;
	}
	
}

