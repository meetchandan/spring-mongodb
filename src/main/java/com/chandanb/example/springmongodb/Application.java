package com.chandanb.example.springmongodb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.net.UnknownHostException;

@SpringBootApplication
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws UnknownHostException {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        System.out.println("Welcome to Spring Boot");
    }
}
