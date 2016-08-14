package com.chandanb.example.springmongodb.utils;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HelperUtilsTest {

    @Test
    public void shouldReturnIntersectionOfTwoNonEmptyLists(){
        List<String> stringList1 = Arrays.asList("Singapore","India");
        List<String> stringList2 = Arrays.asList("Singapore","USA");
        assertEquals(Arrays.asList("Singapore"), HelperUtils.intersection(stringList1, stringList2));
    }

    @Test
    public void shouldReturnIntersectionOfTwoListsWhereOneIsEmpty(){
        List<String> stringList1 = Arrays.asList("Singapore","India");
        List<String> stringList2 = Arrays.asList();
        assertEquals(Arrays.asList(), HelperUtils.intersection(stringList1, stringList2));
    }

}