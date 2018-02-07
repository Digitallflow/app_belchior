package com.digitalflow.belchior.appbelchior.Activity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.VideoView;

import com.digitalflow.belchior.appbelchior.Helper.HelperAux;
import com.digitalflow.belchior.appbelchior.R;

public class MainActivity extends HelperAux {
    private Button AbrirTelaInicial;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        AbrirTelaInicial = (Button)findViewById(R.id.btnConectar);

        findViewById(R.id.btnConectar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(Inicial.class);
//                Intent intentAbrirTelaInicial = new Intent(MainActivity.this, Inicial.class);
//                startActivity(intentAbrirTelaInicial);
            }
        });

        Slide slide = new Slide();
//        getWindow().setExitTransition(slide);


    }

    @Override
    protected void onStart() {
        super.onStart();
        VideoView videoViewbg = findViewById(R.id.videoViewBg);

//        Uri uri = Uri.parse();

        videoViewbg.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bg_video));
        videoViewbg.setOnPreparedListener (new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.setVolume(0,0);
            }
        });
        videoViewbg.start();
    }
}
