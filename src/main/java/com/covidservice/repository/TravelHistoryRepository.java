package com.covidservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.covidservice.models.TravelHistory;

@Repository
public interface TravelHistoryRepository extends MongoRepository<TravelHistory, String>{
}
