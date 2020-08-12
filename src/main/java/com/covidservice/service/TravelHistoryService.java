package com.covidservice.service;

import java.util.Date;
import java.util.List;

import com.covidservice.exceptionHandling.TravelHistoryException;
import com.covidservice.models.TravelHistory;

public interface TravelHistoryService {
	
	public void syncTravelHistory() throws TravelHistoryException;

	public List<TravelHistory> getAllTravelHistory(Date timefrom,Date timeto) throws TravelHistoryException;

	public List<TravelHistory> getAllAddress() throws TravelHistoryException;

}
