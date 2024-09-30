package com.example.filmmate;

import com.example.filmmate.repositories.firebase.GeoLocationRepository;
import com.example.filmmate.repositories.firebase.ReviewsRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import static com.example.filmmate.RxTestUtil.setupTask;
import static com.example.filmmate.RxTestUtil.testOnCompleteListener;
import static com.example.filmmate.RxTestUtil.testOnFailureListener;
import static com.example.filmmate.RxTestUtil.testOnSuccessListener;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import static org.mockito.Mockito.when;

public class GeoLocationRepositoryTest {

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

    GeoLocationRepository geoLocationRepository;

    private Void voidData = null;

    @Before
    public void setup(){
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

        geoLocationRepository = new GeoLocationRepository(firebaseFirestore, firebaseAuth);
    }

    @Test
    public void updateUserLocationFirebaseTest(){
        GeoPoint geoPointTest = new GeoPoint(0.1, 0.1);

        when(documentReference.update("location", geoPointTest)).thenReturn(mockVoidTask);

        geoLocationRepository.updateUserLocationFirebase(geoPointTest);

        // stub onSuccessListener and onFailureListener
        testOnSuccessListener.getValue().onSuccess(voidData);
        testOnFailureListener.getValue().onFailure(new Exception());

        verify(documentReference).update("location", geoPointTest);
    }
}
