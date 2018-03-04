package com.digitalflow.belchior.appbelchior.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalflow.belchior.appbelchior.Adapter.TabsAdapter;
import com.digitalflow.belchior.appbelchior.DAO.ConfiguracaoFirebase;
import com.digitalflow.belchior.appbelchior.DAO.Crud;
import com.digitalflow.belchior.appbelchior.Entidades.Usuarios;
import com.digitalflow.belchior.appbelchior.Fragments.MusicFragment;
import com.digitalflow.belchior.appbelchior.Helper.HelperAux;
import com.digitalflow.belchior.appbelchior.R;
import com.digitalflow.belchior.appbelchior.Util.SlidingTabLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import dyanamitechetan.vusikview.VusikView;

public class MusicActivity extends HelperAux implements MediaPlayer.OnCompletionListener {
    //MediaPlayer.OnBufferingUpdateListener,

    final Handler handler = new Handler();
    private final Usuarios user = Usuarios.getInstance();
    LinearLayout linearLayout;
    private ImageButton btnCamera, btnPlayPause;
    private Button btnMusicas, button2;
    private TextView txtTimer;
    private SeekBar seekBar;
    private VusikView musicView;
    private MediaPlayer mediaPlayer;
    private int mediaFileLength;
    private int realtimelength;
    private HashMap<String, Object> musicas_user;
    private FirebaseUser users;
    public AlertDialog processUserDialogs;
    private Context context = this;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        btnCamera = (ImageButton) findViewById(R.id.btnCamera);
        btnPlayPause = (ImageButton) findViewById(R.id.btn_play_pause);
        btnMusicas = (Button) findViewById(R.id.btnMusicas);
        button2 = (Button) findViewById(R.id.button2);
//        txtTimer = (TextView) findViewById(R.id.txtTimer);
//        seekBar = (SeekBar) findViewById(R.id.seekBar);
        musicView = (VusikView) findViewById(R.id.musicaView);

        //abas
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slidingTabMain);
        viewPager = (ViewPager) findViewById(R.id.viewPagerMain);

        //adapter
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(tabsAdapter);
        slidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.textItemTab);
        //slidingTabLayout.setDistributeEvenly(true);
        //slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.transparent));
        slidingTabLayout.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int cameraSel = R.drawable.botao_camera;
                int cameraUns = R.drawable.botao_camera_blue;
                int musicSel = R.drawable.botao_musicas;
                int musicUns = R.drawable.botao_musicas_blue;
                switch (position) {
                    case 0:
                        btnCamera.setBackgroundResource(cameraUns);
                        btnMusicas.setBackgroundResource(musicSel);
                        break;
                    case 1:
                        btnCamera.setBackgroundResource(cameraSel);
                        btnMusicas.setBackgroundResource(musicUns);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mediaPlayer = new MediaPlayer();
//        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);


        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MusicActivity.this, HomeActivity.class);
//                startActivity(intent);

