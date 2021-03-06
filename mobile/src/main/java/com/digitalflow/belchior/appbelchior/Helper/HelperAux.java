package com.digitalflow.belchior.appbelchior.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalflow.belchior.appbelchior.Activity.Inicial;
import com.digitalflow.belchior.appbelchior.Activity.MainActivity;
import com.digitalflow.belchior.appbelchior.Activity.MusicActivity;
import com.digitalflow.belchior.appbelchior.MediaService.NotificationGenerator;
import com.digitalflow.belchior.appbelchior.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by MarllonS on 25/01/2018.
 */

public class HelperAux extends AppCompatActivity {
    static final android.os.Handler handler = new android.os.Handler();
    private static final boolean[] repeat = new boolean[1];
    public static MediaPlayer mediaPlayer;
    public static View rootView;
    public static Context inContext;
    public static String titleMusic;
    static Runnable updater;
    private static volatile boolean stopUpdater;
    private static NotificationManager notification;
    private int totalDuration;

    public boolean checkConnection(Context context){
        Resources res = context.getResources();
        try {
            if (!isInternetAvailable()){
                Toast.makeText(context, res.getString(R.string.msg_erro_conecte_a_internet_para_continuar), Toast.LENGTH_SHORT).show();
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, res.getString(R.string.msg_erro_reinicie_o_app), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public static boolean isInternetAvailable() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec (command).waitFor() == 0);
    }

    public static void updateBigNotification(MediaPlayer mediaPlayer, boolean songRepeat) {
        if (mediaPlayer.isPlaying()) {
            notification = NotificationGenerator.customBigNotification(inContext, R.drawable.icon_belchior_notification, R.drawable.icon_belchior_laucher, titleMusic, titleMusic, titleMusic, mediaPlayer, false, songRepeat,
                    new NotificationGenerator.IntentNotification() {
                        @Override
                        public Intent setIntent() {
                            Intent intent = new Intent(inContext, MusicActivity.class);
                            return intent;
                        }
                    }, new NotificationGenerator.OnGoingNotification() {
                        @Override
                        public int setNotificationOnGoing(MediaPlayer mediaPlayer) {
                            if (mediaPlayer.isPlaying()) {
                                return Notification.FLAG_NO_CLEAR;
                            } else {
                                return Notification.FLAG_AUTO_CANCEL;
                            }
                        }
                    });
        } else {
            notification = NotificationGenerator.customBigNotification(inContext, R.drawable.icon_belchior_notification, R.drawable.icon_belchior_laucher, titleMusic, titleMusic, titleMusic, mediaPlayer, true, songRepeat,
                    new NotificationGenerator.IntentNotification() {
                        @Override
                        public Intent setIntent() {
                            Intent intent = new Intent(inContext, MusicActivity.class);
                            return intent;
                        }
                    }, new NotificationGenerator.OnGoingNotification() {
                        @Override
                        public int setNotificationOnGoing(MediaPlayer mediaPlayer) {
                            if (mediaPlayer.isPlaying()) {
                                return Notification.FLAG_NO_CLEAR;
                            } else {
                                return Notification.FLAG_AUTO_CANCEL;
                            }
                        }
                    });
        }
    }

    private static void stopThread() {
        stopUpdater = true;
    }

    private static void reRunThread() {
        stopUpdater = false;
    }

    private static void updateSeekBar(final MediaPlayer mp, final SeekBar seekBar, final TextView textViewInit, final TextView textViewFinal, final TextView textViewTitle, Resources res, int totalDuration) {
        seekBar.setProgress(mp.getCurrentPosition());
        textViewInit.setText(milliSecondsToTimer(mp.getCurrentPosition()));
        String remainDuration = milliSecondsToTimer(mp.getDuration() - mp.getCurrentPosition());
        textViewFinal.setText(remainDuration != "0:00" ? "-" + remainDuration : remainDuration);
        setStateMusic(mediaPlayer, textViewTitle, res);
        handler.postDelayed(updater, 1000);
    }

    private static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    private static void setStateMusic(final MediaPlayer mediaPlayer, final TextView textViewTitleMusic, final Resources res) {
        final boolean[] play = {false};
        final String a = res.getString(R.string.reproduzindo, titleMusic);
        textViewTitleMusic.post(new Runnable() {
            @Override
            public void run() {
                if (textViewTitleMusic.getText().toString().equals(a)) {
                    play[0] = true;
                }
                if (mediaPlayer.isPlaying() && (!play[0])) {
                    textViewTitleMusic.setText(res.getString(R.string.reproduzindo, titleMusic));
                } else if (!mediaPlayer.isPlaying() && (play[0])) {
                    textViewTitleMusic.setText(res.getString(R.string.pausado, titleMusic));
                }
            }
        });

    }

    public void openActivity(Class<?> cls, AlertDialog dialog) {
        Intent intent = new Intent(getBaseContext(), cls);
        startActivity(intent);
        dialog.dismiss();
    }

    public void openActivityWithFlags(Class<?> cls, AlertDialog dialog) {
        Intent intent = new Intent(getBaseContext(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        dialog.dismiss();
    }

    public void openActivityWithFlags(Class<?> cls) {
        Intent intent = new Intent(getBaseContext(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void openActivity(Class<?> cls) {
        Intent intent = new Intent(getApplicationContext(), cls);
        startActivity(intent);
    }

    public void openActivity(Class<?> cls, Context context) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }

    public void fadeButtons(final ConstraintLayout layout, AlertDialog dialog, final boolean overlayViews) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View v = layout.getChildAt(i);
            if (v instanceof Button) {
                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_out));
                v.setVisibility(View.INVISIBLE);
                //v.setVisibility(View.GONE); //Or View.INVISIBLE to keep its bounds
            }
        }
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!overlayViews) {
                    for (int i = 0; i < layout.getChildCount(); i++) {
                        final View v = layout.getChildAt(i);
                        if (v instanceof Button) {
                            v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in));
                            v.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }
        });
    }

    public void fadeViews(final ConstraintLayout layout, AlertDialog dialog, boolean overlayViews) {
        layout.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_out));
        layout.setVisibility(View.INVISIBLE);
        if (!overlayViews) {
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    layout.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in));
                    layout.setVisibility(View.VISIBLE);
                }
            });
        } else {
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });
        }
    }

    public AlertDialog AlertDialog(Context inContext, LayoutInflater inflater, String title, String line1, boolean processing) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(inContext);
        View mView = null;
        if (inflater == null) {
            mView = getLayoutInflater().inflate(R.layout.activity_layout, null);
        } else {
            mView = inflater.inflate(R.layout.activity_layout, null);
        }

        ProgressBar progressBar2 = (ProgressBar) mView.findViewById(R.id.progressBar2);
        TextView textViewTitle = (TextView) mView.findViewById(R.id.textViewTitle);
        TextView textViewLine1 = (TextView) mView.findViewById(R.id.textViewLine1);
        ImageView imageViewLock = (ImageView) mView.findViewById(R.id.imageViewLock);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) progressBar2.getLayoutParams();
        ConstraintLayout.LayoutParams paramsTitle = (ConstraintLayout.LayoutParams) textViewTitle.getLayoutParams();

        if (processing) {
            params.setMarginStart(32);
            dialog.setCancelable(false);
            imageViewLock.setVisibility(View.GONE);
            textViewTitle.setText(title);
            paramsTitle.setMargins(textViewTitle.getLeft(), 32, textViewTitle.getRight(), textViewTitle.getBottom());
            textViewLine1.setText(line1);
            //openActivity(toCls);
        } else {
            params.setMarginStart(0);
            paramsTitle.setMargins(textViewTitle.getLeft(), 12, textViewTitle.getRight(), textViewTitle.getBottom());
            progressBar2.setVisibility(View.INVISIBLE);
            textViewTitle.setText(title);
            textViewLine1.setText(line1);
            imageViewLock.setBackgroundResource(R.drawable.lock_animation);

            AnimationDrawable mAnimation = (AnimationDrawable) imageViewLock.getBackground();
            mAnimation.start();
        }
        progressBar2.setLayoutParams(params);
        dialog.show();

        return dialog;


    }

    public void AlertDialog(Context context, LayoutInflater inflater, String title, String subtitle, final ConstraintLayout layout) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = null;
        if (inflater == null) {
            mView = getLayoutInflater().inflate(R.layout.aux_helper, null);
        } else {
            mView = inflater.inflate(R.layout.aux_helper, null);
        }
        TextView textViewTitle = (TextView) mView.findViewById(R.id.textViewTitle);
        TextView textViewSubTitle = (TextView) mView.findViewById(R.id.textViewSubTitle);
        Button btnYes = (Button) mView.findViewById(R.id.btnYes);
        Button btnNo = (Button) mView.findViewById(R.id.btnNo);
        ImageView imageViewType = (ImageView) mView.findViewById(R.id.imageViewType);

        imageViewType.setVisibility(View.INVISIBLE);
        imageViewType.getLayoutParams().height = 0;
        imageViewType.getLayoutParams().width = 0;
        btnYes.setVisibility(View.INVISIBLE);
        btnNo.setText(R.string.ok);
        textViewTitle.setText(title);
        textViewSubTitle.setText(subtitle);
        layout.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_out));
        layout.setVisibility(View.INVISIBLE);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
        dialog.setCancelable(false);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                layout.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in));
                layout.setVisibility(View.VISIBLE);

            }
        });

        dialog.show();

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //return dialog;
    }

    public void AlertDialogLogout(final Context context, LayoutInflater inflater, String title, String subtitle, Message msgType, boolean yesNo, final LogOut logout) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = null;
        Resources res = context.getResources();
        if (inflater == null) {
            mView = getLayoutInflater().inflate(R.layout.aux_helper, null);
        } else {
            mView = inflater.inflate(R.layout.aux_helper, null);
        }

        TextView textViewTitle = (TextView) mView.findViewById(R.id.textViewTitle);
        TextView textViewSubTitle = (TextView) mView.findViewById(R.id.textViewSubTitle);
        Button btnYes = (Button) mView.findViewById(R.id.btnYes);
        Button btnNo = (Button) mView.findViewById(R.id.btnNo);
        ImageView imageViewType = (ImageView) mView.findViewById(R.id.imageViewType);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
        dialog.setCancelable(false);
        switch (msgType) {
            case msgDone:
                imageViewType.setBackgroundResource(R.drawable.done_white);
                if (yesNo) {
                    btnYes.setVisibility(View.VISIBLE);
                    btnNo.setVisibility(View.VISIBLE);
                    btnYes.setText(R.string.yes);
                    btnNo.setText(R.string.no);
                } else {
                    btnYes.setVisibility(View.INVISIBLE);
                    btnNo.setText(R.string.ok);
                }
                textViewTitle.setText(res.getString(R.string.sucesso, title));
                textViewSubTitle.setText(subtitle);
                break;
            case msgInfo:
                imageViewType.setBackgroundResource(R.drawable.info_white);
                if (yesNo) {
                    btnYes.setVisibility(View.VISIBLE);
                    btnNo.setVisibility(View.VISIBLE);
                    btnYes.setText(R.string.yes);
                    btnNo.setText(R.string.no);
                } else {
                    btnYes.setVisibility(View.INVISIBLE);
                    btnNo.setText(R.string.ok);
                }
                textViewTitle.setText(getString(R.string.info, title));
                textViewSubTitle.setText(subtitle);
                break;
            case msgError:
                imageViewType.setBackgroundResource(R.drawable.error_white);
                if (yesNo) {
                    btnYes.setVisibility(View.VISIBLE);
                    btnNo.setVisibility(View.VISIBLE);
                    btnYes.setText(R.string.yes);
                    btnNo.setText(R.string.no);
                } else {
                    btnYes.setVisibility(View.INVISIBLE);
                    btnNo.setText(R.string.ok);
                }
                textViewTitle.setText(title);
                textViewSubTitle.setText(subtitle);
                break;
            case msgWarning:
                imageViewType.setBackgroundResource(R.drawable.warning_white);
                if (yesNo) {
                    btnYes.setVisibility(View.VISIBLE);
                    btnNo.setVisibility(View.VISIBLE);
                    btnYes.setText(R.string.yes);
                    btnNo.setText(R.string.no);
                } else {
                    btnYes.setVisibility(View.INVISIBLE);
                    btnNo.setText(R.string.ok);
                }
                textViewTitle.setText(res.getString(R.string.warning, title));
                textViewSubTitle.setText(subtitle);
                break;
            case msgQuestion:
                imageViewType.setBackgroundResource(R.drawable.question_white);
                if (yesNo) {
                    btnYes.setVisibility(View.VISIBLE);
                    btnNo.setVisibility(View.VISIBLE);
                    btnYes.setText(R.string.yes);
                    btnNo.setText(R.string.no);
                } else {
                    btnYes.setVisibility(View.INVISIBLE);
                    btnNo.setText(R.string.ok);
                }
                textViewTitle.setText(res.getString(R.string.info, title));
                textViewSubTitle.setText(subtitle);
                break;
            case popUpMsg:
                imageViewType.setVisibility(View.INVISIBLE);
                imageViewType.getLayoutParams().height = 0;
                imageViewType.getLayoutParams().width = 0;
                if (yesNo) {
                    btnYes.setVisibility(View.VISIBLE);
                    btnNo.setVisibility(View.VISIBLE);
                    btnYes.setText(R.string.yes);
                    btnNo.setText(R.string.no);
                } else {
                    btnYes.setVisibility(View.INVISIBLE);
                    btnNo.setText(R.string.ok);
                }
                textViewTitle.setText(title);
                textViewSubTitle.setText(subtitle);
                break;
            default:
                textViewSubTitle.setText(R.string.error);
                textViewTitle.setText(R.string.error);
        }

        dialog.show();

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout.logoutFirebase(dialog);
                //startActivity(new Intent(activity, Inicial.class));
            }
        });
    }

    public void AlertDialogActivity(final Context context, LayoutInflater inflater, final ConstraintLayout constraintLayout, String title, String subtitle, Message msgType, boolean yesNo, final BtnYes btn) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = null;
        if (inflater == null) {
            mView = getLayoutInflater().inflate(R.layout.aux_helper, null);
        } else {
            mView = inflater.inflate(R.layout.aux_helper, null);
        }

        TextView textViewTitle = (TextView) mView.findViewById(R.id.textViewTitle);
        TextView textViewSubTitle = (TextView) mView.findViewById(R.id.textViewSubTitle);
        Button btnYes = (Button) mView.findViewById(R.id.btnYes);
        Button btnNo = (Button) mView.findViewById(R.id.btnNo);
        ImageView imageViewType = (ImageView) mView.findViewById(R.id.imageViewType);
        mBuilder.setView(mView);

        constraintLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out));
        constraintLayout.setVisibility(View.INVISIBLE);

        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
        dialog.setCancelable(false);
        switch (msgType) {
            case msgDone:
                imageViewType.setBackgroundResource(R.drawable.done_white);
                if (yesNo) {
                    btnYes.setVisibility(View.VISIBLE);
                    btnNo.setVisibility(View.VISIBLE);
                    btnYes.setText(R.string.yes);
                    btnNo.setText(R.string.no);
                } else {
                    btnYes.setVisibility(View.INVISIBLE);
                    btnNo.setText(R.string.ok);
                }
                textViewTitle.setText(getString(R.string.sucesso, title));
                textViewSubTitle.setText(subtitle);
                break;
            case msgInfo:
                imageViewType.setBackgroundResource(R.drawable.info_white);
                if (yesNo) {
                    btnYes.setVisibility(View.VISIBLE);
                    btnNo.setVisibility(View.VISIBLE);
                    btnYes.setText(R.string.yes);
                    btnNo.setText(R.string.no);
                } else {
                    btnYes.setVisibility(View.INVISIBLE);
                    btnNo.setText(R.string.ok);
                }
                textViewTitle.setText(getString(R.string.info, title));
                textViewSubTitle.setText(subtitle);
                break;
            case msgError:
                imageViewType.setBackgroundResource(R.drawable.error_white);
                if (yesNo) {
                    btnYes.setVisibility(View.VISIBLE);
                    btnNo.setVisibility(View.VISIBLE);
                    btnYes.setText(R.string.yes);
                    btnNo.setText(R.string.no);
                } else {
                    btnYes.setVisibility(View.INVISIBLE);
                    btnNo.setText(R.string.ok);
                }
                textViewTitle.setText(title);
                textViewSubTitle.setText(subtitle);
                break;
            case msgWarning:
                imageViewType.setBackgroundResource(R.drawable.warning_white);
                if (yesNo) {
                    btnYes.setVisibility(View.VISIBLE);
                    btnNo.setVisibility(View.VISIBLE);
                    btnYes.setText(R.string.yes);
                    btnNo.setText(R.string.no);
                } else {
                    btnYes.setVisibility(View.INVISIBLE);
                    btnNo.setText(R.string.ok);
                }
                textViewTitle.setText(getString(R.string.warning, title));
                textViewSubTitle.setText(subtitle);
                break;
            case msgQuestion:
                imageViewType.setBackgroundResource(R.drawable.question_white);
                if (yesNo) {
                    btnYes.setVisibility(View.VISIBLE);
                    btnNo.setVisibility(View.VISIBLE);
                    btnYes.setText(R.string.yes);
                    btnNo.setText(R.string.no);
                } else {
                    btnYes.setVisibility(View.INVISIBLE);
                    btnNo.setText(R.string.ok);
                }
                textViewTitle.setText(getString(R.string.info, title));
                textViewSubTitle.setText(subtitle);
                break;
            case popUpMsg:
                imageViewType.setVisibility(View.INVISIBLE);
                imageViewType.getLayoutParams().height = 0;
                imageViewType.getLayoutParams().width = 0;
                if (yesNo) {
                    btnYes.setVisibility(View.VISIBLE);
                    btnNo.setVisibility(View.VISIBLE);
                    btnYes.setText(R.string.yes);
                    btnNo.setText(R.string.no);
                } else {
                    btnYes.setVisibility(View.INVISIBLE);
                    btnNo.setText(R.string.ok);
                }
                textViewTitle.setText(title);
                textViewSubTitle.setText(subtitle);
                break;
            default:
                textViewSubTitle.setText(R.string.error);
                textViewTitle.setText(R.string.error);
        }

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                constraintLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
                constraintLayout.setVisibility(View.VISIBLE);
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                btn.actionBtnYes(context);
            }

        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public boolean AlertDialog(Context context, LayoutInflater inflater, String title, String subtitle, Message msgType, boolean yesNo) {
        final boolean[] bool = new boolean[1];
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = null;
        if (inflater == null) {
            mView = getLayoutInflater().inflate(R.layout.aux_helper, null);
        } else {
            mView = inflater.inflate(R.layout.aux_helper, null);
        }

        TextView textViewTitle = (TextView) mView.findViewById(R.id.textViewTitle);
        TextView textViewSubTitle = (TextView) mView.findViewById(R.id.textViewSubTitle);
        Button btnYes = (Button) mView.findViewById(R.id.btnYes);
        Button btnNo = (Button) mView.findViewById(R.id.btnNo);
        ImageView imageViewType = (ImageView) mView.findViewById(R.id.imageViewType);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
        dialog.setCancelable(false);
        switch (msgType) {
            case msgDone:
                imageViewType.setBackgroundResource(R.drawable.done_white);
                if (yesNo) {
                    btnYes.setVisibility(View.VISIBLE);
                    btnNo.setVisibility(View.VISIBLE);
                    btnYes.setText(R.string.yes);
                    btnNo.setText(R.string.no);
                } else {
                    btnYes.setVisibility(View.INVISIBLE);
                    btnNo.setText(R.string.ok);
                }
                textViewTitle.setText(getString(R.string.sucesso, title));
                textViewSubTitle.setText(subtitle);
                break;
            case msgInfo:
                imageViewType.setBackgroundResource(R.drawable.info_white);
                if (yesNo) {
                    btnYes.setVisibility(View.VISIBLE);
                    btnNo.setVisibility(View.VISIBLE);
                    btnYes.setText(R.string.yes);
                    btnNo.setText(R.string.no);
                } else {
                    btnYes.setVisibility(View.INVISIBLE);
                    btnNo.setText(R.string.ok);
                }
                textViewTitle.setText(getString(R.string.info, title));
                textViewSubTitle.setText(subtitle);
                break;
            case msgError:
                imageViewType.setBackgroundResource(R.drawable.error_white);
                if (yesNo) {
                    btnYes.setVisibility(View.VISIBLE);
                    btnNo.setVisibility(View.VISIBLE);
                    btnYes.setText(R.string.yes);
                    btnNo.setText(R.string.no);
                } else {
                    btnYes.setVisibility(View.INVISIBLE);
                    btnNo.setText(R.string.ok);
                }
                textViewTitle.setText(title);
                textViewSubTitle.setText(subtitle);
                break;
            case msgWarning:
                imageViewType.setBackgroundResource(R.drawable.warning_white);
                if (yesNo) {
                    btnYes.setVisibility(View.VISIBLE);
                    btnNo.setVisibility(View.VISIBLE);
                    btnYes.setText(R.string.yes);
                    btnNo.setText(R.string.no);
                } else {
                    btnYes.setVisibility(View.INVISIBLE);
                    btnNo.setText(R.string.ok);
                }
                textViewTitle.setText(getString(R.string.warning, title));
                textViewSubTitle.setText(subtitle);
                break;
            case msgQuestion:
                imageViewType.setBackgroundResource(R.drawable.question_white);
                if (yesNo) {
                    btnYes.setVisibility(View.VISIBLE);
                    btnNo.setVisibility(View.VISIBLE);
                    btnYes.setText(R.string.yes);
                    btnNo.setText(R.string.no);
                } else {
                    btnYes.setVisibility(View.INVISIBLE);
                    btnNo.setText(R.string.ok);
                }
                textViewTitle.setText(getString(R.string.info, title));
                textViewSubTitle.setText(subtitle);
                break;
            case popUpMsg:
                imageViewType.setVisibility(View.INVISIBLE);
                imageViewType.getLayoutParams().height = 0;
                imageViewType.getLayoutParams().width = 0;
                if (yesNo) {
                    btnYes.setVisibility(View.VISIBLE);
                    btnNo.setVisibility(View.VISIBLE);
                    btnYes.setText(R.string.yes);
                    btnNo.setText(R.string.no);
                } else {
                    btnYes.setVisibility(View.INVISIBLE);
                    btnNo.setText(R.string.ok);
                }
                textViewTitle.setText(title);
                textViewSubTitle.setText(subtitle);
                break;
            default:
                textViewSubTitle.setText(R.string.error);
                textViewTitle.setText(R.string.error);
        }

        dialog.show();

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bool[0] = true;
                dialog.dismiss();
            }

        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bool[0] = false;
                dialog.dismiss();
            }
        });
        return bool[0];
    }

    public void AlertDialog(final Context context, LayoutInflater inflater, final FirebaseAuth auth, final ConstraintLayout constraintLayout, AlertDialog dialogs) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = null;
        if (inflater == null) {
            mView = getLayoutInflater().inflate(R.layout.activity_forgot, null);
        } else {
            mView = inflater.inflate(R.layout.activity_forgot, null);
        }
        final EditText edtTextEmail = (EditText) mView.findViewById(R.id.edtTextEmail);
        Button btnYes = (Button) mView.findViewById(R.id.btnYes);
        Button btnNo = (Button) mView.findViewById(R.id.btnNo);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
        dialog.setCancelable(false);
        fadeViews(constraintLayout, dialogs, false);
        dialog.show();

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtTextEmail.getText().toString().equals("")) {
                    final AlertDialog alertD = AlertDialog(context, null, getString(R.string.processando), getString(R.string.verificando_usuario_na_database), true);
                    auth.fetchProvidersForEmail(edtTextEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                    if (task.isSuccessful()) {
                                        auth.sendPasswordResetEmail(edtTextEmail.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            alertD.dismiss();
                                                            Toast.makeText(context, getString(R.string.msg_send_email, edtTextEmail.getText().toString()), Toast.LENGTH_LONG).show();
                                                        } else {
                                                            alertD.dismiss();
                                                            Toast.makeText(context, R.string.msg_email_nao_cad, Toast.LENGTH_SHORT).show();
                                                        }
                                                        //dialog.dismiss();
                                                    }
                                                });
                                    }
                                }
                            });

                } else {
                    Toast.makeText(context, R.string.insira_email_para_recuperar_senha, Toast.LENGTH_LONG).show();
                }
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fadeViews(constraintLayout, dialog, false);
                dialog.dismiss();
            }
        });
    }

    //    RequiresApi(api = Build.VERSION_CODES.N)
    public AlertDialog AlertDialogMusic(final Context inContext, LayoutInflater inflater, final String titleMusic, final int position) {
        final View mView;
        if (inflater == null) {
            mView = getLayoutInflater().inflate(R.layout.aux_playmusic, null);
        } else {
            mView = inflater.inflate(R.layout.aux_playmusic, null);
        }
        rootView = mView;
        this.inContext = inContext;
        this.titleMusic = titleMusic;


        final Resources res = inContext.getResources();
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(inContext);
        final String[] fileName = new String[1];
        final boolean[] isFirst = new boolean[1];

        final TextView textViewTitleMusic = (TextView) mView.findViewById(R.id.textViewTitleMusic);
        final TextView textViewTimerInit = (TextView) mView.findViewById(R.id.textViewTimerInit);
        final TextView textViewTimerFinal = (TextView) mView.findViewById(R.id.textViewTimerFinal);

        final ProgressBar progressBarLoadMusic = (ProgressBar) mView.findViewById(R.id.progressBarLoadingMusic);
        final ConstraintLayout constraintLoading = (ConstraintLayout) mView.findViewById(R.id.constraintLoading);
        final TextView textViewLoadMusic = (TextView) mView.findViewById(R.id.textViewLoadingMusic);

        final Button btnPlayPause = (Button) mView.findViewById(R.id.btnPlayPause);
        final Button btnStop = (Button) mView.findViewById(R.id.btnStop);
        final Button btnRepeat = (Button) mView.findViewById(R.id.btnRepeat);

        final SeekBar seekBar = (SeekBar) mView.findViewById(R.id.seekBar);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
        dialog.setCancelable(true);
        seekBar.setEnabled(false);


        textViewTitleMusic.setText(res.getString(R.string.a_reproduzir, titleMusic));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeat[0]) {
                    btnRepeat.setBackgroundResource(R.mipmap.ic_not_repeat);
                    repeat[0] = false;
                } else {
                    btnRepeat.setBackgroundResource(R.mipmap.ic_repeat);
                    repeat[0] = true;
                }
                updateBigNotification(mediaPlayer, repeat[0]);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlayPause.setBackgroundResource(R.mipmap.ic_plays);
                textViewTitleMusic.setText(res.getString(R.string.parado, titleMusic));
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
                updateSeekBar(mediaPlayer, seekBar, textViewTimerInit, textViewTimerFinal, textViewTitleMusic, res, totalDuration);
                notification.cancel(8);
               // dialog.setCancelable(true);
            }
        });

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection(inContext)) {
                    return;
                }
                try {
                    AsyncTask<String, String, String> mp3Play = new AsyncTask<String, String, String>() {

                        @Override
                        protected void onPreExecute() {
                            if (!isFirst[0]) {
                                textViewLoadMusic.setText(R.string.carregando_musica);
                                progressBarLoadMusic.setIndeterminate(true);
                                constraintLoading.setVisibility(View.VISIBLE);
                                btnPlayPause.setVisibility(View.INVISIBLE);
                                btnStop.setVisibility(View.INVISIBLE);
                                btnRepeat.setVisibility(View.INVISIBLE);
                               // seekBar.setEnabled(false);
                            }
                            btnStop.setEnabled(true);
                        }

                        @Override
                        protected String doInBackground(String... params) {
                            try {
                                // String path = downloadMusic(params[0], fileName[0]);
                                mediaPlayer.setDataSource(params[0]);
                                mediaPlayer.prepare();
                            } catch (Exception ex) {

                            }
                            return "";
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            if (!isFirst[0]) {
                                constraintLoading.setVisibility(View.INVISIBLE);
                                btnPlayPause.setVisibility(View.VISIBLE);
                                btnStop.setVisibility(View.VISIBLE);
                                btnRepeat.setVisibility(View.VISIBLE);
                                seekBar.setEnabled(true);
                                isFirst[0] = true;
                                mediaPlayer.start();
                                //textViewTitleMusic.setText(res.getString(R.string.reproduzindo, titleMusic));
                                btnPlayPause.setBackgroundResource(R.mipmap.ic_lpauses);

                            } else {
                                if (!mediaPlayer.isPlaying()) {
                                    mediaPlayer.start();
                                    //textViewTitleMusic.setText(res.getString(R.string.reproduzindo, titleMusic));
                                    btnPlayPause.setBackgroundResource(R.mipmap.ic_lpauses);
                                } else {
                                    mediaPlayer.pause();
                                    //textViewTitleMusic.setText(res.getString(R.string.pausado, titleMusic));
                                    btnPlayPause.setBackgroundResource(R.mipmap.ic_plays);
                                }
                            }
                            updateBigNotification(mediaPlayer, repeat[0]);
                            updateSeekBar(mediaPlayer, seekBar, textViewTimerInit, textViewTimerFinal, textViewTitleMusic, res, totalDuration);
                        }
                    };

                    switch (position) {
                        case 0:
                            if (checkConnection(inContext)) {
                                break;
                            }
                            mp3Play.execute(res.getString(R.string.music0));
                            break;
                        case 1:
                            if (checkConnection(inContext)) {
                                break;
                            }
                            mp3Play.execute(res.getString(R.string.music1));
                            break;
                        case 2:
                            if (checkConnection(inContext)) {
                                break;
                            }
                            mp3Play.execute(res.getString(R.string.music2));
                            break;
                        case 3:
                            if (checkConnection(inContext)) {
                                break;
                            }
                            mp3Play.execute(res.getString(R.string.music3));
                            break;
                        case 4:
                            if (checkConnection(inContext)) {
                                break;
                            }
                            mp3Play.execute(res.getString(R.string.music4));
                            break;
                        case 5:
                            if (checkConnection(inContext)) {
                                break;
                            }
                            mp3Play.execute(res.getString(R.string.music5));
                            break;
                        case 6:
                            if (checkConnection(inContext)) {
                                break;
                            }
                            mp3Play.execute(res.getString(R.string.music6));
                            break;
                        case 7:
                            if (checkConnection(inContext)) {
                                break;
                            }
                            mp3Play.execute(res.getString(R.string.music7));
                            break;
                        case 8:
                            if (checkConnection(inContext)) {
                                break;
                            }
                            mp3Play.execute(res.getString(R.string.music8));
                            break;
                        case 9:
                            if (checkConnection(inContext)) {
                                break;
                            }
                            mp3Play.execute(res.getString(R.string.music9));
                            break;
                        default:
                            if (checkConnection(inContext)) {
                                break;
                            }
                            mp3Play.execute(res.getString(R.string.erro));
                            break;
                    }
                    fileName[0] = Integer.toString(position);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(mediaPlayer.getDuration()); // 100% (0~99)
                mediaPlayer.start();
                totalDuration = mediaPlayer.getDuration();
                updateSeekBar(mediaPlayer, seekBar, textViewTimerInit, textViewTimerFinal, textViewTitleMusic, res, totalDuration);
                btnPlayPause.setBackgroundResource(R.mipmap.ic_lpauses);
            }
        });


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnPlayPause.setBackgroundResource(R.mipmap.ic_plays);
                //textViewTitleMusic.setText(res.getString(R.string.pausado, titleMusic));
                updateBigNotification(mediaPlayer, repeat[0]);
                if (repeat[0]) {
                    mediaPlayer.seekTo(0);
                    btnPlayPause.callOnClick();
                }
            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                seekBar.setSecondaryProgress(percent);
            }
        });
        mediaPlayer.setLooping(false);
        repeat[0] = false;

        try {
            reRunThread();
            updater = new Runnable() {
                @Override
                public void run() {
                    if (!stopUpdater) {
                        updateSeekBar(mediaPlayer, seekBar, textViewTimerInit, textViewTimerFinal, textViewTitleMusic, res, totalDuration);
                    }
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }

        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (isFirst[0]) {
                    mediaPlayer.stop();
                    stopThread();
                    if (notification != null) {
                        updateBigNotification(mediaPlayer, repeat[0]); //ativa a notificação por questoes de segurança
                        notification.cancel(8);
                    }
                }
            }
        });

        return dialog;
    }

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("====================>", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
           // Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
           // Log.e(TAG, "printHashKey()", e);
        }
    }

    public boolean isEmpty(EditText[] editTextList, Context context) {
        boolean bool = false;
        for (EditText edit : editTextList) {
            if (TextUtils.isEmpty(edit.getText())) {
                Toast.makeText(context, R.string.msg_erro_preencha_todos_os_campos, Toast.LENGTH_SHORT).show();
                bool = true;
                break;
            }
        }
        return bool;
    }

    private String downloadMusic(String urlFile, String nameFile) {
        try {
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root + "/Folder");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File music = new File(root + "/" + nameFile + ".mp3");
            if (!music.exists()) {
                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri downloadUri = Uri.parse(urlFile);
                DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                request.setAllowedOverRoaming(false);
                request.setTitle(nameFile);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, nameFile);
                request.allowScanningByMediaScanner();
                return root + "/" + nameFile + ".mp3";
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public enum Message {
        msgError(0), msgWarning(1), msgInfo(2), msgQuestion(3), msgDone(4), popUpMsg(5);

        private int result;

        Message(int result) {
            this.result = result;
        }
    }

    public interface BtnYes {
        void actionBtnYes(Context context);
    }

    public interface LogOut{
        void logoutFirebase(AlertDialog dialog);
    }
    public static class NotificationBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Button btnPause = (Button)  ((Activity) c).findViewById(R.id.btnPause);
            Button btnPlayPause = (Button) rootView.findViewById(R.id.btnPlayPause);
            Button btnRepeat = (Button) rootView.findViewById(R.id.btnRepeat);
            if (intent.getAction().equals(NotificationGenerator.NOTIFY_PLAY)) {
                if (!mediaPlayer.isPlaying()) {
                    HelperAux.mediaPlayer.start();
                    btnPlayPause.setBackgroundResource(R.mipmap.ic_lpauses);
                    HelperAux.updateBigNotification(HelperAux.mediaPlayer, HelperAux.repeat[0]);
                }
            } else if (intent.getAction().equals(NotificationGenerator.NOTIFY_PAUSE)) {
                if (mediaPlayer.isPlaying()) {
                    HelperAux.mediaPlayer.pause();
                    btnPlayPause.setBackgroundResource(R.mipmap.ic_plays);
                    HelperAux.updateBigNotification(HelperAux.mediaPlayer, HelperAux.repeat[0]);
                }
            } else if (intent.getAction().equals(NotificationGenerator.NOTIFY_NEXT)) {
                btnRepeat.setBackgroundResource(R.mipmap.ic_repeat);
                HelperAux.repeat[0] = true;
                HelperAux.updateBigNotification(HelperAux.mediaPlayer, HelperAux.repeat[0]);
            } else if (intent.getAction().equals(NotificationGenerator.NOTIFY_NEXT2)) {
                btnRepeat.setBackgroundResource(R.mipmap.ic_not_repeat);
                HelperAux.repeat[0] = false;
                HelperAux.updateBigNotification(HelperAux.mediaPlayer, HelperAux.repeat[0]);
            } else if (intent.getAction().equals(NotificationGenerator.NOTIFY_DELETE)) {
                mediaPlayer.stop();
                if (notification != null) {
                    HelperAux.updateBigNotification(HelperAux.mediaPlayer, HelperAux.repeat[0]); //ativa a notificação por questoes de segurança
                    notification.cancel(8);
                }
            } else if (intent.getAction().equals(NotificationGenerator.NOTIFY_PREVIOUS)) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);

                //stopThread();
                //
            }
        }
    }

}
