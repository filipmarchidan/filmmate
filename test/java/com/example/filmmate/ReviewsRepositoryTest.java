package com.example.filmmate;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.filmmate.repositories.firebase.RatingsRepository;
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

import java.util.HashMap;
import java.util.Map;

import static com.example.filmmate.RxTestUtil.setupTask;
import static com.example.filmmate.RxTestUtil.testOnCompleteListener;
import static com.example.filmmate.RxTestUtil.testOnFailureListener;
import static com.example.filmmate.RxTestUtil.testOnSuccessListener;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReviewsRepositoryTest {

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

    ReviewsRepository reviewsRepository;

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

        reviewsRepository = new ReviewsRepository(firebaseAuth, firebaseFirestore);
    }

    @Test
    public void getUserMovieReviewTest() {

        Map<String, String> reviews = new HashMap<>();
        reviews.put("0", "good");
        reviews.put("1", "nice");

        when(documentSnapshot.get("movieReviews")).thenReturn(reviews);

        reviewsRepository.getUserMovieReview("0");

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        assertEquals(reviews.get("0"), reviewsRepository.getUserMediaReviewLiveData().getValue());
    }

    @Test
    public void getUserMovieReviewCurrentUserNullTest() {

        Map<String, String> reviews = new HashMap<>();
        reviews.put("0", "good");
        reviews.put("1", "nice");

        when(firebaseAuth.getCurrentUser()).thenReturn(null);

        reviewsRepository.getUserMovieReview("0");

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        verify(documentSnapshot, times(0)).get("movieReviews");
    }

    @Test
    public void getUserMovieReviewEmptyTest() {

        Map<String, String> reviews = new HashMap<>();

        when(documentSnapshot.get("movieReviews")).thenReturn(reviews);

        reviewsRepository.getUserMovieReview("0");

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        assertNull(reviewsRepository.getUserMediaReviewLiveData().getValue());
    }

    @Test
    public void getUserTVReviewTest() {

        Map<String, String> reviews = new HashMap<>();
        reviews.put("0", "good");
        reviews.put("1", "nice");

        when(documentSnapshot.get("tvReviews")).thenReturn(reviews);

        reviewsRepository.getUserTVReview("2");

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        assertEquals(reviews.get("2"), reviewsRepository.getUserMediaReviewLiveData().getValue());
    }

    @Test
    public void getUserTVReviewCurrentUserNullTest() {

        Map<String, String> reviews = new HashMap<>();
        reviews.put("0", "good");
        reviews.put("1", "nice");

        when(firebaseAuth.getCurrentUser()).thenReturn(null);

        reviewsRepository.getUserTVReview("0");

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        verify(documentSnapshot, times(0)).get("tvReviews");
    }

    @Test
    public void updateUserReviewMovieTest() {

        String movieId = "1";
        String review = "good";

        when(documentReference.update("movieReviews." + movieId, review)).thenReturn(mockVoidTask);

        reviewsRepository.updateUserReviewMovie(movieId, review);

        // stub onSuccessListener and onFailureListener
        testOnSuccessListener.getValue().onSuccess(voidData);
        testOnFailureListener.getValue().onFailure(new Exception());

        verify(documentReference).update("movieReviews." + movieId, review);

    }

    @Test
    public void updateUserReviewMovieBlankTest() {
        String movieId = "1";
        String review = "";

        reviewsRepository.updateUserReviewMovie(movieId, review);

        verify(documentReference, times(0)).update("movieReviews." + movieId, review);

    }

    @Test
    public void updateUserReviewTVTest() {

        String tvId = "1";
        String review = "good";

        when(documentReference.update("tvReviews." + tvId, review)).thenReturn(mockVoidTask);

        reviewsRepository.updateUserReviewTV(tvId, review);

        // stub onSuccessListener and onFailureListener
        testOnSuccessListener.getValue().onSuccess(voidData);
        testOnFailureListener.getValue().onFailure(new Exception());

        verify(documentReference).update("tvReviews." + tvId, review);

    }

    @Test
    public void updateUserReviewTVBlankTest() {
        String tvId = "1";
        String review = "";

        reviewsRepository.updateUserReviewTV(tvId, review);

        verify(documentReference, times(0)).update("tvReviews." + tvId, review);

    }
}
