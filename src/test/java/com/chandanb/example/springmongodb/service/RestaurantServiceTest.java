package com.chandanb.example.springmongodb.service;

import com.chandanb.example.springmongodb.Application;
import com.chandanb.example.springmongodb.model.Restaurant;
import junitx.framework.ListAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class RestaurantServiceTest {
    private int defaultRestaurantCount;

    private Restaurant testRestaurant = null;

    @Inject
    private RestaurantService restaurantService;

    @Before
    public void setup() throws Exception {
        // Initialize test database.
        restaurantService.deleteAll();
        restaurantService.restoreDefaultRecords();
        defaultRestaurantCount = restaurantService.findAll().size();
    }

    @Test
    public void shouldReturnAllRecordsOnFindAll() {
        assertEquals(restaurantService.findAll().size(), defaultRestaurantCount);
    }


    @Test
    public void shouldSuccessfullySaveRecordInCollection() {
        Restaurant restaurant = new Restaurant("Marriot", "California");
        restaurantService.save(restaurant);
        assertEquals(restaurantService.findAll().size(), defaultRestaurantCount + 1);

    }

    @Test
    public void shouldFindById() {
        Restaurant restaurant = new Restaurant("Marriot", "California");
        restaurant.setId("uniqueId");
        restaurantService.save(restaurant);
        assertEquals(restaurant, restaurantService.findById("uniqueId"));
    }

    @Test
    public void shouldReturnRecordsSortedByScoreWhichContainQueryInThierNameField(){
        Restaurant restaurant1 = new Restaurant("JW Marriot Pasha", "California");
        restaurant1.setId("uniqueId");
        Restaurant restaurant2 = new Restaurant("Marriot marriot Blu", "LA");
        restaurant1.setId("uniqueId1");
        restaurantService.save(restaurant1);
        restaurantService.save(restaurant2);
        List<Restaurant> actual = restaurantService.findRestaurantsWithNameContaining("marriot");
        assertEquals(restaurant2, actual.get(0));
        assertEquals(restaurant1, actual.get(1));
    }
}
