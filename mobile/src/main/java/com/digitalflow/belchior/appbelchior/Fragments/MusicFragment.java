package com.digitalflow.belchior.appbelchior.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.digitalflow.belchior.appbelchior.Activity.MainActivity;
import com.digitalflow.belchior.appbelchior.Adapter.MusicAdapter;
import com.digitalflow.belchior.appbelchior.DAO.ConfiguracaoFirebase;
import com.digitalflow.belchior.appbelchior.DAO.Crud;
import com.digitalflow.belchior.appbelchior.Entidades.Musicas;
import com.digitalflow.belchior.appbelchior.Entidades.Usuarios;
import com.digitalflow.belchior.appbelchior.Helper.HelperAux;
import com.digitalflow.belchior.appbelchior.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment {
    private ListView listViewMusic;
    private ArrayList<Musicas> musics;
    private ArrayAdapter<Musicas> adapter;
    private FirebaseUser users;
    private AlertDialog processUserDialog;
    private ProgressBar progressBar4;
    private Context context;

    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_music, container, false);

        //Montando view e adapter para popular a lista
        musics = new ArrayList<Musicas>();
        listViewMusic = (ListView) view.findViewById(R.id.listViewMusic);
        progressBar4 = (ProgressBar) view.findViewById(R.id.progressBar4);
        progressBar4.setIndeterminate(true);
        adapter = new MusicAdapter(getActivity(), musics);
        listViewMusic.setAdapter(adapter);

        //recuperar aqui algum dado no firebase ou do usuário logado

        if(Crud.isLogin){
            //exibir aqui os dados ja armazenados nas classes locais
            Usuarios user = Usuarios.getInstance();
            musics.clear();
            Collections.addAll(musics,user.getMusic());
            progressBar4.setVisibility(View.INVISIBLE);
            //adapter.notifyDataSetChanged();
        } else {
           // FirebaseAuth.getInstance().signOut();
           getUserData(inflater);
        }

        return view;

    }

    private void getUserData(LayoutInflater inflater) {
        final HelperAux helper = new HelperAux();
        users = ConfiguracaoFirebase.getFirebaseAuth().getCurrentUser();
        if (users != null) {
            processUserDialog = helper.AlertDialog(getActivity(), inflater, getString(R.string.processando), getString(R.string.msg_autenticando_dados_do_usuario), true);
            if (users.isEmailVerified()) {
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                //load user info from database to Singleton
                DocumentReference docRefUser = db.collection("users").document(users.getUid());
                docRefUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Usuarios.setInstance(documentSnapshot.toObject(Usuarios.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Console Log", "Error adding document", e);
                        processUserDialog.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), R.string.msg_erro_requisicao_falhada, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                processUserDialog.dismiss();
                                DocumentReference docRefMusic = db.collection("musics").document(users.getUid());
                                docRefMusic.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult() != null) {
                                                Usuarios user = Usuarios.getInstance();
                                                HashMap<String, Object> document =  (HashMap<String, Object>) task.getResult().getData();
                                                Musicas[] arrayMusicas = Crud.hashMaptoArrayMusics(document);
                                                user.setMusic(arrayMusicas);
                                                Usuarios.setInstance(user);
                                                musics.clear();
                                                Collections.addAll(musics, arrayMusicas);
                                                progressBar4.setVisibility(View.INVISIBLE);
                                                adapter.notifyDataSetChanged();
                                            } else {
                                                helper.AlertDialog(getActivity(), null, getString(R.string.error), getString(R.string.msg_erro_nenhum_doc_encontrado_para_esse_usuario), HelperAux.Message.msgError, false);
                                                return;
                                            }//there is no music documents by this user
                                        } else {
                                            processUserDialog.dismiss();
                                            helper.AlertDialog(getActivity(), null, getString(R.string.error), getString(R.string.msg_erro_get_documento, task.getException().toString()), HelperAux.Message.msgError, false);
                                            return;
                                        }//failed complete get document of musics
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure
                                            (@NonNull Exception e) {
                                        Toast.makeText(getActivity(), R.string.msg_erro_requisicao_falhada, Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                });//failed to get requisition
                            } else {
                                processUserDialog.dismiss();
                                helper.AlertDialog(getActivity(), null, getString(R.string.error), getString(R.string.msg_erro_nenhum_doc_encontrado_para_esse_usuario), HelperAux.Message.msgError, false);
                                return;
                            }//there is no document by this user
                        } else {
                            processUserDialog.dismiss();
                            helper.AlertDialog(getActivity(), null, getString(R.string.error), getString(R.string.msg_erro_get_documento, task.getException().toString()), HelperAux.Message.msgError, false);
                            return;
                        }//failed to get document complete on user
                    }
                });
            } else {
                processUserDialog.dismiss();
                helper.AlertDialog(getActivity(), inflater, getString(R.string.error), getString(R.string.msg_erro_email_nao_verificado), HelperAux.Message.msgError, false);
            }
        } else {
            startActivity(new Intent(getActivity(),MainActivity.class));
        }
    }

    public void getMusicsByUserLogged(){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final HelperAux helper = new HelperAux();
        //load user info from database to Singleton
        DocumentReference docRefUser = db.collection("users").document(users.getUid());
        docRefUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Usuarios.setInstance(documentSnapshot.toObject(Usuarios.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Console Log", "Error adding document", e);
                processUserDialog.dismiss();
                Toast.makeText(getActivity().getApplicationContext(), R.string.msg_erro_requisicao_falhada, Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        processUserDialog.dismiss();
                        DocumentReference docRefMusic = db.collection("musics").document(users.getUid());
                        docRefMusic.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult() != null) {
                                        Usuarios user = Usuarios.getInstance();
                                        HashMap<String, Object> document =  (HashMap<String, Object>) task.getResult().getData();
                                        Musicas[] arrayMusicas = Crud.hashMaptoArrayMusics(document);
                                        user.setMusic(arrayMusicas);
                                        Usuarios.setInstance(user);
                                        musics.clear();
                                        Collections.addAll(musics, arrayMusicas);
                                        progressBar4.setVisibility(View.INVISIBLE);
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        helper.AlertDialog(getActivity(), null, getString(R.string.error), getString(R.string.msg_erro_nenhum_doc_encontrado_para_esse_usuario), HelperAux.Message.msgError, false);
                                    }//there is no music documents by this user
                                } else {
                                    helper.AlertDialog(getActivity(), null, getString(R.string.error), getString(R.string.msg_erro_get_documento, task.getException().toString()), HelperAux.Message.msgError, false);
                                }//failed complete get document of musics
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure
                                    (@NonNull Exception e) {
                                Toast.makeText(getActivity(), R.string.msg_erro_requisicao_falhada, Toast.LENGTH_SHORT).show();
                            }
                        });//failed to get requisition
                    } else {
                        helper.AlertDialog(getActivity(), null, getString(R.string.error), getString(R.string.msg_erro_nenhum_doc_encontrado_para_esse_usuario), HelperAux.Message.msgError, false);
                    }//there is no document by this user
                } else {
                    helper.AlertDialog(getActivity(), null, getString(R.string.error), getString(R.string.msg_erro_get_documento, task.getException().toString()), HelperAux.Message.msgError, false);
                }//failed to get document complete on user
            }
        });
    }

}
