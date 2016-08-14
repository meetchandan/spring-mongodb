package com.chandanb.example.springmongodb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping("/error")
@RestController
public class ErrorController {

    @CrossOrigin(origins = "http://localhost:8000")
    @RequestMapping(method= RequestMethod.GET)
    public String getErrorMessage() {
        return "Sorry! You have landed at the wrong page. Please check the request URL";
    }
}
