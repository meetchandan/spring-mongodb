package com.chandanb.example.springmongodb.service;

import com.chandanb.example.springmongodb.model.Restaurant;
import com.chandanb.example.springmongodb.repository.RestaurantRepository;
import com.google.common.collect.Lists;
import com.mongodb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class RestaurantService {
    final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

    @Inject
    private RestaurantRepository restaurantRepository;
    @Inject
    private MongoProperties mongoProperties;
    @Inject
    private Mongo mongo;

    public List<Restaurant> findAll() {
        return Lists.newArrayList(restaurantRepository.findAll());
    }

    public List<Restaurant> findRestaurantsWithNameContaining(String name){
        TextCriteria textCriteria = new TextCriteria().matchingAny(name);
        return Lists.newArrayList(restaurantRepository.findAllBy(textCriteria));
    }

    public Restaurant findByName(String name) {
        return restaurantRepository.findOneByName(name);
    }

    public void deleteAll() {
        restaurantRepository.deleteAll();
    }

    public void restoreDefaultRecords() throws URISyntaxException, IOException {
        logger.info("Restore");
        Path path = Paths.get(ClassLoader.getSystemResource("restaurants.json").toURI());
        List<String> records = Files.readAllLines(path);
        DB db = mongo.getDB(mongoProperties.getDatabase());
        DBCollection restaurant = db.getCollection("restaurant");
        for (String record : records) {
            DBObject dbo = (DBObject) com.mongodb.util.JSON.parse(record);
            restaurant.insert(dbo);
        }
    }

    public void save(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    public Restaurant findById(String uniqueId) {
        return restaurantRepository.findOne(uniqueId);
    }
}

