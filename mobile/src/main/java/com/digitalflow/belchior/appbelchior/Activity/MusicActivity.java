package com.digitalflow.belchior.appbelchior.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.digitalflow.belchior.appbelchior.Adapter.TabsAdapter;
import com.digitalflow.belchior.appbelchior.Entidades.Usuarios;
import com.digitalflow.belchior.appbelchior.Fragments.CameraFragment;
import com.digitalflow.belchior.appbelchior.Fragments.MusicFragment;
import com.digitalflow.belchior.appbelchior.Helper.HelperAux;
import com.digitalflow.belchior.appbelchior.R;
import com.digitalflow.belchior.appbelchior.Util.SlidingTabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import dyanamitechetan.vusikview.VusikView;

public class MusicActivity extends HelperAux implements MediaPlayer.OnCompletionListener, HelperAux.BtnYes {
    //MediaPlayer.OnBufferingUpdateListener,

    private final Usuarios user = Usuarios.getInstance();

    private Button btnMusicas, btnCamera, btnLogout;
    private Context context = this;
    public ConstraintLayout tabConstraintLayout;
    public static boolean str;
    public static Runnable thrCallCamera;

    /* ++++ SLIDING TAB ++++ */
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    /* +++++++++++++++++++++ */

    @Override
    public void actionBtnYes(Context c) {
        FirebaseAuth.getInstance().signOut();
        c.startActivity(new Intent(c, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        btnCamera = (Button) findViewById(R.id.btnCamera);
        btnMusicas = (Button) findViewById(R.id.btnMusicas);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        tabConstraintLayout = (ConstraintLayout) findViewById(R.id.tabConstraintLayout);

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
                //
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
                //
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });

        btnMusicas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BtnYes acBtn = new MusicActivity();
                AlertDialogActivity(context,null, tabConstraintLayout, "Logout","Você tem certeza que deseja realizar logout?", Message.msgInfo, true, acBtn);
            }
        });

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


    public void updateUserMusics() {
        //Atualizar a listagem de itens do fragmento MusicFragment
        TabsAdapter newAdapter = (TabsAdapter) viewPager.getAdapter();
        MusicFragment newMusicFragment = (MusicFragment) newAdapter.getFragment(0);
        newMusicFragment.getMusicsByUserLogged();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // btnPlayPause.setBackgroundResource(R.drawable.ic_play);
        // musicView.stopNotesFall();
    }
}
