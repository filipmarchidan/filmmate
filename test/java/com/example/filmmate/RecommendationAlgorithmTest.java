package com.example.filmmate;

import com.example.filmmate.repositories.firebase.RecommendationAlgorithm;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RecommendationAlgorithmTest {

    RecommendationAlgorithm recommendationAlgorithm;

    @Before
    public void setup() {
        recommendationAlgorithm = new RecommendationAlgorithm();
    }

    @Test
    public void getBestRatingsNull() {
        List<String> result =  recommendationAlgorithm.getBestRatings(null);
        assertEquals(new ArrayList<>(), result);
    }

    @Test
    public void getBestRatingsEmpty() {
        List<String> result =  recommendationAlgorithm.getBestRatings(new HashMap<>());
        assertEquals(new ArrayList<>(), result);
    }

    @Test
    public void getBestRatings() {
        Map<String, Double> ratings = new HashMap<>();
        ratings.put("0", (double) 4);
        ratings.put("1", (double) 5);
        ratings.put("2", (double) 1);
        ratings.put("3", (double) 4.5);
        ratings.put("4", (double) 5);
        ratings.put("5", (double) 5);
        ratings.put("6", (double) 3);


        List<String> result =  recommendationAlgorithm.getBestRatings(ratings);
        assertEquals(Arrays.asList("0", "1", "3", "4", "5"), result);
    }
}
