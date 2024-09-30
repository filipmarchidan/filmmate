package com.example.filmmate;

import com.example.filmmate.repositories.firebase.ReviewsRepository;
import com.example.filmmate.repositories.firebase.TagRepository;
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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import static com.example.filmmate.RxTestUtil.setupTask;
import static com.example.filmmate.RxTestUtil.testOnCompleteListener;
import static com.example.filmmate.RxTestUtil.testOnFailureListener;
import static com.example.filmmate.RxTestUtil.testOnSuccessListener;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TagRepositoryTest {
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

    TagRepository tagRepository;

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

        tagRepository = new TagRepository(firebaseFirestore, firebaseAuth);
    }

    @Test
    public void getUserTagsTest(){
        when(firebaseAuth.getCurrentUser()).thenReturn(null);

        tagRepository.getUserTags();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        verify(documentSnapshot, times(0)).get("movieReviews");
    }

    @Test
    public void updateUserTagsTest(){
        ArrayList<String> testTags = new ArrayList<String>();
        testTags.add("Tv");

        when(documentReference.update("tags", testTags)).thenReturn(mockVoidTask);

        tagRepository.updateUserTags(testTags);

        // stub onSuccessListener
        testOnSuccessListener.getValue().onSuccess(voidData);

        verify(documentReference).update("tags", testTags);
    }

}
