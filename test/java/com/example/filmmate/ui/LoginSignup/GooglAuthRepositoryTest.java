package com.example.filmmate.ui.LoginSignup;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

import java.util.Date;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GooglAuthRepositoryTest {
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
    GoogleAuthRepository googleAuthRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        googleAuthRepository = mock(GoogleAuthRepository.class);

    }

    @Test
    public void testGoogleUser(){
        // stub firebase auth and current user id
        when(firebaseAuth.getCurrentUser()).thenReturn(firebaseUser);
        when(firebaseUser.getUid()).thenReturn("2");

        // stub match collection and document
        when(firebaseFirestore.collection("users")).thenReturn(collectionReference);
        when(collectionReference.document("1")).thenReturn(documentReference);

        // stub document snapshot
        when(documentReference.get()).thenReturn(documentSnapshotTask);
        when(documentSnapshotTask.getResult()).thenReturn(documentSnapshot);
        GoogleSignInAccount account = mock(GoogleSignInAccount.class);
        doNothing().when(googleAuthRepository).createUserFromGoogle(account);
    }


    @Test
    public void testGoogleIfNotExist(){
        when(firebaseAuth.getCurrentUser()).thenReturn(firebaseUser);
        when(firebaseUser.getUid()).thenReturn("3");

        // stub match collection and document
        when(firebaseFirestore.collection("users")).thenReturn(collectionReference);
        when(collectionReference.document("1")).thenReturn(documentReference);

        // stub document snapshot
        when(documentReference.get()).thenReturn(documentSnapshotTask);
        when(documentSnapshotTask.getResult()).thenReturn(documentSnapshot);
        GoogleSignInAccount account = mock(GoogleSignInAccount.class);
        MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
        when(googleAuthRepository.createUserInFirestoreIfNotExists(account)).thenReturn(user);
    }

    @Test
    public void testGoogle(){
        when(firebaseAuth.getCurrentUser()).thenReturn(firebaseUser);
        when(firebaseUser.getUid()).thenReturn("3");

        // stub match collection and document
        when(firebaseFirestore.collection("users")).thenReturn(collectionReference);
        when(collectionReference.document("1")).thenReturn(documentReference);

        // stub document snapshot
        when(documentReference.get()).thenReturn(documentSnapshotTask);
        when(documentSnapshotTask.getResult()).thenReturn(documentSnapshot);
        GoogleSignInAccount account = mock(GoogleSignInAccount.class);
        MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
        when(googleAuthRepository.createUserInFirestoreIfNotExists(account)).thenReturn(user);
    }
}
