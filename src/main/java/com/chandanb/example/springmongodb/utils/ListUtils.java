package com.chandanb.example.springmongodb.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
    public static <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }
}
