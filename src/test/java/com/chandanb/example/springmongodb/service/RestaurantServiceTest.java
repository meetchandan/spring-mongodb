package com.chandanb.example.springmongodb.service;

import com.chandanb.example.springmongodb.Application;
import com.chandanb.example.springmongodb.model.Restaurant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.List;

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
        restaurant1.setId("uniqueId1");
        Restaurant restaurant2 = new Restaurant("Marriot marriot Blu", "LA");
        restaurant2.setId("uniqueId2");
        restaurantService.save(restaurant1);
        restaurantService.save(restaurant2);
        List<Restaurant> actual = restaurantService.findRestaurantsContaining("marriot");
        assertEquals(2, actual.size());
        assertEquals(restaurant2, actual.get(0));
        assertEquals(restaurant1, actual.get(1));
    }

    @Test
    public void shouldReturnRecordsContainingAllTheWordsInTheQuery(){
        Restaurant restaurant1 = new Restaurant("JW Marriot Pasha", "California");
        restaurant1.setId("uniqueId1");
        Restaurant restaurant2 = new Restaurant("Marriot marriot Blu", "LA");
        restaurant2.setId("uniqueId2");
        restaurantService.save(restaurant1);
        restaurantService.save(restaurant2);
        List<Restaurant> actual = restaurantService.findRestaurantsContaining("marriot blu");
        assertEquals(1, actual.size());
        assertEquals(restaurant2, actual.get(0));
    }

    @Test
    public void shouldReturnSortedRecordsContainingAllTheWordsInTheQuery(){
        Restaurant restaurant1 = new Restaurant("JW Marriot blu ", "California");
        restaurant1.setId("uniqueId1");
        Restaurant restaurant2 = new Restaurant("Marriot marriot Blu blu", "LA");
        restaurant2.setId("uniqueId2");
        restaurantService.save(restaurant1);
        restaurantService.save(restaurant2);
        List<Restaurant> actual = restaurantService.findRestaurantsContaining("marriot blu");
        assertEquals(2, actual.size());
        assertEquals(restaurant2, actual.get(0));
        assertEquals(restaurant1, actual.get(1));
    }

    @Test
    public void shouldSearchOnCityField(){
        Restaurant restaurant1 = new Restaurant("JW Marriot Pasha", "California");
        restaurant1.setId("uniqueId1");
        Restaurant restaurant2 = new Restaurant("Marriot marriot Blu", "LA");
        restaurant2.setId("uniqueId2");
        restaurantService.save(restaurant1);
        restaurantService.save(restaurant2);
        List<Restaurant> actual = restaurantService.findRestaurantsContaining("California");
        assertEquals(1, actual.size());
        assertEquals(restaurant1, actual.get(0));
    }

    @Test
    public void shouldNotSearchOnIdField(){
        Restaurant restaurant1 = new Restaurant("JW Marriot Pasha", "California");
        restaurant1.setId("uniqueId1");
        Restaurant restaurant2 = new Restaurant("Marriot marriot Blu", "LA");
        restaurant2.setId("uniqueId2");
        restaurantService.save(restaurant1);
        restaurantService.save(restaurant2);
        List<Restaurant> actual = restaurantService.findRestaurantsContaining("uniqueId1");
        assertEquals(0, actual.size());
    }
}
