package com.chandanb.example.springmongodb.service;

import com.chandanb.example.springmongodb.config.Constants;
import com.chandanb.example.springmongodb.model.Restaurant;
import com.chandanb.example.springmongodb.repository.RestaurantRepository;
import com.chandanb.example.springmongodb.utils.HelperUtils;
import com.google.common.collect.Lists;
import com.mongodb.DB;
import com.mongodb.DBCollection;
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
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.chandanb.example.springmongodb.config.Constants.DATA_FILE_NAME;
import static com.chandanb.example.springmongodb.config.Constants.RESTAURANT_COLLECTION_NAME;
import static com.chandanb.example.springmongodb.utils.HelperUtils.intersection;
import static com.chandanb.example.springmongodb.utils.HelperUtils.loadDataFromFileInMongoCollection;

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

    public List<Restaurant> findRestaurantsContaining(String query){
        TextCriteria textCriteria = new TextCriteria().matchingAny(query);
        ArrayList<Restaurant> restaurantsMatchingAnyWord = Lists.newArrayList(restaurantRepository.
                findAllByOrderByScoreDesc(textCriteria));
        String[] words = query.split(" ");
        List<Restaurant> recordsMatchingAllWords  = new ArrayList<>(restaurantsMatchingAnyWord);
        for(String w: words){
            TextCriteria matchingCriteria = new TextCriteria().matching(w);
            List<Restaurant> restaurantsMatchingSpecificWord = restaurantRepository.findAllByOrderByScoreDesc(matchingCriteria);
            recordsMatchingAllWords = intersection(recordsMatchingAllWords, restaurantsMatchingSpecificWord);
        }
        Collections.sort(recordsMatchingAllWords);
        return recordsMatchingAllWords;
    }

    public void deleteAll() {
        restaurantRepository.deleteAll();
    }

    public void restoreDefaultRestaurantRecords() throws URISyntaxException, IOException {
        logger.info("Loading Default Restaurants Data");
        Resource resource = new ClassPathResource(DATA_FILE_NAME);
        DB db = mongo.getDB(mongoProperties.getDatabase());
        DBCollection restaurant = db.getCollection(RESTAURANT_COLLECTION_NAME);
        loadDataFromFileInMongoCollection(resource, restaurant);
    }

    void save(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    Restaurant findById(String uniqueId) {
        return restaurantRepository.findOne(uniqueId);
    }
}

