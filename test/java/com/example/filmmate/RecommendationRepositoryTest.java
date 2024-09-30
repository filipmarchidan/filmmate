package com.example.filmmate;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.filmmate.repositories.firebase.RecommendationRepository;
import com.example.filmmate.repositories.firebase.ReviewsRepository;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.example.filmmate.RxTestUtil.setupTask;
import static com.example.filmmate.RxTestUtil.testOnCompleteListener;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecommendationRepositoryTest {

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

    RecommendationRepository recommendationRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // stub firebase auth and current user id
        when(firebaseAuth.getCurrentUser()).thenReturn(firebaseUser);
        when(firebaseUser.getUid()).thenReturn("1");

        // stub user collection and document
        when(firebaseFirestore.collection("users")).thenReturn(collectionReference);
        when(collectionReference.document("1")).thenReturn(documentReference);

        // stub document snapshot
        when(documentReference.get()).thenReturn(documentSnapshotTask);
        when(documentSnapshotTask.getResult()).thenReturn(documentSnapshot);


        // set up task with onCompleteListener
        setupTask(documentSnapshotTask);

        recommendationRepository = new RecommendationRepository(firebaseAuth, firebaseFirestore);
    }



    @Test
    public void getMoviesIdsRecommendationTest() {
        Map<String, Object> ratings = new HashMap<>();
        ratings.put("0", (double) 4);
        ratings.put("1", (double) 2);

        when(documentSnapshot.get("movieRatings")).thenReturn(ratings);
        when(documentSnapshot.get("favMovies")).thenReturn(Arrays.asList((long) 3));

        recommendationRepository.getMoviesIdsRecommendation();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        assertEquals(Arrays.asList("0"), recommendationRepository.getUserRecommendedMoviesIdsLiveData().getValue());
    }

    @Test
    public void getMoviesIdsRecommendationEmptyTest() {
        Map<String, Object> ratings = new HashMap<>();
        ratings.put("0", (double) 1);
        ratings.put("1", (double) 2);

        when(documentSnapshot.get("movieRatings")).thenReturn(ratings);
        when(documentSnapshot.get("favMovies")).thenReturn(Arrays.asList((long) 3));

        recommendationRepository.getMoviesIdsRecommendation();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        assertEquals(Arrays.asList("3"), recommendationRepository.getUserRecommendedMoviesIdsLiveData().getValue());
    }

    @Test
    public void getMoviesIdsRecommendationCurrentUserNullTest() {
        Map<String, Object> ratings = new HashMap<>();
        ratings.put("0", (double) 1);
        ratings.put("1", (double) 2);

        when(documentSnapshot.get("movieRatings")).thenReturn(ratings);
        when(documentSnapshot.get("favMovies")).thenReturn(Arrays.asList((long) 3));
        when(firebaseAuth.getCurrentUser()).thenReturn(null);

        recommendationRepository.getMoviesIdsRecommendation();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        verify(documentSnapshot, times(0)).get("movieRatings");
        verify(documentSnapshot, times(0)).get("favMovies");
    }


    @Test
    public void getTVIdsRecommendationTest() {
        Map<String, Object> ratings = new HashMap<>();
        ratings.put("0", (double) 1);
        ratings.put("1", (double) 4);

        when(documentSnapshot.get("tvRatings")).thenReturn(ratings);
        when(documentSnapshot.get("favTvShows")).thenReturn(Arrays.asList((long) 4));

        recommendationRepository.getTVIdsRecommendation();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        assertEquals(Arrays.asList("1"), recommendationRepository.getUserRecommendedTVIdsLiveData().getValue());
    }

    @Test
    public void getTVIdsRecommendationEmptyTest() {
        Map<String, Object> ratings = new HashMap<>();
        ratings.put("0", (double) 1);
        ratings.put("1", (double) 2);

        when(documentSnapshot.get("tvRatings")).thenReturn(ratings);
        when(documentSnapshot.get("favTvShows")).thenReturn(Arrays.asList((long) 4));

        recommendationRepository.getTVIdsRecommendation();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        assertEquals(Arrays.asList("4"), recommendationRepository.getUserRecommendedTVIdsLiveData().getValue());
    }

    @Test
    public void getTVIdsRecommendationCurrentUserNullTest() {
        Map<String, Object> ratings = new HashMap<>();
        ratings.put("0", (double) 1);
        ratings.put("1", (double) 2);

        when(documentSnapshot.get("tvRatings")).thenReturn(ratings);
        when(documentSnapshot.get("favTvShows")).thenReturn(Arrays.asList((long) 3));
        when(firebaseAuth.getCurrentUser()).thenReturn(null);

        recommendationRepository.getTVIdsRecommendation();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        verify(documentSnapshot, times(0)).get("tvRatings");
        verify(documentSnapshot, times(0)).get("favTvShows");
    }
}
