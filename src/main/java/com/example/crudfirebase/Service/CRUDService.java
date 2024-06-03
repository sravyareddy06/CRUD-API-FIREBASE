package com.example.crudfirebase.Service;

import com.example.crudfirebase.Entity.CRUD;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class CRUDService {
    public void createCRUD(CRUD crud) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference collection = firestore.collection("users");

        // Generate a unique ID for the new data item
        String newId = UUID.randomUUID().toString();
        crud.setId(newId); // Set the generated ID to the 'id' field of the CRUD object

        // Add the CRUD object with the manually set ID to Firestore
        ApiFuture<WriteResult> writeResult = collection.document(newId).set(crud);
    }

    public void updateCRUD(CRUD updatedToDo) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = firestore.collection("users").document(updatedToDo.getId()).set(updatedToDo);
        writeResult.get();
    }

    public List<CRUD> getAllCRUD() throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference collection = firestore.collection("users");

        // Use a Query to retrieve all documents from the "users" collection
        ApiFuture<QuerySnapshot> querySnapshot = collection.get();

        List<CRUD> todoList = new ArrayList<>();

        // Iterate through the QuerySnapshot to convert each document to a CRUD object
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            CRUD todo = document.toObject(CRUD.class);
            todoList.add(todo);
        }

        return todoList;
    }

    public void deleteCRUD(String id) {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = firestore.collection("users").document(id).delete();
    }

    public CRUD getCRUD(String id) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection("users").document(id);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        CRUD crud;

        if(document.exists()){
            crud = document.toObject(CRUD.class);
            return crud;
        }
        return null;
    }
}