//                Toast.makeText(MusicActivity.this, user.getEmail(), Toast.LENGTH_LONG);
//                Usuarios u = Usuarios.getInstance();
//                AlertDialog(MusicActivity.this, u.getId(), u.getFirstName(), false);
                viewPager.setCurrentItem(1);
            }
        });

        btnMusicas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                openActivity(MainActivity.class);
                viewPager.setCurrentItem(0);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                openActivity(MainActivity.class);
            }
        });

      //  if (Crud.isLogin) {

       // } else {
           /* processUserDialog = AlertDialog(context, getString(R.string.processando), getString(R.string.msg_autenticando_dados_do_usuario), true);
            users = ConfiguracaoFirebase.getFirebaseAuth().getCurrentUser();
            if (users != null) {
                if (users.isEmailVerified()) {
                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                    //load user info from database to Singleton
                    DocumentReference docRefUser = db.collection("users").document(users.getUid());
                    docRefUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            // Usuarios u = Usuarios.getInstance();
                            Usuarios.setInstance(documentSnapshot.toObject(Usuarios.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Console Log", "Error adding document", e);
                            Toast.makeText(context, R.string.msg_erro_requisicao_falhada, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult() != null) {
                                    DocumentReference docRefMusic = db.collection("musics").document(users.getUid());
                                    docRefMusic.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult() != null) {
                                                    HashMap<String, Object> document = new HashMap<>();
                                                    Usuarios u = Usuarios.getInstance();
                                                    u.setMusic((HashMap<String, Object>) task.getResult().getData());
                                                    Usuarios.setInstance(u);
                                                    //final Usuarios user = Usuarios.getInstance();
                                                    Fragment fragment = new MusicFragment();
                                                    processUserDialog.dismiss();


                                                } else {
                                                    AlertDialog(context, getString(R.string.error), getString(R.string.msg_erro_nenhum_doc_encontrado_para_esse_usuario), HelperAux.Message.msgError, false);
                                                    return;
                                                }//there is no music documents by this user
                                            } else {
                                                AlertDialog(context, getString(R.string.error), getString(R.string.msg_erro_get_documento, task.getException().toString()), HelperAux.Message.msgError, false);
                                                return;
                                            }//failed complete get document of musics
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure
                                                (@NonNull Exception e) {
                                            Toast.makeText(context, R.string.msg_erro_requisicao_falhada, Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    });//failed to get requisition
                                } else {
                                    AlertDialog(context, getString(R.string.error), getString(R.string.msg_erro_nenhum_doc_encontrado_para_esse_usuario), HelperAux.Message.msgError, false);
                                    return;
                                }//there is no document by this user
                            } else {
                                AlertDialog(context, getString(R.string.error), getString(R.string.msg_erro_get_documento, task.getException().toString()), HelperAux.Message.msgError, false);
                                return;
                            }//failed to get document complete on user
                        }
                    });
                    return;
                } else {
                    AlertDialog(this, getString(R.string.error), getString(R.string.msg_erro_email_nao_verificado), HelperAux.Message.msgError, false);
                    return;
                }
            } else {
                openActivity(MainActivity.class);
            }*/
        //}


        /*musicas_user = user.getMusic();
        linearLayout = new LinearLayout(MusicActivity.this);
        Log.d("////TESTE///////////", "Valor: " + user.getMusic());

        for (Map.Entry<String, Object> entry : musicas_user.entrySet()) {
            btnPlayPause.setTag(entry);
            Log.d("/////TESTE2///////", "VALORES: " + entry.getKey() + " : " + entry.getValue());
            if (entry.getValue().toString().equals("false")) {
                btnPlayPause.setBackgroundResource(R.drawable.botao_bloqueado);
                LinearLayout.LayoutParams teste = new LinearLayout.LayoutParams(40, 40);
                teste.gravity = Gravity.LEFT;
                btnPlayPause.setLayoutParams(teste);
                btnPlayPause.setEnabled(false);
            } else {
                btnPlayPause.setBackgroundResource(R.drawable.botao_desbloqueado);
                btnPlayPause.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        final ProgressDialog mDialog = new ProgressDialog(MusicActivity.this);

                        AsyncTask<String, String, String> mp3Play = new AsyncTask<String, String, String>() {

                            @Override
                            protected void onPreExecute() {
                                mDialog.setMessage("Please wait");
                                mDialog.show();
                            }

                            @Override
                            protected String doInBackground(String... params) {
                                try {
                                    mediaPlayer.setDataSource(params[0]);
                                    mediaPlayer.prepare();
                                } catch (Exception ex) {

                                }
                                return "";
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                mediaFileLength = mediaPlayer.getDuration();
                                realtimelength = mediaFileLength;
                                if (!mediaPlayer.isPlaying()) {
                                    mediaPlayer.start();
                                    btnPlayPause.setBackgroundResource(R.drawable.ic_pause);
                                } else {
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
        }*/

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
