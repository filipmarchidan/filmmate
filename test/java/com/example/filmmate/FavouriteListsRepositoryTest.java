package com.example.filmmate;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.filmmate.repositories.firebase.FavouriteListsRepository;

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

import static com.example.filmmate.RxTestUtil.setupTask;
import static com.example.filmmate.RxTestUtil.testOnCompleteListener;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class FavouriteListsRepositoryTest {

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
    }

    @Test
    public void getUserFavouriteMoviesTest() {

        // stub list of favourite movies
        when(documentSnapshot.get("favMovies")).thenReturn(Arrays.asList(1,2));

        // create repository and get favourite movies from database
        FavouriteListsRepository favouriteListsRepository = new FavouriteListsRepository(firebaseAuth, firebaseFirestore);
        favouriteListsRepository.getUserFavouriteMovies();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        assertEquals(Arrays.asList(1,2), favouriteListsRepository.getUserFavouriteMoviesLiveData().getValue());
    }

    @Test
    public void getUserFavouriteMoviesCurrentUserNullTest() {

        // create repository and get favourite movies from database
        FavouriteListsRepository favouriteListsRepository = new FavouriteListsRepository(firebaseAuth, firebaseFirestore);

        when(firebaseAuth.getCurrentUser()).thenReturn(null);
        favouriteListsRepository.getUserFavouriteMovies();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        verify(documentSnapshot, times(0)).get("favMovies");
    }

    @Test
    public void getUserFavouriteTVTest() {

        // stub list of favourite tv
        when(documentSnapshot.get("favTvShows")).thenReturn(Arrays.asList(1,2));

        // create repository and get favourite tv from database
        FavouriteListsRepository favouriteListsRepository = new FavouriteListsRepository(firebaseAuth, firebaseFirestore);
        favouriteListsRepository.getUserFavouriteTV();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        assertEquals(Arrays.asList(1,2), favouriteListsRepository.getUserFavouriteTVLiveData().getValue());
    }


    @Test
    public void getUserFavouriteTVCurrentUserNullTest() {

        // create repository and get favourite tv from database
        FavouriteListsRepository favouriteListsRepository = new FavouriteListsRepository(firebaseAuth, firebaseFirestore);

        when(firebaseAuth.getCurrentUser()).thenReturn(null);

        favouriteListsRepository.getUserFavouriteTV();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        verify(documentSnapshot, times(0)).get("favTvShows");
    }

    @Test
    public void getUserFavouritePeopleCurrentUserNullTest() {

        // create repository and get favourite people from database
        FavouriteListsRepository favouriteListsRepository = new FavouriteListsRepository(firebaseAuth, firebaseFirestore);

        when(firebaseAuth.getCurrentUser()).thenReturn(null);

        favouriteListsRepository.getUserFavouritePeople();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        verify(documentSnapshot, times(0)).get("favCast");
    }

    @Test
    public void getUserFavouritePeopleTest() {

        // stub list of favourite people
        when(documentSnapshot.get("favCast")).thenReturn(Arrays.asList(1,2));

        // create repository and get favourite people from database
        FavouriteListsRepository favouriteListsRepository = new FavouriteListsRepository(firebaseAuth, firebaseFirestore);
        favouriteListsRepository.getUserFavouritePeople();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        assertEquals(Arrays.asList(1,2), favouriteListsRepository.getUserFavouritePeopleLiveData().getValue());
    }
}

