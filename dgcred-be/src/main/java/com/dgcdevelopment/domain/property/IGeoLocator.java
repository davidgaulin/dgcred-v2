package com.dgcdevelopment.domain.property;

import com.dgcdevelopment.domain.Address;

public interface IGeoLocator {
	
	public double[] getLongLat(Address address);

}
