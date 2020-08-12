package com.covidservice.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.covidservice.exception.TravelHistoryException;
import com.covidservice.models.TravelHistory;
import com.covidservice.service.TravelHistoryService;

@Validated
@RestController
@RequestMapping(value = "/travelhistory")
public class TravelHistoryController {
	
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TravelHistoryService travelHistoryService;

	@RequestMapping(value = "/people", method = RequestMethod.GET)
	public List<TravelHistory> getAllTravelHistory(@RequestParam(name = "timefrom",required = false) @Valid @DateTimeFormat(pattern = "dd/MM/yy") Date timefrom,
			@RequestParam(name = "timeto",required = false) @Valid @DateTimeFormat(pattern = "dd/MM/yy") Date timeto) throws TravelHistoryException {
		LOG.info("Getting all people. on date basis");
		return travelHistoryService.getAllTravelHistory(timefrom,timeto);
	}

	@RequestMapping(value = "/sync", method = RequestMethod.POST)
	public  ResponseEntity<String> syncTravelHistory() throws TravelHistoryException {
		LOG.info("Saving travel history details.");
		travelHistoryService.syncTravelHistory();
		return new ResponseEntity<String>("Travel history data successfuly synched", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/address", method = RequestMethod.GET)
	public List<TravelHistory> getAllPeopleAddress() throws TravelHistoryException {
		LOG.info("Getting all people address.");
		return travelHistoryService.getAllAddress();
	}
}
