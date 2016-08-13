package com.chandanb.example.springmongodb.controller;

import com.chandanb.example.springmongodb.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ErrorControllerTest {
    private MockMvc mockMvc;

    @Inject
    private ErrorController errorController;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(errorController).build();
    }

    @Test
    public void shouldReturnEmptyJsonIfNoRecordsInCollection() throws Exception {
        mockMvc.perform(get("/error").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string("Sorry! You have landed at the wrong page. Please check the request URL"));
    }
}