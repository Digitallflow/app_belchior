package com.digitalflow.belchior.appbelchior.Activity;

import android.app.AlertDialog;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;


import com.digitalflow.belchior.appbelchior.DAO.ConfiguracaoFirebase;
import com.digitalflow.belchior.appbelchior.DAO.Crud;
import com.digitalflow.belchior.appbelchior.Entidades.Usuarios;
import com.digitalflow.belchior.appbelchior.Helper.Base64Custom;
import com.digitalflow.belchior.appbelchior.Helper.HelperAux;
import com.digitalflow.belchior.appbelchior.Helper.Preferencias;
import com.digitalflow.belchior.appbelchior.R;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static android.view.View.INVISIBLE;


public class Inicial extends HelperAux {
    public AlertDialog dialogLogin, dialogCadastro;
    //implements GoogleApiClient.OnConnectionFailedListener
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
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_inicial);

        //user = Usuarios.getInstance();

        /* +++++++++++ FIREBASE CONFIGURATION +++++++++++ */
        auth = ConfiguracaoFirebase.getFirebaseAuth();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        /* ++++++++++++++++++++++++++++++++++++++++++++++ */

        btnAbrirTelaLogin = (Button) findViewById(R.id.btnLogin);
        btnAbrirTelaCadastro = (Button) findViewById(R.id.btnCadastrar);
        btnAbrirLoginGoogle = (Button) findViewById(R.id.btnLoginGoogle);
        mCallbackManager = CallbackManager.Factory.create();
        btnCustomFB = (Button) findViewById(R.id.btnCustomFb);
        textViewPoliticPrivacity = (TextView) findViewById(R.id.textViewPoliticPrivacity);
        textViewTermsOfUse = (TextView) findViewById(R.id.textViewTermsOfUse);
        mainConstraintLayout = (ConstraintLayout) findViewById(R.id.mainConstraintLayout);

        //Define back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseLogin(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                //fazer exceçoes
            }
        });

        btnCustomFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(Inicial.this, Arrays.asList("public_profile", "email", "user_friends"));
            }
        });

        btnAbrirTelaLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Inicial.this);
                View mView = getLayoutInflater().inflate(R.layout.activity_login, null);

                edtEmail = (EditText) mView.findViewById(R.id.edtEmail);
                edtSenha = (EditText) mView.findViewById(R.id.edtCadSenha);
                textViewCadastro = (TextView) mView.findViewById(R.id.textViewCadastro);
                textViewEsqueci = (TextView) mView.findViewById(R.id.textViewEsqueci);
                btnLogin = (Button) mView.findViewById(R.id.btnLogin);

                mBuilder.setView(mView);
                dialogLogin = mBuilder.create();
                dialogLogin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
                fadeViews(mainConstraintLayout, dialogLogin);
                dialogLogin.show();

                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(edtSenha.getWindowToken(), 0);
                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(edtEmail.getWindowToken(), 0);
                        final AlertDialog processDialog = AlertDialog(Inicial.this, getString(R.string.processando), getString(R.string.msg_autenticando_dados_do_usuario), "", true);

                        if (!edtEmail.getText().toString().equals("") && !edtSenha.getText().toString().equals("")) {
                            auth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if (auth.getCurrentUser().isEmailVerified()) {
                                            //load user info from database to Singleton
                                            DocumentReference docRef = db.collection("users").document(auth.getUid());
                                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    Usuarios user = Usuarios.getInstance();
                                                    Usuarios.setInstance(documentSnapshot.toObject(Usuarios.class));
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("Console Log", "Error adding document", e);
                                                    Toast.makeText(Inicial.this, R.string.msg_erro_requisicao_falhada, Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    Usuarios u = Usuarios.getInstance();
                                                    Toast.makeText(Inicial.this, u.getPass(), Toast.LENGTH_SHORT).show();
                                                    openActivity(HomeActivity.class, processDialog);
                                                }
                                            });
                                        } else {
                                            processDialog.dismiss();
                                            AlertDialog(Inicial.this, getString(R.string.error), getString(R.string.msg_erro_email_nao_verificado), Message.msgError, false);
                                        }
                                    } else {
                                        processDialog.dismiss();
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
                                            stringException = getString(R.string.msg_erro_email_nao_existe);
                                        } catch (Exception e) {
                                            stringException = getString(R.string.msg_erro_cadastro);
                                            e.printStackTrace();
                                        }
                                        AlertDialog(Inicial.this, getString(R.string.error), stringException, Message.msgError, false);
                                    }
                                }
                            });
                        } else {
                            processDialog.dismiss();
                            Toast.makeText(Inicial.this, R.string.msg_erro_preencha_email_e_senha, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                textViewCadastro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogLogin.dismiss();
                        fadeViews(mainConstraintLayout, dialogLogin);
                        btnAbrirTelaCadastro.callOnClick();
                    }
                });

                textViewEsqueci.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogLogin.dismiss();
                        fadeViews(mainConstraintLayout, dialogLogin);
                        AlertDialog(Inicial.this, auth);
