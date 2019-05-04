package com.digitalflow.belchior.appbelchior.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.digitalflow.belchior.appbelchior.DAO.ConfiguracaoFirebase;
import com.digitalflow.belchior.appbelchior.Entidades.Usuarios;
import com.digitalflow.belchior.appbelchior.Helper.HelperAux;
import com.digitalflow.belchior.appbelchior.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends HelperAux {
    public static boolean isVerificate = true;
    private Button AbrirTelaInicial;
    private AudioManager audioManager;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        AbrirTelaInicial = (Button) findViewById(R.id.btnConectar);
        ImageView imageViewLock = (ImageView) findViewById(R.id.iconImg);
        imageViewLock.setBackgroundResource(R.drawable.inicial_animation);
        imageViewLock.setPadding(40,40,40,40);

        AnimationDrawable mAnimation = (AnimationDrawable) imageViewLock.getBackground();
        mAnimation.start();


        findViewById(R.id.btnConectar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(Inicial.class);
               // finish();
            }
        });

        if (!isVerificate) {
            AlertDialogLogout(context, null, getString(R.string.error), getString(R.string.msg_erro_email_nao_verificado), HelperAux.Message.msgError, false, new LogOut() {
                @Override
                public void logoutFirebase(AlertDialog dialog) {
                    dialog.dismiss();
                    FirebaseAuth.getInstance().signOut();
                }
            });
        } else {
            FirebaseAuth.getInstance().signOut();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
