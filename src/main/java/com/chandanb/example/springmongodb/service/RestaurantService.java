package com.chandanb.example.springmongodb.service;

import com.chandanb.example.springmongodb.model.Restaurant;
import com.chandanb.example.springmongodb.repository.RestaurantRepository;
import com.chandanb.example.springmongodb.utils.ListUtils;
import com.google.common.collect.Lists;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.*;

@Service

public class RestaurantService {
    final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

    @Inject
    private RestaurantRepository restaurantRepository;
    @Inject
    private MongoProperties mongoProperties;
    @Inject
    private Mongo mongo;
    @Inject
    private ResourceLoader resourceLoader;

    public List<Restaurant> findAll() {
        return Lists.newArrayList(restaurantRepository.findAll());
    }

    public List<Restaurant> findRestaurantsWithNameContaining(String query){
        TextCriteria textCriteria = new TextCriteria().matchingAny(query);
        ArrayList<Restaurant> restaurants = Lists.newArrayList(restaurantRepository.
                findAllByOrderByScoreDesc(textCriteria));
        String[] words = query.split(" ");
        List<Restaurant> finalList  = new ArrayList<>(restaurants);
        for(String w: words){
            List<Restaurant> restaurantsMatchingASingleWord = restaurantRepository.findAllByOrderByScoreDesc(new TextCriteria().matching(w));
            finalList = ListUtils.intersection(finalList, restaurantsMatchingASingleWord);
        }
        Collections.sort(finalList);
        return finalList;
    }

    public Restaurant findByName(String name) {
        return restaurantRepository.findOneByName(name);
    }

    public void deleteAll() {
        restaurantRepository.deleteAll();
    }

    public void restoreDefaultRecords() throws URISyntaxException, IOException {
        Resource resource = new ClassPathResource("restaurants.json");
        String line;
        DB db = mongo.getDB(mongoProperties.getDatabase());
        DBCollection restaurant = db.getCollection("restaurants");
        logger.info("Loading Default Restaurants Data");
        try(BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))){
            while ((line = br.readLine()) != null) {
                DBObject dbo = (DBObject) com.mongodb.util.JSON.parse(line);
                restaurant.insert(dbo);
            }
        }

    }

    public void save(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    public Restaurant findById(String uniqueId) {
        return restaurantRepository.findOne(uniqueId);
    }
}

