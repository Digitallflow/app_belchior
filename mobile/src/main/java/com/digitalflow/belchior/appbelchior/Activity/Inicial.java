package com.digitalflow.belchior.appbelchior.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;


import com.digitalflow.belchior.appbelchior.DAO.ConfiguracaoFirebase;
import com.digitalflow.belchior.appbelchior.DAO.Crud;
import com.digitalflow.belchior.appbelchior.Entidades.Usuarios;
import com.digitalflow.belchior.appbelchior.Helper.HelperAux;
import com.digitalflow.belchior.appbelchior.R;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;

import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.digitalflow.belchior.appbelchior.DAO.Crud.*;


public class Inicial extends HelperAux {
    //implements GoogleApiClient.OnConnectionFailedListener
    private Button btnAbrirTelaLogin, btnAbrirTelaCadastro, btnAbrirLoginGoogle, btnCustomFB, btnLogin;
    private TextView textViewTermsOfUse, textViewPoliticPrivacity, textViewCadastro;
    private EditText edtEmail, edtSenha;
    private FirebaseAuth auth;
    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    private Usuarios user, usuarios;
    public AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
              //  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_inicial);

        auth = ConfiguracaoFirebase.getFirebaseAuth();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        //FirebaseUser currentUser = auth.getCurrentUser();
        btnAbrirTelaLogin = (Button)findViewById(R.id.btnLogin);
        btnAbrirTelaCadastro = (Button)findViewById(R.id.btnCadastrar);
        btnAbrirLoginGoogle = (Button)findViewById(R.id.btnLoginGoogle);
        mCallbackManager = CallbackManager.Factory.create();
//        btbnAbrirLoginFacebook = (LoginButton)findViewById(R.id.btnLoginFacebook);
        btnCustomFB = (Button)findViewById(R.id.btnCustomFb);
        textViewPoliticPrivacity = (TextView)findViewById(R.id.textViewPoliticPrivacity);
        textViewTermsOfUse = (TextView)findViewById(R.id.textViewTermsOfUse);

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

            }
        });

        btnCustomFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(Inicial.this, Arrays.asList("public_profile", "email","user_friends"));
            }
        });

        btnAbrirTelaLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Inicial.this);
                View mView = getLayoutInflater().inflate(R.layout.activity_login, null);

                edtEmail = (EditText) mView.findViewById(R.id.edtEmail);
                edtSenha = (EditText)  mView.findViewById(R.id.edtSenha);
                textViewCadastro = (TextView)  mView.findViewById(R.id.textViewCadastro);
                btnLogin = (Button)  mView.findViewById(R.id.btnLogin);

                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.show();

                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!edtEmail.getText().toString().equals("") && !edtSenha.getText().toString().equals("")) {
                            auth = ConfiguracaoFirebase.getFirebaseAuth();
                            auth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()){
                                        usuarios = new Usuarios();
//                                        usuarios.setEmail(edtEmail.getText().toString());
//                                        usuarios.setPass(edtSenha.getText().toString());
//                                        usuarios.setId(auth.getUid());
//                                        Crud.setUser(usuarios);

                                        usuarios = Crud.getUsers(auth.getUid());
                                        Toast.makeText(Inicial.this, usuarios.getPass(), Toast.LENGTH_SHORT).show();
//                                        usuarios = new Usuarios();
//                                        usuarios.setEmail(edtEmail.getText().toString());
//                                        usuarios.setSenha(edtSenha.getText().toString());
//                                        usuarios.setId(auth.getUid());
//                                        Crud.setUser(usuarios);
                                        //openActivity(HomeActivity.class, dialog);
                                    } else {
                                        Toast.makeText(Inicial.this, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(Inicial.this, "Preencha os campos de email e senha! ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                textViewCadastro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openActivity(CadastroActivity.class, dialog);
                    }
                });
            }
        });

        btnAbrirTelaCadastro.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openActivity(CadastroActivity.class, dialog);
            }
        });

        textViewPoliticPrivacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent para politica de privacidade
                MyCustomAlertDialog(Inicial.this, getString(R.string.politica_de_privacidade), getString(R.string.politica_de_privacidade_texto), Message.popUpMsg, false);
            }
        });

        textViewTermsOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent para termos de uso
                MyCustomAlertDialog(Inicial.this, getString(R.string.termos_de_servico), getString(R.string.termos_de_uso_texto), Message.popUpMsg, false);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseLogin(final AccessToken token){
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            GraphRequest request = GraphRequest.newMeRequest(token,new GraphRequest.GraphJSONObjectCallback(){

                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {


                                    try {//object.toString();
                                        Toast.makeText(getApplicationContext(),object.getString("id"),Toast.LENGTH_LONG).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });

                            Bundle b = new Bundle();
                            b.putString("fields","id,email,first_name,last_name,picture.type(large)");
                            request.setParameters(b);
                            request.executeAsync();

                            FirebaseUser user = auth.getCurrentUser();

                            // update user firebase
                            // updateUI(user)
                            //finish();
                            openActivity(HomeActivity.class);
                        }else {
                            Toast.makeText(Inicial.this, "Falha na autenticação", Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }

}