//
//                       String x = auth.getCurrentUser().getEmail();
                    }
                });
            }
        });

        btnAbrirTelaCadastro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Inicial.this);
                View mView = getLayoutInflater().inflate(R.layout.activity_cadastro, null);

                edtCadEmail = (EditText) mView.findViewById(R.id.edtCadEmail);
                edtCadSenha = (EditText) mView.findViewById(R.id.edtCadSenha);
                edtCadConfirmarSenha = (EditText) mView.findViewById(R.id.edtCadConfirmarSenha);
                edtCadNome = (EditText) mView.findViewById(R.id.edtCadNome);
                edtCadSobrenome = (EditText) mView.findViewById(R.id.edtCadSobrenome);
                edtCadNascimento = (EditText) mView.findViewById(R.id.edtCadNasc);
                rgSexo = (RadioGroup) findViewById(R.id.rgSexo);
                rbMasculino = (RadioButton) mView.findViewById(R.id.rbMasculino);
                rbFeminino = (RadioButton) mView.findViewById(R.id.rbFeminino);
                btnCadastrar = (Button) mView.findViewById(R.id.btnCadastrar);
                loginConstraintLayout = mView.findViewById(R.id.loginConstraintLayout);

                mBuilder.setView(mView);
                dialogCadastro = mBuilder.create();
                dialogCadastro.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
                fadeViews(mainConstraintLayout, dialogCadastro);
                dialogCadastro.show();

                btnCadastrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText[] edits = {edtCadConfirmarSenha, edtCadEmail, edtCadNascimento, edtCadNome, edtCadSenha, edtCadSobrenome};
                        if (isEmpty(edits, Inicial.this)) {
                            return;
                        }

                        if (edtCadSenha.getText().toString().equals(edtCadConfirmarSenha.getText().toString())) {
                            final AlertDialog processDialog = AlertDialog(Inicial.this, getString(R.string.processando), "cadastrando usuario", "", true);

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
                                        Crud.setUser(user);
                                        Usuarios.setInstance(user);

                                        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    fadeViews(mainConstraintLayout,dialogCadastro);
                                                    processDialog.dismiss();
                                                    dialogCadastro.dismiss();
                                                    AlertDialog(Inicial.this,getString(R.string.email_enviado_title), getString(R.string.msg_info_um_email_foi_enviado, user.getEmail()), Message.msgInfo,false);
                                                } else {
                                                    Toast.makeText(Inicial.this, R.string.msg_erro_email_nao_existente_para_autenticacao, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        processDialog.dismiss();
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
                                        AlertDialog(Inicial.this, getString(R.string.error), stringException, Message.msgError, false);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(Inicial.this, R.string.msg_erro_senhas_nao_conferem, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        textViewPoliticPrivacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent para politica de privacidade
                AlertDialog(Inicial.this, getString(R.string.politica_de_privacidade), getString(R.string.politica_de_privacidade_texto), Message.popUpMsg, false);
            }
        });

        textViewTermsOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent para termos de uso
                AlertDialog(Inicial.this, getString(R.string.termos_de_servico), getString(R.string.termos_de_uso_texto), Message.popUpMsg, false);
            }
        });


//        btnAbrirLoginGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signIn();
//            }
//        });
//        findViewById(R.id.btnLogoutGoogle).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signOut();
//            }
//        });
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();

    } //onCreate

//    private void signIn(){
//        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(intent, 1);
//    }
//    private void signOut(){
//        auth.signOut();
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
//            @Override
//            public void onResult(@NonNull Status status) {
//                Toast.makeText(Inicial.this, "conta desconectada", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1){
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//
//            if (result.isSuccess()){
//                GoogleSignInAccount account = result.getSignInAccount();
//                firebaseLogin(account);
//            }
//        }
//    }
//    private void firebaseLogin(GoogleSignInAccount account){
//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//        auth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            startActivity(new Intent(Inicial.this, MainActivity.class));
//                            finish();
//                        }else {
//                            Toast.makeText(Inicial.this, "Falha na autenticação", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Toast.makeText(Inicial.this, "Falha na autenticação", Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void firebaseLogin(final AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    try {
                                        Usuarios user = Usuarios.getInstance();
                                        user.setId(auth.getUid());
                                        user.setFbId("https://facebook.com/" + object.getString("id"));
                                        user.setEmail(object.getString("email"));
                                        user.setFirstName(object.getString("first_name"));
                                        user.setLastName(object.getString("last_name"));
                                        Crud.setUser(user);
                                        Usuarios.setInstance(user);
                                        openActivity(HomeActivity.class);
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
                            //task.getException();
                            //Toast.makeText(Inicial.this, "Falha na autenticação" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
