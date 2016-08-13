package com.chandanb.example.springmongodb.controller;

import com.chandanb.example.springmongodb.model.Restaurant;
import com.chandanb.example.springmongodb.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@RequestMapping("/")
@RestController
public class RestaurantController {

    final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

    @Inject
    private RestaurantService restaurantService;

    @RequestMapping(method= RequestMethod.GET)
    public List<Restaurant> getAll() {
        return restaurantService.findAll();
    }

    @RequestMapping(value = "/search/{query}", method = RequestMethod.GET)
    public List<Restaurant> getMatching(@PathVariable("query") String query, Model model) {
        return restaurantService.findRestaurantsWithNameContaining(query);
    }
}
