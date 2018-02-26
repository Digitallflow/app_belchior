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
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by MarllonS on 04/02/2018.
 */

public class Crud {

    public static void setUser(Usuarios user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getId()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("CRUD", "++++++++++++++++++++++++++++++++saved++++++++++++++++++++++++++++++++++++++++++++++++++");

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("CRUD", "+++++++++++++++++++++++++++++++++++++++++++not saved+++++++++++++++++++++++++++++++++++", e);
                //fazer excecoes com toast
            }

        });
    }
    @Exclude
    public static Map<String, Object> toMap(Usuarios user) {
        HashMap<String, Object> hashMap = new HashMap<>();
        for (int i=0; i<10; i++){
            hashMap.put("Music " + i, false);
        }
        hashMap.put("uid", user.getId());
        return hashMap;
    }

    public static void setInitialMusics(Usuarios user) {
        Map<String, Object> music = toMap(user);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("musics").document(user.getId()).set(music).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("CRUD", "++++++++++++++++++++++++++++++++saved++++++++++++++++++++++++++++++++++++++++++++++++++");
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("CRUD", "+++++++++++++++++++++++++++++++++++++++++++not saved+++++++++++++++++++++++++++++++++++", e);
                //fazer excecoes com toast
            }

        });
        user.setMusic((HashMap<String, Object>) music);
        Usuarios.setInstance(user);
    }

    public static void updateUser() {
        //
    }
}
