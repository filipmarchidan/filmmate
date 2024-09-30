package com.example.filmmate;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import com.example.filmmate.repositories.firebase.ChatScreenRepository;
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


import static com.example.filmmate.RxTestUtil.setupTask;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.when;

public class ChatScreenRepositoryTest {

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

    ChatScreenRepository chatScreenRepository;

    @Mock
    DocumentSnapshot documentSnapshot;
    private Void voidData = null;



    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // stub firebase auth and current user id
        when(firebaseAuth.getCurrentUser()).thenReturn(firebaseUser);
        when(firebaseUser.getUid()).thenReturn("1");

        // stub user collection and document
        when(firebaseFirestore.collection("chat")).thenReturn(collectionReference);
        when(collectionReference.document("testChatId")).thenReturn(documentReference);

        // stub document snapshot
        when(documentReference.get()).thenReturn(documentSnapshotTask);
        when(documentSnapshotTask.getResult()).thenReturn(documentSnapshot);


        // set up task with onCompleteListener
        setupTask(documentSnapshotTask);
        setupTask(mockVoidTask);

        chatScreenRepository = new ChatScreenRepository(firebaseFirestore, firebaseAuth);
    }

//    @Test
//    public void sendMessageTest(){
//        String currentUserId = firebaseUser.getUid();
//        Map newMessage = new HashMap();
//        Timestamp timestamp = Timestamp.now();
//        newMessage.put("sender", currentUserId);
//        newMessage.put("timestamp", timestamp);
//        newMessage.put("text", "testMessage");
//        when(documentReference.set(newMessage)).thenReturn(mockVoidTask);
//        chatScreenRepository.sendMessage("testMessage","testChatId");
//        testOnSuccessListener.getValue().onSuccess(voidData);
//        testOnFailureListener.getValue().onFailure(new Exception());
//        verify(documentReference).set(newMessage);
//    }

    @Test
    public void generateRandomStringTest(){
        String asciiUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String asciiLowerCase = asciiUpperCase.toLowerCase();
        String digits = "1234567890";
        String asciiChars = asciiUpperCase + asciiLowerCase + digits;
        String random = chatScreenRepository.generateRandomString(20, asciiChars);
        assertEquals(20, random.length());
    }


}
