package com.example.filmmate;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;

import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.when;

/** Class taken from FrangSierra to use static methods OnCompleteListener, OnSuccessListener and OnFailureListener in tests.
 * @author FrangSierra
 * url: https://github.com/FrangSierra/RxFirebase/blob/master/app/src/test/java/durdinapps/rxfirebase2/RxTestUtil.java
 */
class RxTestUtil {

    static ArgumentCaptor<OnCompleteListener> testOnCompleteListener = ArgumentCaptor.forClass(OnCompleteListener.class);
    static ArgumentCaptor<OnSuccessListener> testOnSuccessListener = ArgumentCaptor.forClass(OnSuccessListener.class);
    static ArgumentCaptor<OnFailureListener> testOnFailureListener = ArgumentCaptor.forClass(OnFailureListener.class);
    static ArgumentCaptor<EventListener<DocumentSnapshot>> eventSnapshotListener = ArgumentCaptor.forClass(EventListener.class);

    public static <T> void setupTask(Task<T> task) {
        when(task.addOnCompleteListener(testOnCompleteListener.capture())).thenReturn(task);
        when(task.addOnSuccessListener(testOnSuccessListener.capture())).thenReturn(task);
        when(task.addOnFailureListener(testOnFailureListener.capture())).thenReturn(task);
    }

    static void setupOfflineTask(DocumentReference documentReference, ListenerRegistration registration) {
        when(documentReference.addSnapshotListener(eventSnapshotListener.capture())).thenReturn(registration);
    }
}
