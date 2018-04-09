package com.digitalflow.belchior.appbelchior.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;


import com.digitalflow.belchior.appbelchior.DAO.ConfiguracaoFirebase;
import com.digitalflow.belchior.appbelchior.DAO.Crud;
import com.digitalflow.belchior.appbelchior.Entidades.Musicas;
import com.digitalflow.belchior.appbelchior.Entidades.Usuarios;
import com.digitalflow.belchior.appbelchior.Helper.Base64Custom;
import com.digitalflow.belchior.appbelchior.Helper.HelperAux;
import com.digitalflow.belchior.appbelchior.Helper.Preferencias;
import com.digitalflow.belchior.appbelchior.R;
import com.digitalflow.belchior.appbelchior.Util.MaskEditUtil;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;

import com.google.firebase.auth.FacebookAuthProvider;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;


public class Inicial extends HelperAux {
    private static final int SIGN_IN_CODE = 777;
    private final Usuarios user = Usuarios.getInstance();
    public AlertDialog dialogLogin, dialogCadastro;
    public AlertDialog processFbDialog;
    public AlertDialog processGDialog;
    public AlertDialog processDialog;
    public AlertDialog processUserDialog;
    private Button btnAbrirTelaLogin, btnAbrirTelaCadastro, btnAbrirLoginGoogle, btnCustomFB;
    private Button btnLogin;
    private Button btnCadastrar;
    private TextView textViewTermsOfUse, textViewPoliticPrivacity, textViewCadastro, textViewEsqueci;
    private ConstraintLayout mainConstraintLayout, loginConstraintLayout;
    private EditText edtEmail, edtSenha;
    private EditText edtCadEmail, edtCadSenha, edtCadConfirmarSenha, edtCadNome, edtCadSobrenome, edtCadNascimento;
    private RadioButton rbMasculino, rbFeminino, rbNaoInformado, rbOutro;
    private RadioGroup rgSexo;
    private FirebaseAuth auth;
    private CallbackManager mCallbackManager;
    private GoogleApiClient googleApiClient;
    private Context context = this;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inicial);

        /* +++++++++++ FIREBASE CONFIGURATION +++++++++++ */
        auth = ConfiguracaoFirebase.getFirebaseAuth();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        /* ++++++++++++++++++++++++++++++++++++++++++++++ */

        /* +++++++++++ LOGIN WITH GOOGLE +++++++++++ */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(context, R.string.msg_erro_conecte_a_internet_para_continuar, Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        /* ++++++++++++++++++++++++++++++++++++++++++++++ */

        btnAbrirTelaLogin = (Button) findViewById(R.id.btnLogin);
        btnAbrirTelaCadastro = (Button) findViewById(R.id.btnCadastrar);
        btnAbrirLoginGoogle = (Button) findViewById(R.id.btnLoginGoogle);
        mCallbackManager = CallbackManager.Factory.create();
        btnCustomFB = (Button) findViewById(R.id.btnCustomFb);
        textViewPoliticPrivacity = (TextView) findViewById(R.id.textViewPoliticPrivacity);
        textViewTermsOfUse = (TextView) findViewById(R.id.textViewTermsOfUse);
        mainConstraintLayout = (ConstraintLayout) findViewById(R.id.mainConstraintLayout);

        /* ++++++++++++++ Define back button ++++++++++++ */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openActivity(MainActivity.class);
                finish();
            }
        });
        /* ++++++++++++++++++++++++++++++++++++++++++++++ */

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseLoginFacebook(loginResult.getAccessToken());
                processFbDialog = AlertDialog(context, null, getString(R.string.processando), getString(R.string.msg_autenticando_dados_do_usuario), true);
            }

            @Override
            public void onCancel() {
                processFbDialog.dismiss();
            }

            @Override
            public void onError(FacebookException error) {
                processFbDialog.dismiss();
            }
        });

        btnCustomFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection(context)) {
                    return;
                }
                LoginManager.getInstance().logInWithReadPermissions(Inicial.this, Arrays.asList("public_profile", "email", "user_friends"));
            }
        });

        btnAbrirTelaLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                View mView = getLayoutInflater().inflate(R.layout.activity_login, null);

                edtEmail = (EditText) mView.findViewById(R.id.edtEmail);
                edtSenha = (EditText) mView.findViewById(R.id.edtCadSenha);
                textViewCadastro = (TextView) mView.findViewById(R.id.textViewCadastro);
                textViewEsqueci = (TextView) mView.findViewById(R.id.textViewEsqueci);
                btnLogin = (Button) mView.findViewById(R.id.btnLogin);

                mBuilder.setView(mView);
                dialogLogin = mBuilder.create();
                dialogLogin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
                fadeViews(mainConstraintLayout, dialogLogin, false);
                dialogLogin.show();

                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkConnection(context)) {
                            return;
                        }

                        ((InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(edtSenha.getWindowToken(), 0);
                        ((InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(edtEmail.getWindowToken(), 0);
                        processDialog = AlertDialog(context, null, getString(R.string.processando), getString(R.string.msg_autenticando_dados_do_usuario), true);

                        if (!edtEmail.getText().toString().equals("") && !edtSenha.getText().toString().equals("")) {
                            auth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if (auth.getCurrentUser().isEmailVerified()) {
                                            DocumentReference docRefUser = db.collection("users").document(auth.getUid());
                                            docRefUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    // Usuarios user = Usuarios.getInstance();
                                                    if(documentSnapshot.exists()) {
                                                        Usuarios.setInstance(documentSnapshot.toObject(Usuarios.class));
                                                    } else {
                                                        AlertDialogLogout(context, null, getString(R.string.error), getString(R.string.msg_erro_nenhum_doc_encontrado_para_esse_usuario), HelperAux.Message.msgError, false, new HelperAux.LogOut() {
                                                            @Override
                                                            public void logoutFirebase(AlertDialog dialog) {
                                                                dialog.dismiss();
                                                                FirebaseAuth.getInstance().signOut();
                                                                startActivity(new Intent(context, MainActivity.class));
                                                                finish();
                                                            }
                                                        });
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    //Log.w("Console Log", "Error adding document", e);
                                                    processDialog.dismiss();
                                                    Toast.makeText(context, R.string.msg_erro_requisicao_falhada, Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                            }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        if (task.getResult().exists()) {
                                                            DocumentReference docRefMusic = db.collection("musics").document(auth.getUid());
                                                            docRefMusic.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        if (task.getResult().exists()) {
                                                                            Usuarios u = Usuarios.getInstance();
                                                                            HashMap<String, Object> document = (HashMap<String, Object>) task.getResult().getData();
                                                                            Musicas[] musics = Crud.hashMaptoArrayMusics(document);
                                                                            u.setMusic(musics);
                                                                            Usuarios.setInstance(u);
                                                                            //Toast.makeText(context, u.getMusic().toString(), Toast.LENGTH_LONG).show();
                                                                            ///////////////
                                                                            Crud.isLogin = true;
                                                                            openActivityWithFlags(MusicActivity.class, processDialog);
                                                                            finish();
                                                                        } else {
                                                                            processDialog.dismiss();
                                                                            AlertDialogLogout(context, null, getString(R.string.error), getString(R.string.msg_erro_nenhum_doc_encontrado_para_esse_usuario), HelperAux.Message.msgError, false, new HelperAux.LogOut() {
                                                                                @Override
                                                                                public void logoutFirebase(AlertDialog dialog) {
                                                                                    dialog.dismiss();
                                                                                    FirebaseAuth.getInstance().signOut();
                                                                                    startActivity(new Intent(context, MainActivity.class));
                                                                                    finish();
                                                                                }
                                                                            });
                                                                        }
                                                                    } else {
                                                                        processDialog.dismiss();
                                                                        AlertDialog(context, null, getString(R.string.error), getString(R.string.msg_erro_get_documento, task.getException().toString()), HelperAux.Message.msgError, false);
                                                                    }//failed complete get document of musics
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure
                                                                        (@NonNull Exception e) {
                                                                    //Log.w("Console Log", "Error adding document", e);
                                                                    processDialog.dismiss();
                                                                    Toast.makeText(context, R.string.msg_erro_requisicao_falhada, Toast.LENGTH_SHORT).show();
                                                                }
                                                            });//failed to get requisition
                                                        } else {
                                                            processDialog.dismiss();
                                                            AlertDialogLogout(context, null, getString(R.string.error), getString(R.string.msg_erro_nenhum_doc_encontrado_para_esse_usuario), HelperAux.Message.msgError, false, new HelperAux.LogOut() {
                                                                @Override
                                                                public void logoutFirebase(AlertDialog dialog) {
                                                                    dialog.dismiss();
                                                                    FirebaseAuth.getInstance().signOut();
                                                                    startActivity(new Intent(context, MainActivity.class));
                                                                    finish();
                                                                }
                                                            });
                                                        }
                                                    } else {
                                                        processDialog.dismiss();
                                                        AlertDialog(context, null, getString(R.string.error), getString(R.string.msg_erro_get_documento, task.getException().toString()), HelperAux.Message.msgError, false);
                                                    }//failed to get document complete on user
                                                }
                                            });
                                        } else {
                                            processDialog.dismiss();
                                            AlertDialog(context, null, getString(R.string.error), getString(R.string.msg_erro_email_nao_verificado), HelperAux.Message.msgError, false);
                                        }
                                    } else {
                                        exceptionLogin(processDialog, task);
                                    }
                                }
                            });
                        } else {
                            processDialog.dismiss();
                            Toast.makeText(context, R.string.msg_erro_preencha_email_e_senha, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                textViewCadastro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fadeViews(mainConstraintLayout, dialogLogin, true);
                        btnAbrirTelaCadastro.callOnClick();
                        dialogLogin.dismiss();
                    }
                });

                textViewEsqueci.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fadeViews(mainConstraintLayout, dialogLogin, true);
                        dialogLogin.dismiss();
                        AlertDialog(context, null, auth, mainConstraintLayout, dialogLogin);
                    }
                });
            }
        });

        btnAbrirTelaCadastro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                View mView = getLayoutInflater().inflate(R.layout.activity_cadastro, null);

                edtCadEmail = (EditText) mView.findViewById(R.id.edtCadEmail);
                edtCadSenha = (EditText) mView.findViewById(R.id.edtCadSenha);
                edtCadConfirmarSenha = (EditText) mView.findViewById(R.id.edtCadConfirmarSenha);
                edtCadNome = (EditText) mView.findViewById(R.id.edtCadNome);
                edtCadSobrenome = (EditText) mView.findViewById(R.id.edtCadSobrenome);
                edtCadNascimento = (EditText) mView.findViewById(R.id.edtCadNasc);
                edtCadNascimento.addTextChangedListener(MaskEditUtil.mask(edtCadNascimento, MaskEditUtil.FORMAT_DATE));
                rgSexo = (RadioGroup) findViewById(R.id.rgSexo);
                rbMasculino = (RadioButton) mView.findViewById(R.id.rbMasculino);
                rbFeminino = (RadioButton) mView.findViewById(R.id.rbFeminino);
                btnCadastrar = (Button) mView.findViewById(R.id.btnCadastrar);
                loginConstraintLayout = mView.findViewById(R.id.loginConstraintLayout);

                mBuilder.setView(mView);
                dialogCadastro = mBuilder.create();
                dialogCadastro.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
                fadeViews(mainConstraintLayout, dialogCadastro, false);
                dialogCadastro.show();

                btnCadastrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkConnection(context)) {
                            return;
                        }

                        EditText[] edits = {edtCadConfirmarSenha, edtCadEmail, edtCadNascimento, edtCadNome, edtCadSenha, edtCadSobrenome};
                        if (isEmpty(edits, Inicial.this)) {
                            return;
                        }

                        if (edtCadSenha.getText().toString().equals(edtCadConfirmarSenha.getText().toString())) {

                            final AlertDialog processDialog = AlertDialog(context, null, getString(R.string.processando), getString(R.string.msg_cadastrando_dados_do_usuario), true);

                            auth.createUserWithEmailAndPassword(edtCadEmail.getText().toString(), edtCadSenha.getText().toString()).addOnCompleteListener(Inicial.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String userSex = rbMasculino.isChecked() ? getString(R.string.rdMasculino) : getString(R.string.rdFeminino);
                                        final Usuarios user = Usuarios.getInstance();
                                        FirebaseUser currentUser = auth.getCurrentUser();
                                        user.setId(currentUser.getUid());
                                        user.setEmail(edtCadEmail.getText().toString());
                                        user.setPass(edtCadSenha.getText().toString()); //decode string
                                        user.setFirstName(edtCadNome.getText().toString());
                                        user.setLastName(edtCadSobrenome.getText().toString());
                                        user.setBirth(edtCadNascimento.getText().toString());
                                        user.setSex(userSex);
                                        user.setMusic(null);
                                        Crud.setUser(user, context);
                                        Crud.setInitialMusics(user, context);
                                        Usuarios.setInstance(user);

                                        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    fadeViews(mainConstraintLayout, dialogCadastro, false);
                                                    processDialog.dismiss();
                                                    AlertDialog(context, null, getString(R.string.email_enviado_title), getString(R.string.msg_info_um_email_foi_enviado, user.getEmail()), Message.msgInfo, false);
                                                    dialogCadastro.dismiss();
                                                } else {
                                                    Toast.makeText(context, R.string.msg_erro_email_nao_existente_para_autenticacao, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        exceptionLogin(processDialog, task);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(context, R.string.msg_erro_senhas_nao_conferem, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        textViewPoliticPrivacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent para politica de privacidade
                AlertDialog(context, null, getString(R.string.politica_de_privacidade), getString(R.string.politica_de_privacidade_texto), mainConstraintLayout);
            }
        });

        textViewTermsOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent para termos de uso
                AlertDialog(context, null, getString(R.string.termos_de_servico), getString(R.string.termos_de_uso_texto), mainConstraintLayout);
            }
        });


        btnAbrirLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection(context)) {
                    return;
                }
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == SIGN_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                processGDialog = AlertDialog(context, null, getString(R.string.processando), getString(R.string.msg_autenticando_dados_do_usuario), true);
                GoogleSignInAccount account = result.getSignInAccount();
                if (checkConnection(context)) {
                    return;
                }
                firebaseLoginGoogle(account);
            } else {
                Toast.makeText(context, getText(R.string.msg_erro_conecte_a_internet_para_continuar), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseLoginGoogle(final GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            try {
                                FirebaseUser currentUser = auth.getCurrentUser();
                                final Usuarios u = Usuarios.getInstance();
                                u.setId(currentUser.getUid());
                                u.setEmail(acct.getEmail());
                                u.setPass(""); //decode string
                                u.setFirstName(acct.getDisplayName());
                                u.setLastName(acct.getFamilyName());
                                u.setBirth("");
                                u.setSex("");
                                u.setgId(acct.getId());
                                u.setMusic(null);
                                Crud.setUser(u, context);
                                FirebaseFirestore data = FirebaseFirestore.getInstance();
                                final Query result = data.collection("musics").whereEqualTo("uid", auth.getUid());
                                result.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            HashMap<String, Object> document = new HashMap<>();
                                            for (DocumentSnapshot doc : task.getResult()) {
                                                document = (HashMap<String, Object>) doc.getData();
                                            }
                                            if (document.size() != 0) {
                                                //faz o set das musicas na classe aqui
                                                Musicas[] musics = Crud.hashMaptoArrayMusics(document);
                                                u.setMusic(musics);
                                                Usuarios.setInstance(u);
                                                //Toast.makeText(context, user.getMusic().toString(), Toast.LENGTH_LONG).show();
                                            } else {
                                                Crud.setInitialMusics(u, context);
                                            }
                                            Crud.isLogin = true;
                                            openActivityWithFlags(MusicActivity.class, processGDialog);
                                            finish();
                                        } else {
                                            processGDialog.dismiss();
                                            AlertDialog(context, null, getString(R.string.error), getString(R.string.msg_erro_cadastro), HelperAux.Message.msgError, false);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        processGDialog.dismiss();
                                        Toast.makeText(context, getString(R.string.msg_erro_get_documento, e.toString()), Toast.LENGTH_LONG).show();
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            exceptionLogin(processGDialog, task);
                        }
                    }
                });
    }

    private void firebaseLoginFacebook(final AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    try {
                                        final Usuarios u = Usuarios.getInstance();
                                        u.setId(auth.getUid());
                                        u.setFbId("https://facebook.com/" + object.getString("id"));
                                        u.setEmail(object.getString("email"));
                                        u.setFirstName(object.getString("first_name"));
                                        u.setLastName(object.getString("last_name"));
                                        u.setMusic(null);
                                        Crud.setUser(u, context);
                                        FirebaseFirestore data = FirebaseFirestore.getInstance();
                                        final Query result = data.collection("musics").whereEqualTo("uid", auth.getUid());
                                        result.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    HashMap<String, Object> document = new HashMap<>();
                                                    for (DocumentSnapshot doc : task.getResult()) {
                                                        document = (HashMap<String, Object>) doc.getData();
                                                    }
                                                    if (document.size() != 0) {
                                                        //set das musicas na classe
                                                        Musicas[] musics = Crud.hashMaptoArrayMusics(document);
                                                        u.setMusic(musics);
                                                        Usuarios.setInstance(u);
                                                        //Toast.makeText(context, user.getMusic().toString(), Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Crud.setInitialMusics(u, context);
                                                    }
                                                    Crud.isLogin = true;
                                                    openActivityWithFlags(MusicActivity.class, processFbDialog);
                                                    finish();
                                                } else {
                                                    processFbDialog.dismiss();
                                                    AlertDialog(context, null, getString(R.string.error), getString(R.string.msg_erro_cadastro), HelperAux.Message.msgError, false);
                                                    return;
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                processFbDialog.dismiss();
                                                Toast.makeText(context, getString(R.string.msg_erro_get_documento, e.toString()), Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                        });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });

                            Bundle b = new Bundle();
                            b.putString("fields", "id,email,first_name,last_name");
                            request.setParameters(b);
                            request.executeAsync();
                        } else {
                            exceptionLogin(processFbDialog, task);
                        }
                    }
                });
    }

    public void exceptionLogin(AlertDialog alert, Task<AuthResult> task) {
        alert.dismiss();
        String stringException = "";
        try {
            throw task.getException();
        } catch (FirebaseAuthWeakPasswordException e) {
            stringException = getString(R.string.msg_erro_senha_minimo);
        } catch (FirebaseAuthInvalidCredentialsException e) {
            stringException = getString(R.string.msg_erro_senha_invalida);
        } catch (FirebaseAuthUserCollisionException e) {
            stringException = getString(R.string.msg_erro_cadastro_existente);
        } catch (FirebaseAuthInvalidUserException e) {
            stringException = getString(R.string.msg_erro_email_valido);
        } catch (Exception e) {
            stringException = getString(R.string.msg_erro_cadastro);
            e.printStackTrace();
        }
        AlertDialog(context, null, getString(R.string.error), stringException, Message.msgError, false);

    }

//    @Override
//    public void onBackPressed() {
//        openActivity(MainActivity.class);
//        finish();
//    }
}
