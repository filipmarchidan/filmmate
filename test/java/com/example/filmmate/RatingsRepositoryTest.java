package com.example.filmmate;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.filmmate.repositories.firebase.RatingsRepository;
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

import java.util.HashMap;
import java.util.Map;

import static com.example.filmmate.RxTestUtil.setupTask;
import static com.example.filmmate.RxTestUtil.testOnCompleteListener;
import static com.example.filmmate.RxTestUtil.testOnFailureListener;
import static com.example.filmmate.RxTestUtil.testOnSuccessListener;
import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RatingsRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantRule = new InstantTaskExecutorRule() ;

    @Mock
    private Task<Void> mockVoidTask;

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

    RatingsRepository ratingsRepository;

    private Void voidData = null;



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
        setupTask(mockVoidTask);

        ratingsRepository = new RatingsRepository(firebaseAuth, firebaseFirestore);
    }


    @Test
    public void getUserMovieRatingTest() {

        Map<String, Object> ratings = new HashMap<>();
        ratings.put("0", (double) 4);
        ratings.put("1", (double) 5);

        when(documentSnapshot.get("movieRatings")).thenReturn(ratings);

        ratingsRepository.getUserMovieRating("0");

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        assertEquals(4, ratingsRepository.getUserMediaRatingLiveData().getValue(), 0);
    }

    @Test
    public void getUserMovieRatingCurrentUserNullTest() {

        Map<String, Object> ratings = new HashMap<>();
        ratings.put("0", (double) 4);
        ratings.put("1", (double) 5);

        when(firebaseAuth.getCurrentUser()).thenReturn(null);

        ratingsRepository.getUserMovieRating("0");

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        verify(documentSnapshot, times(0)).get("movieRatings");
    }

    @Test
    public void getUserMovieRatingEmptyTest() {

        Map<String, Object> ratings = new HashMap<>();

        when(documentSnapshot.get("movieRatings")).thenReturn(ratings);

        ratingsRepository.getUserMovieRating("0");

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        assertEquals(0, ratingsRepository.getUserMediaRatingLiveData().getValue(), 0);
    }

    @Test
    public void getUserTVRatingTest() {

        Map<String, Object> ratings = new HashMap<>();
        ratings.put("0", (double) 4);
        ratings.put("1", (double) 5);

        when(documentSnapshot.get("tvRatings")).thenReturn(ratings);

        ratingsRepository.getUserTVRating("2");

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        assertEquals(0, ratingsRepository.getUserMediaRatingLiveData().getValue(), 0);
    }

    @Test
    public void getUserTVRatingCurrentUserNullTest() {

        Map<String, Object> ratings = new HashMap<>();
        ratings.put("0", (double) 4);
        ratings.put("1", (double) 5);

        when(firebaseAuth.getCurrentUser()).thenReturn(null);

        ratingsRepository.getUserTVRating("0");

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        verify(documentSnapshot, times(0)).get("tvRatings");
    }

    @Test
    public void updateUserRatingMovieTest() {

        String movieId = "1";
        Float rating = 3f;

        when(documentReference.update("movieRatings." + movieId, rating)).thenReturn(mockVoidTask);

        ratingsRepository.updateUserRatingMovie(movieId, rating);

        // stub onSuccessListener and onFailureListener
        testOnSuccessListener.getValue().onSuccess(voidData);
        testOnFailureListener.getValue().onFailure(new Exception());

        verify(documentReference).update("movieRatings." + movieId, rating);

    }

    @Test
    public void updateUserRatingTVTest() {

        String tvId = "1";
        Float rating = 3f;

        when(documentReference.update("tvRatings." + tvId, rating)).thenReturn(mockVoidTask);

        ratingsRepository.updateUserRatingTV(tvId, rating);

        // stub onSuccessListener and onFailureListener
        testOnSuccessListener.getValue().onSuccess(voidData);
        testOnFailureListener.getValue().onFailure(new Exception());

        verify(documentReference).update("tvRatings." + tvId, rating);

    }
}
