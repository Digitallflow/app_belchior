package com.digitalflow.belchior.appbelchior.DAO;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.digitalflow.belchior.appbelchior.Activity.Inicial;
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

public class Crud  {

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
