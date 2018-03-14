package com.digitalflow.belchior.appbelchior.DAO;

import android.support.annotation.NonNull;
import android.util.Log;

import com.digitalflow.belchior.appbelchior.Activity.Inicial;
import com.digitalflow.belchior.appbelchior.Entidades.Musicas;
import com.digitalflow.belchior.appbelchior.Entidades.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created by MarllonS on 04/02/2018.
 */

public class Crud extends Inicial {

    // public static Login typeLogin;
    public static final String[] titleMusics = new String[]{"Fotografia 3x4 (1976)",
                                                            "Tudo Outra Vez (1979)",
                                                            "Galos, Noites e Quintais (1977)",
                                                            "A Palo Seco (1973)",
                                                            "Alucinação (1976)",
                                                            "Divina Comédia Romântica (1978)",
                                                            "Coração Selvagem (1977)",
                                                            "Velha Roupa Colorida (1976)",
                                                            "Como Nossos Pais (1976)",
                                                            "Paralelas (1976)"};
    public static boolean isLogin = false;

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
    public static Musicas[] toMusic(Usuarios user) {
        //HashMap<String, Object> hashMap = new HashMap<>();
        Musicas[] musics = new Musicas[11];
        for (int i = 0; i < 10; i++) {
            musics[i] = new Musicas("Music" + i, false);
            //hashMap.put("Music " + i, false);
        }
        //hashMap.put("uid", user.getId());
        musics[11] = new Musicas("UID", user.getId());
        return musics;
    }

    @Exclude
    public static HashMap<String, Object> toMap(Usuarios user) {
        HashMap<String, Object> hashMap = new HashMap<>();
        for (int i = 0; i < 10; i++) {
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
        Musicas[] arrayMusics = toMusic(user);
        user.setMusic(arrayMusics);
        Usuarios.setInstance(user);
    }

    public static Musicas[] hashMaptoArrayMusics(HashMap<String, Object> map) {
        Musicas[] musics = new Musicas[10];
        TreeMap<String, Object> sorted = new TreeMap<>(map); //hashmap sorted
        int i = 0;
        for (Map.Entry<String, Object> pair : sorted.entrySet()) {
            if (i == 10) {
                continue;
            } else {
                musics[i] = new Musicas(pair.getKey(), pair.getValue());
                i++;
            }
        }
        return musics;
    }

}
