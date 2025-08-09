package com.p.circuitbreaker.service;

import com.p.circuitbreaker.model.TravelDestination;

public interface TravelDestinationService {

	TravelDestination getDestinationDetails(String destinationName,String country);
	
	String getAttractions(String destinationName,String country);
}
