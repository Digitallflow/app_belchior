package com.digitalflow.belchior.appbelchior.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.digitalflow.belchior.appbelchior.Entidades.Usuarios;
import com.digitalflow.belchior.appbelchior.R;
import com.google.android.gms.vision.text.Line;

import java.util.HashMap;
import java.util.Map;

import dyanamitechetan.vusikview.VusikView;

public class MusicActivity extends AppCompatActivity implements  MediaPlayer.OnCompletionListener{
                                                            //MediaPlayer.OnBufferingUpdateListener,


    private ImageButton btnCamera, btnPlayPause;
    private TextView txtTimer;
    private SeekBar seekBar;
    private VusikView musicView;

    private MediaPlayer mediaPlayer;
    private int mediaFileLength;
    private int realtimelength;
    final Handler handler = new Handler();

    private HashMap<String, Object> musicas_user;
    LinearLayout linearLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        btnCamera = (ImageButton) findViewById(R.id.btnCamera);
        btnPlayPause = (ImageButton) findViewById(R.id.btn_play_pause);
//        txtTimer = (TextView) findViewById(R.id.txtTimer);
//        seekBar = (SeekBar) findViewById(R.id.seekBar);
        musicView = (VusikView) findViewById(R.id.musicaView);

        Usuarios user = Usuarios.getInstance();

//        seekBar.setMax(99); // 100% (0~99)
//        seekBar.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                if(mediaPlayer.isPlaying()){
//                    SeekBar seekBar = (SeekBar)view;
//                    int playPosition = (mediaFileLength/100)*seekBar.getProgress();
//                    mediaPlayer.seekTo(playPosition);
//                }
//                return false;
//            }
//        });
        mediaPlayer = new MediaPlayer();
//        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MusicActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        musicas_user = user.getMusic();
        linearLayout = new LinearLayout(this);
        Log.d("////TESTE///////////", "Valor: " + user.getMusic());

        for(Map.Entry<String, Object> entry: musicas_user.entrySet()) {
            btnPlayPause.setTag(entry);
            Log.d("/////TESTE2///////", "VALORES: " + entry.getKey() + " : " + entry.getValue());
            if(entry.getValue().toString().equals("false")){
                btnPlayPause.setBackgroundResource(R.drawable.botao_bloqueado);
                LinearLayout.LayoutParams teste = new LinearLayout.LayoutParams(40,40);
                teste.gravity = Gravity.LEFT;
                btnPlayPause.setLayoutParams(teste);
                btnPlayPause.setEnabled(false);
            } else {
                btnPlayPause.setBackgroundResource(R.drawable.botao_desbloqueado);
                btnPlayPause.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        final ProgressDialog mDialog = new ProgressDialog(MusicActivity.this);

                        AsyncTask<String,String,String> mp3Play = new AsyncTask<String, String, String>() {

                            @Override
                            protected void onPreExecute() {
                                mDialog.setMessage("Please wait");
                                mDialog.show();
                            }

                            @Override
                            protected String doInBackground(String... params) {
                                try{
                                    mediaPlayer.setDataSource(params[0]);
                                    mediaPlayer.prepare();
                                }
                                catch (Exception ex){

                                }
                                return "";
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                mediaFileLength = mediaPlayer.getDuration();
                                realtimelength = mediaFileLength;
                                if(!mediaPlayer.isPlaying()){
                                    mediaPlayer.start();
                                    btnPlayPause.setBackgroundResource(R.drawable.ic_pause);
                                } else
                                {
                                    mediaPlayer.pause();
                                    btnPlayPause.setBackgroundResource(R.drawable.ic_play);

                                }

//                        updateSeekBar();
                                mDialog.dismiss();
                            }
                        };

                        mp3Play.execute("https://firebasestorage.googleapis.com/v0/b/appbelchior-df.appspot.com/o/02%20-%20Tudo%20outra%20vez%20(1979).mp3?alt=media&token=a20f39b2-3814-438f-8682-981ce1b833c2"); // direct link mp3 file

                        musicView.start();
                    }
                });
            }
        }

    }



//    private void updateSeekBar() {
//        seekBar.setProgress((int)(((float)mediaPlayer.getCurrentPosition() / mediaFileLength)*100));
//        if(mediaPlayer.isPlaying())
//        {
//            Runnable updater = new Runnable() {
//                @Override
//                public void run() {
//                    updateSeekBar();
//                    realtimelength-=1000; // declare 1 second
//                    txtTimer.setText(String.format("%d:%d",TimeUnit.MILLISECONDS.toMinutes(realtimelength),
//                            TimeUnit.MILLISECONDS.toSeconds(realtimelength) -
//                                    TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(realtimelength))));
//
//                }
//
//            };
//            handler.postDelayed(updater,1000); // 1 second
//        }
//    }

//    @Override
//    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//        seekBar.setSecondaryProgress(percent);
//    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        btnPlayPause.setBackgroundResource(R.drawable.ic_play);
        musicView.stopNotesFall();

    }

}
