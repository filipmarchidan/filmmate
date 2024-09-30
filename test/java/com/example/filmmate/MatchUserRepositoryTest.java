package com.example.filmmate;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.core.util.Pair;

import com.example.filmmate.data.models.MatchUser;
import com.example.filmmate.data.models.RatingReviewHelper;
import com.example.filmmate.repositories.firebase.MatchUserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.filmmate.RxTestUtil.setupTask;
import static com.example.filmmate.RxTestUtil.testOnCompleteListener;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MatchUserRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantRule = new InstantTaskExecutorRule() ;


    @Mock
    FirebaseFirestore firebaseFirestore;

    @Mock
    FirebaseUser firebaseUser;

    @Mock
    FirebaseAuth firebaseAuth;

    @Mock
    DocumentReference documentReference;

    @Mock
    CollectionReference collectionReference;

    @Mock
    Task<DocumentSnapshot> documentSnapshotTask;

    @Mock
    DocumentSnapshot documentSnapshot;

    MatchUserRepository matchUserRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // stub firebase auth and current user id
        when(firebaseAuth.getCurrentUser()).thenReturn(firebaseUser);
        when(firebaseUser.getUid()).thenReturn("2");

        // stub match collection and document
        when(firebaseFirestore.collection("users")).thenReturn(collectionReference);
        when(collectionReference.document("1")).thenReturn(documentReference);

        // stub document snapshot
        when(documentReference.get()).thenReturn(documentSnapshotTask);
        when(documentSnapshotTask.getResult()).thenReturn(documentSnapshot);

        // set up task with onCompleteListener
        setupTask(documentSnapshotTask);

        matchUserRepository = new MatchUserRepository(firebaseFirestore, firebaseAuth, "1");
    }

    @Test
    public void getMatchUserTest() {

        List<Long> favouriteMovies = new ArrayList<>();
        List<Long> favouriteTV = new ArrayList<>();
        List<Long> favouritePeople = new ArrayList<>();
        String name = "name";
        String bio = "bio";
        String movieQuote = "quote";

        when(documentSnapshot.get("favMovies")).thenReturn(favouriteMovies);
        when(documentSnapshot.get("favTvShows")).thenReturn(favouriteTV);
        when(documentSnapshot.get("favCast")).thenReturn(favouritePeople);
        when(documentSnapshot.getString("name")).thenReturn(name);
        when(documentSnapshot.getString("bio")).thenReturn(bio);
        when(documentSnapshot.getString("movieQuote")).thenReturn(movieQuote);

        // create match user
        MatchUser match = new MatchUser();
        match.setBio(bio);
        match.setName(name);
        match.setFavouriteMovies(favouriteMovies);
        match.setFavouriteTV(favouriteTV);
        match.setFavouritePeople(favouritePeople);
        match.setMovieQuote(movieQuote);

        matchUserRepository.getMatchUser();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        assertEquals(match, matchUserRepository.getMatchUserLiveData().getValue());
    }

    @Test
    public void getMatchUserCurrentUserNullTest() {

        when(firebaseAuth.getCurrentUser()).thenReturn(null);

        matchUserRepository.getMatchUser();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        verify(documentSnapshot, times(0)).get("favMovies");
        verify(documentSnapshot, times(0)).get("favTvShows");
        verify(documentSnapshot, times(0)).get("favCast");
        verify(documentSnapshot, times(0)).getString("name");
        verify(documentSnapshot, times(0)).getString("bio");
        verify(documentSnapshot, times(0)).getString("movieQuote");
    }

    @Test
    public void getRatingsAndReviewsMoviesTest() {

        Map<String, Double> ratings = new HashMap<>();
        ratings.put("1", (double) 4);

        Map<String, String> reviews = new HashMap<>();
        reviews.put("1", "good");

        when(documentSnapshot.get("movieRatings")).thenReturn(ratings);
        when(documentSnapshot.get("movieReviews")).thenReturn(reviews);

        matchUserRepository.getRatingsAndReviewsMovies();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);


        RatingReviewHelper result = new RatingReviewHelper("1", ratings.get("1").floatValue(), reviews.get("1"));

        assertEquals(Arrays.asList(result), matchUserRepository.getRatingsAndReviewsLiveData().getValue());

    }

    @Test
    public void getRatingsAndReviewsMoviesCurrentUserNullTest() {

        when(firebaseAuth.getCurrentUser()).thenReturn(null);

        matchUserRepository.getRatingsAndReviewsMovies();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        verify(documentSnapshot, times(0)).get("movieRatings");
        verify(documentSnapshot, times(0)).get("movieReviews");
    }

    @Test
    public void getRatingsAndReviewsTVTest() {

        Map<String, Double> ratings = new HashMap<>();
        ratings.put("1", (double) 4);

        Map<String, String> reviews = new HashMap<>();
        reviews.put("1", "good");

        when(documentSnapshot.get("tvRatings")).thenReturn(ratings);
        when(documentSnapshot.get("tvReviews")).thenReturn(reviews);

        matchUserRepository.getRatingsAndReviewsTV();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);


        RatingReviewHelper result = new RatingReviewHelper("1", ratings.get("1").floatValue(), reviews.get("1"));

        assertEquals(Arrays.asList(result), matchUserRepository.getRatingsAndReviewsLiveData().getValue());

    }

    @Test
    public void getRatingsAndReviewsTVCurrentUserNullTest() {

        when(firebaseAuth.getCurrentUser()).thenReturn(null);

        matchUserRepository.getRatingsAndReviewsTV();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        verify(documentSnapshot, times(0)).get("tvRatings");
        verify(documentSnapshot, times(0)).get("tvReviews");
    }

    @Test
    public void mergeMapsRatingsNullTest() {
        Map<String, String> reviews = new HashMap<>();
        reviews.put("1", "good");

        Map<String, Pair<Double, String>> result = matchUserRepository.mergeMaps(null, reviews);

        assertEquals(new HashMap<>(), result);
    }

    @Test
    public void mergeMapsReviewsNullTest() {
        Map<String, Double> ratings = new HashMap<>();
        ratings.put("1", (double) 4);

        Map<String, Pair<Double, String>> result = matchUserRepository.mergeMaps(ratings, null);

        assertEquals(new HashMap<>(), result);
    }
}
