package com.example.filmmate.ui.LoginSignup;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserTest {

    private Map<String, Object> user;
    private Map<String, Object> user2;
    private User user3;
    private Map<String, Object> preValues;

    @Before
    public void setUp(){

        user = new HashMap<>();
        preValues = new HashMap<>();
        user2 = new HashMap<>();
        user3 = new User();

        preValues.put("id", 2);
        preValues.put("username", "example");

        this.user.put("id", 1);
        this.user.put("username", "user");
        this.user.put("bio", "");
        this.user.put("movieQuote", "");
        this.user.put("age", 0);
        this.user.put("action", 0);
        this.user.put("adventure", 0);
        this.user.put("animation", 0);
        this.user.put("comedy", 0);
        this.user.put("crime", 0);
        this.user.put("documentary", 0);
        this.user.put("drama", 0);
        this.user.put("family", 0);
        this.user.put("fantasy", 0);
        this.user.put("horror", 0);
        this.user.put("online", true);
        this.user.put("offline", true);
        this.user.put("filterMale", true);
        this.user.put("filterFemale", true);
        this.user.put("friendship", true);
        this.user.put("relationship", true);
        this.user.put("gender", "male");
        this.user.put("maxDistance", 100);
        this.user.put("minAge", 18);
        this.user.put("maxAge", 100);
        this.user.put("music", 0);
        this.user.put("mystery", 0);
        this.user.put("romance", 0);
        this.user.put("scienceFiction", 0);
        this.user.put("thriller", 0);
        this.user.put("tvMovie", 0);
        this.user.put("war", 0);
        this.user.put("profileImageUrl", "default");
        this.user.put("western", 0);
        this.user.put("reports", 0);
        this.user.put("phoneVerified", true);
        this.user.put("disabled", false);
        Map<String, Object> movieRatings = new HashMap<>();
        Map<String, Object> movieReviews = new HashMap<>();
        Map<String, Object> tvRatings = new HashMap<>();
        Map<String, Object> tvReviews = new HashMap<>();
        ArrayList<Integer> matches = new ArrayList<>();
        ArrayList<Integer> favMovies = new ArrayList<>();
        ArrayList<Integer> favTvShows = new ArrayList<>();
        ArrayList<Integer> favCast = new ArrayList<>();
        ArrayList<Integer> favDirectors = new ArrayList<>();
        ArrayList<String> tags = new ArrayList<>();
        ArrayList<String> recommendedMatches = new ArrayList<>();
        ArrayList<String> chats = new ArrayList<>();
        ArrayList<String> blockedMatches = new ArrayList<>();
        ArrayList<String> tagsFilter = new ArrayList<>();

        this.user.put("favMovies", favMovies);
        this.user.put("favTvShows", favTvShows);
        this.user.put("favCast", favCast);
        this.user.put("favDirectors", favDirectors);
        this.user.put("matches", matches);
        this.user.put("movieRatings", movieRatings);
        this.user.put("movieReviews", movieReviews);
        this.user.put("tvRatings", tvRatings);
        this.user.put("tvReviews", tvReviews);
        this.user.put("tags", tags);
        this.user.put("recommendedMatches", recommendedMatches);
        this.user.put("chats", chats);
        this.user.put("blockedMatches", blockedMatches);
        this.user.put("backdropImageUrl", "default");
        this.user.put("filterTags", tagsFilter);

        this.user2.put("id", 2);
        this.user2.put("username", "example");
        this.user2.put("bio", "");
        this.user2.put("movieQuote", "");
        this.user2.put("age", 0);
        this.user2.put("action", 0);
        this.user2.put("adventure", 0);
        this.user2.put("animation", 0);
        this.user2.put("comedy", 0);
        this.user2.put("crime", 0);
        this.user2.put("documentary", 0);
        this.user2.put("drama", 0);
        this.user2.put("family", 0);
        this.user2.put("fantasy", 0);
        this.user2.put("horror", 0);
        this.user2.put("online", true);
        this.user2.put("offline", true);
        this.user2.put("filterMale", true);
        this.user2.put("filterFemale", true);
        this.user2.put("friendship", true);
        this.user2.put("relationship", true);
        this.user2.put("gender", "male");
        this.user2.put("maxDistance", 100);
        this.user2.put("minAge", 18);
        this.user2.put("maxAge", 100);
        this.user2.put("music", 0);
        this.user2.put("mystery", 0);
        this.user2.put("romance", 0);
        this.user2.put("scienceFiction", 0);
        this.user2.put("thriller", 0);
        this.user2.put("tvMovie", 0);
        this.user2.put("war", 0);
        this.user2.put("profileImageUrl", "default");
        this.user2.put("western", 0);
        this.user2.put("reports", 0);
        this.user2.put("phoneVerified", true);
        this.user2.put("disabled", false);
        this.user2.put("backdropImageUrl", "default");
        Map<String, Object> movieRatings2 = new HashMap<>();
        Map<String, Object> movieReviews2 = new HashMap<>();
        Map<String, Object> tvRatings2 = new HashMap<>();
        Map<String, Object> tvReviews2 = new HashMap<>();
        ArrayList<Integer> matches2 = new ArrayList<>();
        ArrayList<Integer> favMovies2 = new ArrayList<>();
        ArrayList<Integer> favTvShows2 = new ArrayList<>();
        ArrayList<Integer> favCast2 = new ArrayList<>();
        ArrayList<Integer> favDirectors2 = new ArrayList<>();
        ArrayList<String> tags2 = new ArrayList<>();
        ArrayList<String> recommendedMatches2 = new ArrayList<>();
        ArrayList<String> chats2 = new ArrayList<>();
        ArrayList<String> blockedMatches2 = new ArrayList<>();
        ArrayList<String> tagsFilter2 = new ArrayList<>();

        this.user2.put("filterTags", tagsFilter2);
        this.user2.put("favMovies", favMovies2);
        this.user2.put("favTvShows", favTvShows2);
        this.user2.put("favCast", favCast2);
        this.user2.put("favDirectors", favDirectors2);
        this.user2.put("matches", matches2);
        this.user2.put("movieRatings", movieRatings2);
        this.user2.put("movieReviews", movieReviews2);
        this.user2.put("tvRatings", tvRatings2);
        this.user2.put("tvReviews", tvReviews2);
        this.user2.put("tags", tags2);
        this.user2.put("recommendedMatches", recommendedMatches2);
        this.user2.put("chats", chats2);
        this.user2.put("blockedMatches", blockedMatches2);

    }

    @Test
    public void usersSame(){
        User user1 = new User();

        user1.createVerifiedUser(preValues);
        Assert.assertFalse(user1.equals(user2));
    }

    @Test
    public void usersDifferent(){
        Assert.assertFalse(user3.equals(user));
    }

    @Test
    public void userGet(){
        User user1 = new User();

        user1.createVerifiedUser(preValues);
        Assert.assertEquals(user1.getUser(),user2);
    }

    @Test
    public void normalUser(){
        User user1 = new User();
        Map<String, Object> map = new HashMap<>();
        map.put("id",1);
        map.put("username", "user");
        user1.setUser(map);
    }
}
