package com.chandanb.example.springmongodb.repository;

import com.chandanb.example.springmongodb.model.Restaurant;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface RestaurantRepository extends MongoRepository<Restaurant,String> {
    List<Restaurant> findAllByOrderByScoreDesc(TextCriteria textCriteria);
}
