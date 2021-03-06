package com.chandanb.example.springmongodb.config;

import com.chandanb.example.springmongodb.service.RestaurantService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class SpringContextListener implements ApplicationListener<ContextRefreshedEvent> {
	
	@Inject 
	private RestaurantService restaurantService;


	public void onApplicationEvent(ContextRefreshedEvent event) {
		restaurantService.deleteAll();
		try {
			restaurantService.restoreDefaultRestaurantRecords();
		} catch (Exception e) {
			e.printStackTrace();
		}
	};
}
