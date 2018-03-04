package com.digitalflow.belchior.appbelchior.Helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalflow.belchior.appbelchior.Activity.HomeActivity;
import com.digitalflow.belchior.appbelchior.Activity.Inicial;
import com.digitalflow.belchior.appbelchior.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by MarllonS on 25/01/2018.
 */

public class HelperAux extends AppCompatActivity {

    public void getSupportActionB() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void openActivity(Class<?> cls, AlertDialog dialog) {
        Intent intent = new Intent(getApplicationContext(), cls);
        startActivity(intent);
        dialog.dismiss();
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
                imageViewType.setImageDrawable(getResources().getDrawable(R.drawable.done_white));
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
                imageViewType.setImageDrawable(getResources().getDrawable(R.drawable.info_white));
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
                imageViewType.setImageDrawable(getResources().getDrawable(R.drawable.error_white));
                if (yesNo) {
                    btnYes.setVisibility(View.VISIBLE);
                    btnNo.setVisibility(View.VISIBLE);
                    btnYes.setText(R.string.yes);
                    btnNo.setText(R.string.no);
                } else {
                    btnYes.setVisibility(View.INVISIBLE);
                    btnNo.setText(R.string.ok);
                }
                textViewTitle.setText(getString(R.string.error, title));
                textViewSubTitle.setText(subtitle);
                break;
            case msgWarning:
                imageViewType.setImageDrawable(getResources().getDrawable(R.drawable.warning_white));
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
                imageViewType.setImageDrawable(getResources().getDrawable(R.drawable.question_white));
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

    public enum Message {
        msgError(0), msgWarning(1), msgInfo(2), msgQuestion(3), msgDone(4), popUpMsg(5);

        private int result;

        Message(int result) {
            this.result = result;
        }
    }
}
