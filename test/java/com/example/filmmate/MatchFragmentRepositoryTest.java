package com.example.filmmate;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.filmmate.repositories.MatchFragmentRepository;
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
import static com.example.filmmate.RxTestUtil.testOnFailureListener;
import static com.example.filmmate.RxTestUtil.testOnSuccessListener;
import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class MatchFragmentRepositoryTest {
    @Rule
    public InstantTaskExecutorRule instantRule = new InstantTaskExecutorRule();

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

    MatchFragmentRepository matchFragmentRepository;

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

        matchFragmentRepository = new MatchFragmentRepository(firebaseAuth, firebaseFirestore);
    }

    @Test
    public void updateOnlineFilterTest(){
        Map<String, Object> user = new HashMap<>();
        user.put("online", false);
        user.put("offline", true);
        user.put("filterFemale", true);
        user.put("filterMale", false);
        user.put("minAge", 20);
        user.put("maxAge", 40);
        user.put("maxDistance", 69);
        user.put("friendship", false);
        user.put("relationship", true);
        when(documentReference.update(user)).thenReturn(mockVoidTask);
        matchFragmentRepository.updateFilterFields(false, true, true, false, 20,
                40, 69, false, true);
        // stub onSuccessListener and onFailureListener
        testOnSuccessListener.getValue().onSuccess(voidData);
        testOnFailureListener.getValue().onFailure(new Exception());
        verify(documentReference).update(user);
    }

    @Test
    public void calculateDistanceZeroTest(){
        double latA = 30, latB = 30, lonA = -100, lonB = -100;
        double distance = matchFragmentRepository.calculateDistance(latA, lonA, latB, lonB);
        assertEquals(0, (int) distance);
    }

    @Test
    public void calculateDistanceTest(){
        double latA = 37, latB = 35, lonA = -121, lonB = -120;
        double distance = matchFragmentRepository.calculateDistance(latA, lonA, latB, lonB);
        assertEquals(239, (int) distance);
    }

}
