package com.chandanb.example.springmongodb.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.TextScore;

import java.io.Serializable;
import java.util.Objects;

@Document(collection = "restaurants")
public class Restaurant implements Serializable, Comparable<Restaurant> {
    @Id
    private String id;

    @NotEmpty
    @Field("name")
    @TextIndexed(weight = 2.5f)
    private String name;

    @Field("city")
    @TextIndexed(weight = 1.5f)
    private String city;

    @TextScore
    private Float score;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        Restaurant res = (Restaurant) o;

        return Objects.equals(id, res.id);

    }

    @Override
    public int compareTo(Restaurant o) {
        if(this.score >= o.score) return 1;
        return 0;
    }
}