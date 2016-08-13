package com.chandanb.example.springmongodb.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Document(collection = "restaurant")
public class Restaurant implements Serializable {
    @Id
    private String id;

    @NotEmpty
    @Field("name")
    @TextIndexed
    private String name;

    @Field("city")
    private String city;

    public Restaurant(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public Restaurant(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}