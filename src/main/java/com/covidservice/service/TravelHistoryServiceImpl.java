package com.covidservice.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.covidservice.exceptionHandling.InvalidDateException;
import com.covidservice.exceptionHandling.TravelHistoryException;
import com.covidservice.models.TravelHistory;
import com.covidservice.repository.TravelHistoryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TravelHistoryServiceImpl implements TravelHistoryService{
	
	public static final String DATE_FORMATTER = "dd/MM/yyyy hh:mm:ss";
	public static final String TRAVEL_HISTORY_JSON_URL = "https://api.covid19india.org/travel_history.json";
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
    private TravelHistoryRepository travelHistoryRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Override
	public void syncTravelHistory() throws TravelHistoryException {
		try {
			String travelHistoryData = getTravelHistoryJson();
			 List<TravelHistory> travelHistoryList = formatTravelHistoryJson(travelHistoryData);
			 LOG.info("Size of data to save {}",travelHistoryList.size());
			 /*Assumption: To always refresh in database from travel history json.*/ 
			 travelHistoryRepository.deleteAll();
	         travelHistoryRepository.saveAll(travelHistoryList);
		} catch (TravelHistoryException e) {
			LOG.error(e.getMessage());
			throw e;
		}catch(Exception e) {
			LOG.error(e.getMessage());
			throw new TravelHistoryException("Error occured while processing travel history data.");
		}
	}
	
	@Override
	public List<TravelHistory> getAllTravelHistory(Date timefrom,Date timeto) throws TravelHistoryException {
		List<TravelHistory> travelHistoryList =  new ArrayList<>();
		try {
			validateDates(timefrom,timeto);
			Query query = new Query();		
			query.addCriteria(Criteria.where("timefrom").gt(timefrom));
			query.addCriteria(Criteria.where("timeto").lt(timeto));
			travelHistoryList = mongoTemplate.find(query,TravelHistory.class);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			throw new TravelHistoryException("Error occurred while fetching required data.");
		}
		return travelHistoryList;
	}

	private void validateDates(Date timefrom, Date timeto) throws InvalidDateException {
		if(timefrom == null || timeto == null) {
			throw new InvalidDateException("Invalid value of timefrom or timeto dates");
		}
		
	}

	private String getTravelHistoryJson() throws TravelHistoryException {
		String travelHistoryData = null;
		try {
			HttpHeaders headers=new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<String> entity=new HttpEntity<String>(headers);
			travelHistoryData =  restTemplate.exchange(TRAVEL_HISTORY_JSON_URL,HttpMethod.GET,entity,String.class).getBody();
		} catch (RestClientException e) {
			LOG.error(e.getMessage());
			throw new TravelHistoryException("Unable to get travel history json");
		}
		return travelHistoryData;
	}
	
	private List<TravelHistory> formatTravelHistoryJson(String travelHistoryData) throws TravelHistoryException{
		List<TravelHistory> travelHistoryList =null;
		try {
			JSONObject jsonObj = new JSONObject(travelHistoryData);
			JSONArray travelHistoryJson = jsonObj.getJSONArray("travel_history");
			LOG.info("Json array size {}",travelHistoryJson.length());
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			DateFormat df = new SimpleDateFormat(DATE_FORMATTER);
			objectMapper.setDateFormat(df);
			travelHistoryList = new ArrayList<TravelHistory>();
			int size = travelHistoryJson.length();
			JSONObject travelHistoryJsonObj;
			TravelHistory travelHistory = new TravelHistory();
			for(int i=0;i<size;i++) {
				 travelHistoryJsonObj = travelHistoryJson.getJSONObject(i);
				 try {
					travelHistory = objectMapper.readValue(travelHistoryJsonObj.toString(),TravelHistory.class) ;
					travelHistoryList.add(travelHistory);
				} catch (JsonProcessingException e) {
					LOG.error("Invalid json format for data {}",travelHistoryJsonObj);
				} 
			}
		} catch (JSONException e) {
			LOG.error(e.getMessage());
			throw new TravelHistoryException("Unable to process travel history json data");
		}
		return travelHistoryList;
	}

	@Override
	public List<TravelHistory> getAllAddress() throws TravelHistoryException {
		List<TravelHistory> travelHistoryList =new ArrayList<>();
		try {
			Query query = new Query();
			query.fields().include("address");
			travelHistoryList =  mongoTemplate.find(query,TravelHistory.class);
		} catch (Exception e) {
			throw new TravelHistoryException("Unable to get all address.");
		}
		return travelHistoryList;
	}
	
}
