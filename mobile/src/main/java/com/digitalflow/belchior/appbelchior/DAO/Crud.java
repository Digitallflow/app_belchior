package com.digitalflow.belchior.appbelchior.DAO;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.digitalflow.belchior.appbelchior.Entidades.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by MarllonS on 04/02/2018.
 */

public class Crud extends Usuarios {

    private Usuarios user;

    public static void getUser(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    Toast.makeText(getApplicationContext(), doc.getString("pass"), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void getUsers(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Query capitalCities = db.collection("users").whereEqualTo("id", userId);
        DocumentReference docRef = db.collection("users").document(userId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Usuarios user = documentSnapshot.toObject(Usuarios.class);
                Log.w("laksd", "+++++++++++++++++++++++++++++ adding document++++++++++++++++++++++++++++++++++++++++++");
                //setar aqui o singleton
                Toast.makeText(getApplicationContext(), user.getPass(), Toast.LENGTH_SHORT).show();
            }
        })
         .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("laksd", "Error adding document", e);
            }
        });


    }

     public static void setUser(Usuarios user) {
         FirebaseFirestore db = FirebaseFirestore.getInstance();
         db.collection("users").document(user.getId()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 Log.d("CRUD","++++++++++++++++++++++++++++++++saved++++++++++++++++++++++++++++++++++++++++++++++++++");
             }

         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Log.d("CRUD","+++++++++++++++++++++++++++++++++++++++++++not saved+++++++++++++++++++++++++++++++++++", e);
             }

         });
     }

     public static void updateUser(){
        //
     }
}
