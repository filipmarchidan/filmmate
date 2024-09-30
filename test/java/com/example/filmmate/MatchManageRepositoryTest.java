package com.example.filmmate;

import android.net.Uri;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.filmmate.repositories.firebase.MatchManageRepository;
import com.example.filmmate.repositories.firebase.MatchUserRepository;
import com.example.filmmate.ui.match.adapters.CardStackAdapter;
import com.example.filmmate.ui.match.adapters.CardStackItemModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.filmmate.RxTestUtil.setupTask;
import static com.example.filmmate.RxTestUtil.testOnCompleteListener;
import static com.example.filmmate.RxTestUtil.testOnFailureListener;
import static com.example.filmmate.RxTestUtil.testOnSuccessListener;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MatchManageRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantRule = new InstantTaskExecutorRule() ;


    @Mock
    FirebaseFirestore firebaseFirestore;

    @Mock
    FirebaseUser firebaseUser;

    @Mock
    FirebaseAuth firebaseAuth;

    @Mock
    DocumentReference documentReferenceUser;

    @Mock
    DocumentReference documentReferenceMatch;

    @Mock
    DocumentReference documentReferenceChat;

    @Mock
    CollectionReference collectionReference;

    @Mock
    CollectionReference collectionChats;

    @Mock
    Task<DocumentSnapshot> documentSnapshotTaskUser;

    @Mock
    Task<DocumentSnapshot> documentSnapshotTaskMatch;

    @Mock
    Task<DocumentSnapshot> documentSnapshotTaskChat;

    @Mock
    DocumentSnapshot documentSnapshotUser;

    @Mock
    DocumentSnapshot documentSnapshotMatch;

    @Mock
    DocumentSnapshot documentSnapshotChat;

    @Mock
    private Task<Void> mockVoidTask;

    @Mock
    StorageReference storageReference;

    @Mock
    FirebaseStorage firebaseStorage;

    @Mock
    StorageReference profile;

    @Mock
    Task<Uri> uri;

    MatchManageRepository matchManageRepository;

    private Void voidData = null;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // stub firebase auth and current user id
        when(firebaseAuth.getCurrentUser()).thenReturn(firebaseUser);
        when(firebaseUser.getUid()).thenReturn("1");

        // stub user collection and document
        when(firebaseFirestore.collection("users")).thenReturn(collectionReference);
        when(collectionReference.document("1")).thenReturn(documentReferenceUser);

        // stub chat collection and document
        when(firebaseFirestore.collection("chat")).thenReturn(collectionChats);
        when(collectionChats.document("12")).thenReturn(documentReferenceChat);

        // stub match document
        when(collectionReference.document("2")).thenReturn(documentReferenceMatch);

        // stub document snapshot
        when(documentReferenceUser.get()).thenReturn(documentSnapshotTaskUser);
        when(documentSnapshotTaskUser.getResult()).thenReturn(documentSnapshotUser);
        when(documentReferenceMatch.get()).thenReturn(documentSnapshotTaskMatch);
        when(documentSnapshotTaskMatch.getResult()).thenReturn(documentSnapshotMatch);
        when(documentReferenceChat.get()).thenReturn(documentSnapshotTaskChat);
        when(documentSnapshotTaskChat.getResult()).thenReturn(documentSnapshotChat);


        when(firebaseStorage.getReference()).thenReturn(storageReference);
        when(storageReference.child("profilePictures/" + firebaseUser.getUid())).thenReturn(profile);
        when(profile.getDownloadUrl()).thenReturn(uri);
        // set up task with onCompleteListener
        setupTask(documentSnapshotTaskUser);
        setupTask(documentSnapshotTaskMatch);
        setupTask(documentSnapshotTaskChat);
        setupTask(mockVoidTask);

        matchManageRepository = new MatchManageRepository(firebaseAuth, firebaseFirestore, firebaseStorage, storageReference);
    }

    @Test
    public void getRecommendedMatchesTest() {

        List<String> recommendedMatches = Collections.singletonList("2");
        List<String> tags = Collections.singletonList("action");
        List<CardStackItemModel> result = Collections.singletonList(new CardStackItemModel(uri.getResult(), "name", "10", "bio", "2",tags));

        when(documentSnapshotUser.get("recommendedMatches")).thenReturn(recommendedMatches);
        when(documentSnapshotMatch.getString("name")).thenReturn("name");
        when(documentSnapshotMatch.getString("bio")).thenReturn("bio");
        when(documentSnapshotMatch.getLong("age")).thenReturn((long) 10);
        when(documentSnapshotMatch.exists()).thenReturn(true);
        when(documentSnapshotMatch.get("tags")).thenReturn(tags);
        when(documentSnapshotMatch.get("profileImageUrl")).thenReturn("default");

        matchManageRepository.getRecommendedMatches();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTaskUser);
        testOnCompleteListener.getValue().onComplete(documentSnapshotTaskMatch);

        assertEquals(result, matchManageRepository.getRecommendedMatchesLiveData().getValue());
    }

    @Test
    public void getRecommendedMatchesCurrentUserNullTest() {

        when(firebaseAuth.getCurrentUser()).thenReturn(null);

        matchManageRepository.getRecommendedMatches();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTaskUser);

        verify(documentSnapshotUser, times(0)).get("recommendedMatches");

    }

    @Test
    public void getRecommendedMatchesNullTest() {

        when(documentSnapshotUser.get("recommendedMatches")).thenReturn(null);

        matchManageRepository.getRecommendedMatches();

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTaskUser);

        verify(documentReferenceMatch, times(0)).get();

    }


    @Test
    public void deleteUserFromMatchTest() {
        Map<String, Object> matchUpdate = new HashMap<>();

        List<String> chatsMatch = Arrays.asList("12, 13");
        List<String> matchesMatch = Arrays.asList("1, 3");

        when(documentSnapshotMatch.get("chats")).thenReturn(chatsMatch);
        when(documentSnapshotMatch.get("matches")).thenReturn(matchesMatch);

        chatsMatch.remove("12");
        matchesMatch.remove("1");

        matchUpdate.put("chats", chatsMatch);
        matchUpdate.put("matches", matchesMatch);

        when(documentReferenceMatch.update(matchUpdate)).thenReturn(mockVoidTask);

        matchManageRepository.deleteUserFromMatch("2", "12");

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTaskMatch);
        // stub onSuccessListener and onFailureListener
        testOnSuccessListener.getValue().onSuccess(voidData);
        testOnFailureListener.getValue().onFailure(new Exception());

        verify(documentReferenceMatch).update(matchUpdate);
    }

    @Test
    public void deleteUserFromMatchNullTest() {
        Map<String, Object> matchUpdate = new HashMap<>();

        List<String> chatsMatch = null;
        List<String> matchesMatch = null;

        when(documentSnapshotMatch.get("chats")).thenReturn(chatsMatch);
        when(documentSnapshotMatch.get("matches")).thenReturn(matchesMatch);

        matchUpdate.put("chats", chatsMatch);
        matchUpdate.put("matches", matchesMatch);

        when(documentReferenceMatch.update(matchUpdate)).thenReturn(mockVoidTask);

        matchManageRepository.deleteUserFromMatch("2", "12");

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTaskMatch);
        // stub onSuccessListener and onFailureListener
        testOnSuccessListener.getValue().onSuccess(voidData);
        testOnFailureListener.getValue().onFailure(new Exception());

        verify(documentReferenceMatch).update(matchUpdate);
    }

    @Test
    public void deleteUserFromMatchCurrentUserNullTest() {
        when(firebaseAuth.getCurrentUser()).thenReturn(null);

        matchManageRepository.deleteUserFromMatch("2", "12");

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTaskMatch);

        verify(documentSnapshotMatch, times(0)).get("chats");
        verify(documentSnapshotMatch, times(0)).get("matches");
    }

    @Test
    public void deleteMatchFromUserTest() {
        Map<String, Object> userUpdate = new HashMap<>();

        List<String> chatsUser = Arrays.asList("12, 13");
        List<String> matchesUser = Arrays.asList("2, 3");

        when(documentSnapshotUser.get("chats")).thenReturn(chatsUser);
        when(documentSnapshotUser.get("matches")).thenReturn(matchesUser);

        chatsUser.remove("12");
        matchesUser.remove("2");

        userUpdate.put("chats", chatsUser);
        userUpdate.put("matches", matchesUser);

        when(documentReferenceUser.update(userUpdate)).thenReturn(mockVoidTask);

        matchManageRepository.deleteMatchFromUser("2", "12");

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTaskUser);
        // stub onSuccessListener and onFailureListener
        testOnSuccessListener.getValue().onSuccess(voidData);
        testOnFailureListener.getValue().onFailure(new Exception());

        verify(documentReferenceUser).update(userUpdate);
    }


    @Test
    public void deleteMatchFromUserNullTest() {
        Map<String, Object> userUpdate = new HashMap<>();

        List<String> chatsUser = null;
        List<String> matchesUser = null;

        when(documentSnapshotUser.get("chats")).thenReturn(chatsUser);
        when(documentSnapshotUser.get("matches")).thenReturn(matchesUser);

        userUpdate.put("chats", chatsUser);
        userUpdate.put("matches", matchesUser);

        when(documentReferenceUser.update(userUpdate)).thenReturn(mockVoidTask);

        matchManageRepository.deleteMatchFromUser("2", "12");

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTaskUser);
        // stub onSuccessListener and onFailureListener
        testOnSuccessListener.getValue().onSuccess(voidData);
        testOnFailureListener.getValue().onFailure(new Exception());

        verify(documentReferenceUser).update(userUpdate);
    }

    @Test
    public void deleteMatchFromUserCurrentUserNullTest() {
        when(firebaseAuth.getCurrentUser()).thenReturn(null);

        matchManageRepository.deleteMatchFromUser("2", "12");

        // stub onCompleteListener with documentSnapshotTask
        testOnCompleteListener.getValue().onComplete(documentSnapshotTaskUser);

        verify(documentSnapshotUser, times(0)).get("chats");
        verify(documentSnapshotUser, times(0)).get("matches");
    }

    @Test
    public void deleteMatchTest() {

        when(documentReferenceChat.delete()).thenReturn(mockVoidTask);

        matchManageRepository.deleteMatch("2", "12");

        // stub onSuccessListener and onFailureListener
        testOnSuccessListener.getValue().onSuccess(voidData);
        testOnFailureListener.getValue().onFailure(new Exception());


        verify(documentReferenceChat).delete();

    }
}
