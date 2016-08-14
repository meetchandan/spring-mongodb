package com.chandanb.example.springmongodb.controller;

import com.chandanb.example.springmongodb.Application;
import com.chandanb.example.springmongodb.model.Restaurant;
import com.chandanb.example.springmongodb.repository.RestaurantRepository;
import com.chandanb.example.springmongodb.service.RestaurantService;
import com.mongodb.Mongo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class RestaurantControllerTest {
    private MockMvc mockMvc;

    @Inject
    private RestaurantRepository restaurantRepository;
    @Inject
    RestaurantService restaurantService;
    @Inject
    RestaurantController restaurantController;
    @Inject
    private Mongo mongo;
    @Inject
    private MongoProperties mongoProperties;

    Restaurant restaurant1;
    Restaurant restaurant2;

    @Before
    public void setup() throws IOException, URISyntaxException {
        ReflectionTestUtils.setField(restaurantService, "restaurantRepository",	restaurantRepository);
        ReflectionTestUtils.setField(restaurantService, "mongoProperties",	mongoProperties);
        ReflectionTestUtils.setField(restaurantService, "mongo",	mongo);
        restaurant1 = new Restaurant("name one", "city1", "address1");
        restaurant2 = new Restaurant("name two", "city2", "address2");
        ReflectionTestUtils.setField(restaurantController, "restaurantService", restaurantService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).build();
    }

    @Test
    public void shouldReturnEmptyJsonIfNoRecordsInCollection() throws Exception {
        restaurantService.deleteAll();
        mockMvc.perform(get("/").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("[]"));
    }

    @Test
    public void shouldReturnAllRecordsAsJsonOnRootRoute() throws Exception {
        restaurantService.deleteAll();
        restaurantRepository.save(restaurant1);
        restaurantRepository.save(restaurant2);
        String expectedRecord1 = "{\"name\" : \"name one\", \"city\" : \"city1\", \"address\" : \"address1\"}";
        String expectedRecord2 = "{\"name\" : \"name two\", \"city\" : \"city2\", \"address\" : \"address2\"}";
        mockMvc.perform(get("/").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("[" + expectedRecord1 + "," + expectedRecord2 + "]"));
    }

    @Test
    public void shouldReturnRecordsMatchingQueryAsJson() throws Exception {
        restaurantService.deleteAll();
        restaurantRepository.save(restaurant1);
        restaurantRepository.save(restaurant2);
        restaurantRepository.save(new Restaurant("otherRestaurant", "Singapore", "address1"));
        String expectedRecord1 = "{\"name\" : \"name one\", \"city\" : \"city1\",\"address\" : \"address1\"}";
        String expectedRecord2 = "{\"name\" : \"name two\", \"city\" : \"city2\", \"address\" : \"address2\"}";
        mockMvc.perform(get("/search/name").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("[" + expectedRecord1 + "," + expectedRecord2 + "]"));
    }

    // partial text search is not yet provided by mongodb  - Open Issue: https://jira.mongodb.org/browse/SERVER-15090
    @Test
    public void shouldReturnEmptyJsonOnPartialMatch() throws Exception {
        restaurantService.deleteAll();
        restaurantRepository.save(restaurant1);
        restaurantRepository.save(restaurant2);
        mockMvc.perform(get("/search/nam").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("[]"));
    }
}
