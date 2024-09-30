package com.example.filmmate.ui.LoginSignup;

import android.app.Application;

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
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthAppRepositoryTest {
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

    @Mock
    Application application;

    @Mock
    AuthAppRepository authAppRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        authAppRepository = mock(AuthAppRepository.class);

    }

    @Test
    public void testSignIn(){
        // stub firebase auth and current user id
        when(firebaseAuth.getCurrentUser()).thenReturn(firebaseUser);
        when(firebaseUser.getUid()).thenReturn("2");

        // stub match collection and document
        when(firebaseFirestore.collection("users")).thenReturn(collectionReference);
        when(collectionReference.document("1")).thenReturn(documentReference);

        // stub document snapshot
        when(documentReference.get()).thenReturn(documentSnapshotTask);
        when(documentSnapshotTask.getResult()).thenReturn(documentSnapshot);
        doNothing().when(authAppRepository).register("","","",new Date(),"", "male");
    }


    @Test
    public void testLogIn(){
        when(firebaseAuth.getCurrentUser()).thenReturn(firebaseUser);
        when(firebaseUser.getUid()).thenReturn("3");

        // stub match collection and document
        when(firebaseFirestore.collection("users")).thenReturn(collectionReference);
        when(collectionReference.document("1")).thenReturn(documentReference);

        // stub document snapshot
        when(documentReference.get()).thenReturn(documentSnapshotTask);
        when(documentSnapshotTask.getResult()).thenReturn(documentSnapshot);

        doNothing().when(authAppRepository).login("user","pass");
    }
}
