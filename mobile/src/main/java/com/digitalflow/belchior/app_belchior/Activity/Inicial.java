package com.digitalflow.belchior.app_belchior.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.WindowManager;


import com.digitalflow.belchior.app_belchior.DAO.ConfiguracaoFirebase;
import com.digitalflow.belchior.app_belchior.Helper.HelperAux;
import com.digitalflow.belchior.app_belchior.R;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.Toast;
import android.widget.VideoView;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.auth.FacebookAuthProvider;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;


public class Inicial extends HelperAux {
    //implements GoogleApiClient.OnConnectionFailedListener
    private Button btnAbrirTelaLogin;
    private Button btnAbrirTelaCadastro;
    private Button btnAbrirLoginGoogle;
    private LoginButton btbnAbrirLoginFacebook;

    private CallbackManager mCallbackManager;

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
              //  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_inicial);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        btnAbrirTelaLogin = (Button)findViewById(R.id.btnLogar);
        btnAbrirTelaCadastro = (Button)findViewById(R.id.btnCadastrar);
        btnAbrirLoginGoogle = (Button)findViewById(R.id.btnLoginGoogle);
        mCallbackManager = CallbackManager.Factory.create();
        btbnAbrirLoginFacebook = (LoginButton)findViewById(R.id.btnLoginFacebook);

        btbnAbrirLoginFacebook.setReadPermissions("email","public_profile");

        btbnAbrirLoginFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
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

        btnAbrirTelaLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intentAbrirTelaLogin = new Intent(Inicial.this, LoginActivity.class);
                startActivity(intentAbrirTelaLogin);
            }
        });

        btnAbrirTelaCadastro.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intentAbrirTelaCadastro = new Intent(Inicial.this, CadastroActivity.class);
                startActivity(intentAbrirTelaCadastro);
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
//        autenticacao.signOut();
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
//        autenticacao.signInWithCredential(credential)
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
    private void firebaseLogin(AccessToken token){
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        autenticacao.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(Inicial.this, MainActivity.class));
                            finish();
                        }else {
                            Toast.makeText(Inicial.this, "Falha na autenticação", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
