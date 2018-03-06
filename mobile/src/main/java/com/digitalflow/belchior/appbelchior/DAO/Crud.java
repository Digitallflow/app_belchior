package com.digitalflow.belchior.appbelchior.DAO;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.digitalflow.belchior.appbelchior.Activity.Inicial;
import com.digitalflow.belchior.appbelchior.Activity.MainActivity;
import com.digitalflow.belchior.appbelchior.Activity.MusicActivity;
import com.digitalflow.belchior.appbelchior.Entidades.Musicas;
import com.digitalflow.belchior.appbelchior.Entidades.Usuarios;
import com.digitalflow.belchior.appbelchior.Helper.HelperAux;
import com.digitalflow.belchior.appbelchior.R;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;


/**
 * Created by MarllonS on 04/02/2018.
 */

public class Crud extends Inicial {

    public static boolean isLogin = false;
    public static Login typeLogin;

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
            musics[i] = new Musicas("Music"+ i,false);
            //hashMap.put("Music " + i, false);
        }
        //hashMap.put("uid", user.getId());
        musics[11] = new Musicas("UID", user.getId());
        return musics;
    }

    @Exclude
    public static HashMap<String,Object> toMap(Usuarios user) {
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

    public static Musicas[] hashMaptoArrayMusics(HashMap<String,Object> map){
        Musicas[] musics = new Musicas[11];
        TreeMap<String, Object> sorted = new TreeMap<>(map); //hashmap sorted
        int i = 0;
        for (Map.Entry<String, Object> pair : sorted.entrySet()) {
            musics[i] = new Musicas(pair.getKey(), pair.getValue());
            i++;
        }
        return musics;
    }

    public static void setFirMusic(Usuarios user, Musicas music){
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
    }



    public enum Login {
        email(0), facebook(1), google(2);

        private int result;

        Login(int result) {
            this.result = result;
            typeLogin = getLogin();
        }

        public Login getLogin() {
            switch (result) {
                case 0:
                    return email;
                case 1:
                    return facebook;
                case 2:
                    return google;
                default:
                    return null;
            }
        }


    }
}
