package com.covidservice.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.scheduling.annotation.EnableAsync;

import com.covidservice.models.TravelHistory;

@Configuration
@EnableAsync
public class MongoConfig {

	@Autowired
	MongoTemplate mongoTemplate;
	
	@PostConstruct
	public void mondoIndex() {
		setMongoIndex();
	}
	
	private void setMongoIndex() {
		mongoTemplate.indexOps(TravelHistory.class).ensureIndex(new Index().on("_cn6ca",Direction.ASC).unique());
		mongoTemplate.indexOps(TravelHistory.class).ensureIndex(new Index().on("timefrom",Direction.ASC).on("timeto", Direction.ASC));
	}
}
