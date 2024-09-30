package com.example.filmmate;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.filmmate.data.models.media.Genre;
import com.example.filmmate.repositories.firebase.GenreRepository;
import com.example.filmmate.repositories.firebase.RecommendationRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.filmmate.RxTestUtil.setupTask;
import static com.example.filmmate.RxTestUtil.testOnCompleteListener;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GenreRepositoryTest {

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

    GenreRepository genreRepository;

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

        genreRepository = new GenreRepository(firebaseAuth, firebaseFirestore);
    }

    @Test
    public void updateGenreTallyTest() {

        List<Genre> genreList = new ArrayList<>();
        genreList.add(new Genre(1, "action"));
        genreList.add(new Genre(3, "science fiction"));
        genreList.add(new Genre(4, "tv movie"));
        genreList.add(new Genre(5, "Action & Adventure"));

        when(documentSnapshot.contains("action")).thenReturn(true);
        when(documentSnapshot.contains("scienceFiction")).thenReturn(true);
        when(documentSnapshot.contains("tvMovie")).thenReturn(true);

        genreRepository.updateGenreTally(genreList, 1, 0);

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);


        verify(documentReference, times(5)).get();
    }

    @Test
    public void updateGenreTallyNotContainTest() {

        List<Genre> genreList = new ArrayList<>();
        genreList.add(new Genre(2, "Manga"));

        when(documentSnapshot.contains("manga")).thenReturn(false);

        genreRepository.updateGenreTally(genreList, 1, 0);

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);


        verify(documentSnapshot, times(1)).contains("manga");
        verify(documentReference, times(1)).get();
    }

    @Test
    public void updateGenreTallyCurrentUserNullTest() {

        List<Genre> genreList = new ArrayList<>();
        genreList.add(new Genre(1, "action"));
        genreList.add(new Genre(2, "manga"));
        genreList.add(new Genre(3, "science fiction"));
        genreList.add(new Genre(4, "tv movie"));
        genreList.add(new Genre(5, "Action & Adventure"));

        when(documentSnapshot.contains("action")).thenReturn(true);
        when(documentSnapshot.contains("manga")).thenReturn(false);
        when(documentSnapshot.contains("scienceFiction")).thenReturn(true);
        when(documentSnapshot.contains("tvMovie")).thenReturn(true);
        when(firebaseAuth.getCurrentUser()).thenReturn(null);

        genreRepository.updateGenreTally(genreList, 1, 0);

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTask);

        verify(documentSnapshot, times(0)).contains("action");

    }

}
