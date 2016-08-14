package com.chandanb.example.springmongodb.utils;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HelperUtils {
    public static <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();
        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }

    public static void loadDataFromFileInMongoCollection(Resource resource, DBCollection restaurant) throws IOException {
        String line;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))){
            while ((line = br.readLine()) != null) {
                DBObject dbo = (DBObject) com.mongodb.util.JSON.parse(line);
                restaurant.insert(dbo);
            }
        }
    }
}
