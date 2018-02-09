package com.digitalflow.belchior.appbelchior.Entidades;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.digitalflow.belchior.appbelchior.DAO.ConfiguracaoFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by RUTH on 19/12/2017.
 */

public class Usuarios {
    private String id, email, pass, firstName, lastName, birth, sex, fbId, gId;

    private volatile static Usuarios instance = new Usuarios();

    private Usuarios() {

    }

//    public Usuarios(String id, String email, String pass, String firstName,
//                    String lastName, String birth, String sex, String fbId,
//                    String gId) {
//        this.id = id;
//        this.email = email;
//        this.pass = pass;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.birth = birth;
//        this.sex = sex;
//        this.fbId = fbId;
//        this.gId = gId;
//    }
//
//    public Usuarios(String email, String pass, String firstName,
//                    String lastName, String birth, String sex) {
//        this.email = email;
//        this.pass = pass;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.birth = birth;
//        this.sex = sex;
//    }

    public static Usuarios getInstance() {
//        if (instance == null) {
//            instance = new Usuarios();
//        }
        return instance;
    }

    public static void setInstance(Usuarios user){
        instance = user;
    }
    public void salvar() {
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("usuario").child(String.valueOf(getId())).setValue(this);
    }


//    @Exclude
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> hashMapUsuario = new HashMap<>();
//
//        hashMapUsuario.put("id", getId());
//        hashMapUsuario.put("email", getEmail());
//        hashMapUsuario.put("pass", getPass());
//        hashMapUsuario.put("firstName", getFirstName());
//        hashMapUsuario.put("lastName", getLastName());
//        hashMapUsuario.put("birth", getBirth());
//        hashMapUsuario.put("sex", getSex());
//
//        return hashMapUsuario;
//
//    }

//    public void getUser(String userId) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference docRef = db.collection("users").document(userId);
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot doc = task.getResult();
//                    Toast.makeText(getApplicationContext(), doc.getString("pass"), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    public void getUsers(String userId, final Usuarios[] user) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        //Query capitalCities = db.collection("users").whereEqualTo("id", userId);
//        DocumentReference docRef = db.collection("users").document(userId);
//        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                user[0] = documentSnapshot.toObject(Usuarios.class);
//                Log.w("laksd", "+++++++++++++++++++++++++++++ adding document++++++++++++++++++++++++++++++++++++++++++");
//                //setar aqui o singleton
//                //Toast.makeText(getApplicationContext(), user[0].getPass(), Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("CRUD", "+++++++++++++++++++++++++++++++++++++++++++not saved+++++++++++++++++++++++++++++++++++", e);
//            }
//        });
//    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String fristName) {
        this.firstName = fristName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }

    public String getFbId() {
        return fbId;
    }

    public String getgId() {
        return gId;
    }
}
