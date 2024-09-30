package com.example.filmmate;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.filmmate.repositories.firebase.PhoneVerificationRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PhoneVerificationRepositoryTest {
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
    PhoneVerificationRepository phoneVerificationRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        when(firebaseAuth.getCurrentUser()).thenReturn(firebaseUser);
        when(firebaseUser.getUid()).thenReturn("3");

        // stub match collection and document
        when(firebaseFirestore.collection("users")).thenReturn(collectionReference);
        when(collectionReference.document("1")).thenReturn(documentReference);

        // stub document snapshot
        when(documentReference.get()).thenReturn(documentSnapshotTask);
        when(documentSnapshotTask.getResult()).thenReturn(documentSnapshot);

       phoneVerificationRepository = mock(PhoneVerificationRepository.class);
    }

    @Test
    public void testPhoneGet(){
        PhoneVerificationRepository phoneVerificationRepository1 = new PhoneVerificationRepository(firebaseAuth,firebaseFirestore);
        String phone = "123456";
        phoneVerificationRepository1.setPhoneNumber(phone);
        Assert.assertEquals(phone, phoneVerificationRepository1.getPhone());
    }

    @Test
    public void importPhone(){
        when(documentSnapshot.get("phone")).thenReturn("");
        doNothing().when(phoneVerificationRepository).importPhone();
    }
}
