package com.digitalflow.belchior.appbelchior.Fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalflow.belchior.appbelchior.Activity.HomeActivity;
import com.digitalflow.belchior.appbelchior.Activity.MusicActivity;
import com.digitalflow.belchior.appbelchior.Adapter.TabsAdapter;
import com.digitalflow.belchior.appbelchior.DAO.Crud;
import com.digitalflow.belchior.appbelchior.Entidades.Musicas;
import com.digitalflow.belchior.appbelchior.Entidades.Usuarios;
import com.digitalflow.belchior.appbelchior.Helper.HelperAux;
import com.digitalflow.belchior.appbelchior.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;


public class CameraFragment extends Fragment {

    final int RequestCameraPermissionID = 1001;
    final boolean[] secondUse = new boolean[1];
    protected boolean onCreateViewCalled = false;
    protected boolean firstTimeInitCameraCalled;
    LayoutInflater inflater;
    SurfaceView cameraPreview = null;
    ProgressBar progressBarCamera;
    TextView txtResult;
    BarcodeDetector barcodeDetector = null;
    CameraSource cameraSource = null;
    private boolean cameraRunning = false;
    private HelperAux helper = new HelperAux();
    //double scaleCamera;
    private AlertDialog alert;
    //Callback Surface
    private SurfaceHolder.Callback surfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            startCameraView(getContext(), cameraSource, cameraPreview);
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            stopCamera();
        }
    };

    public CameraFragment() {
        // Required empty public constructor
        // scaleCamera = this.getResources().getDisplayMetrics().density;
    }

    /**
     * onCreateView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        onCreateViewCalled = true; // importante

        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        this.inflater = inflater;

        cameraPreview = (SurfaceView) view.findViewById(R.id.cameraPreview);
        txtResult = (TextView) view.findViewById(R.id.txtResult);
        progressBarCamera = (ProgressBar) view.findViewById(R.id.progressBarCamera);
        firstTimeInitCameraCalled = true;

        initCamera();

        return view;
    }

    /**
     * Função para detectar se o fragment da câmera está visível para o usuário
     */

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        if (menuVisible && onCreateViewCalled) {
            if (helper.checkConnection(getActivity())){
                return;
            }
            firstTimeInitCameraCalled = false;
            initCamera();
            startCameraView(getContext(), cameraSource, cameraPreview);

        } else if (!menuVisible && onCreateViewCalled) {
            if (helper.checkConnection(getActivity())){
                return;
            }
            progressBarCamera.setVisibility(View.VISIBLE);
            cameraSource.stop();
            cameraRunning = false;
            firstTimeInitCameraCalled = true;
            barcodeDetector = null;
            cameraSource = null;
        }

        super.setMenuVisibility(menuVisible);
    }

    public boolean onCreateViewBeenCalled() {
        return onCreateViewCalled;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                        cameraRunning = true;
                        progressBarCamera.setVisibility(View.GONE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }

    /**
     * Init Camera
     */
    public void initCamera() {
        final Resources res = getActivity().getResources();
        // if (barcodeDetector == null) {
        barcodeDetector = new BarcodeDetector.Builder(getActivity())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        if (barcodeDetector.isOperational()) {
            secondUse[0] = false;
            barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                @Override
                public void release() {
                }

                @Override
                public void receiveDetections(Detector.Detections<Barcode> detections) {
//                    final Handler timeOutAlert = new Handler(Looper.getMainLooper());
//                    final Runnable runnable = new Runnable() {
//                        @Override
//                        public void run() {
//                            alert.dismiss();
//                            Log.d("Thread","runnable line 186 executed");
//                            helper.AlertDialog(getActivity(), inflater, res.getString(R.string.error), res.getString(R.string.erro_timeout_reconhecer_codigo), HelperAux.Message.msgError, false);
//                        }
//                    };
                    if (!firstTimeInitCameraCalled) {
                        final SparseArray<Barcode> qrcodes = detections.getDetectedItems();

                        if (qrcodes.size() != 0) {
                            txtResult.post(new Runnable() {
                                @Override
                                public void run() {
                                    txtResult.setText(qrcodes.valueAt(0).displayValue);
                                    String tituloMusica = txtResult.getText().toString();
                                    Usuarios user = Usuarios.getInstance();
                                    int p;
                                    if (tituloMusica != null && !secondUse[0]) {
                                        alert = helper.AlertDialog(getActivity(), inflater, getString(R.string.processando), getString(R.string.identificando_codigo), true);
                                        //timeOutAlert.postDelayed(runnable, 10000);
                                        Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                                        vibrator.vibrate(1000);
                                        secondUse[0] = true;
                                        switch (tituloMusica) {
                                            case "numero00":
                                                p = 0;
                                                if (isUnlocked(user, p)) {
                                                    alert.dismiss();
                                                    //timeOutAlert.removeCallbacks(runnable);
                                                    slideToMusicFragment();
                                                    helper.AlertDialog(getActivity(), inflater, getString(R.string.procure_a_proxima), getString(R.string.a_musica_ja_foi_desbloqueada, Crud.titleMusics[p]), false);
                                                } else {
                                                    Musicas music = new Musicas("Music 0", true);
                                                    setFirMusic(user, music, p, helper, alert, res, inflater);
                                                }
                                                if (alert.isShowing()) {
                                                    alert.dismiss();
                                                }
                                                break;
                                            case "numero01":
                                                p = 1;
                                                if (isUnlocked(user, p)) {
                                                    alert.dismiss();
                                                    //timeOutAlert.removeCallbacks(runnable);
                                                    slideToMusicFragment();
                                                    helper.AlertDialog(getActivity(), inflater, getString(R.string.procure_a_proxima), getString(R.string.a_musica_ja_foi_desbloqueada, Crud.titleMusics[p]), false);
                                                } else {
                                                    Musicas music = new Musicas("Music 1", true);
                                                    setFirMusic(user, music, p, helper, alert, res, inflater);
                                                }
                                                if (alert.isShowing()) {
                                                    alert.dismiss();
                                                }
                                                break;
                                            case "numero02":
                                                p = 2;
                                                if (isUnlocked(user, p)) {
                                                    alert.dismiss();
                                                    //timeOutAlert.removeCallbacks(runnable);
                                                    slideToMusicFragment();
                                                    helper.AlertDialog(getActivity(), inflater, getString(R.string.procure_a_proxima), getString(R.string.a_musica_ja_foi_desbloqueada, Crud.titleMusics[p]), false);
                                                } else {
                                                    Musicas music = new Musicas("Music 2", true);
                                                    setFirMusic(user, music, p, helper, alert, res, inflater);
                                                }
                                                if (alert.isShowing()) {
                                                    alert.dismiss();
                                                }
                                                break;
                                            case "numero03":
                                                p = 3;
                                                if (isUnlocked(user, p)) {
                                                    alert.dismiss();
                                                    //timeOutAlert.removeCallbacks(runnable);
                                                    slideToMusicFragment();
                                                    helper.AlertDialog(getActivity(), inflater, getString(R.string.procure_a_proxima), getString(R.string.a_musica_ja_foi_desbloqueada, Crud.titleMusics[p]), false);
                                                } else {
                                                    Musicas music = new Musicas("Music 3", true);
                                                    setFirMusic(user, music, p, helper, alert, res, inflater);
                                                }
                                                if (alert.isShowing()) {
                                                    alert.dismiss();
                                                }
                                                break;
                                            case "numero04":
                                                p = 4;
                                                if (isUnlocked(user, p)) {
                                                    alert.dismiss();
                                                   // timeOutAlert.removeCallbacks(runnable);
                                                    slideToMusicFragment();
                                                    helper.AlertDialog(getActivity(), inflater, getString(R.string.procure_a_proxima), getString(R.string.a_musica_ja_foi_desbloqueada, Crud.titleMusics[p]), false);
                                                } else {
                                                    Musicas music = new Musicas("Music 4", true);
                                                    setFirMusic(user, music, p, helper, alert, res, inflater);
                                                }
                                                if (alert.isShowing()) {
                                                    alert.dismiss();
                                                }
                                                break;
                                            case "numero05":
                                                p = 5;
                                                if (isUnlocked(user, p)) {
                                                    alert.dismiss();
                                                    //timeOutAlert.removeCallbacks(runnable);
                                                    slideToMusicFragment();
                                                    helper.AlertDialog(getActivity(), inflater, getString(R.string.procure_a_proxima), getString(R.string.a_musica_ja_foi_desbloqueada, Crud.titleMusics[p]), false);
                                                } else {
                                                    Musicas music = new Musicas("Music 5", true);
                                                    setFirMusic(user, music, p, helper, alert, res, inflater);
                                                }
                                                if (alert.isShowing()) {
                                                    alert.dismiss();
                                                }
                                                break;
                                            case "numero06":
                                                p = 6;
                                                if (isUnlocked(user, p)) {
                                                    alert.dismiss();
                                                    //timeOutAlert.removeCallbacks(runnable);
                                                    slideToMusicFragment();
                                                    helper.AlertDialog(getActivity(), inflater, getString(R.string.procure_a_proxima), getString(R.string.a_musica_ja_foi_desbloqueada, Crud.titleMusics[p]), false);
                                                } else {
                                                    Musicas music = new Musicas("Music 6", true);
                                                    setFirMusic(user, music, p, helper, alert, res, inflater);
                                                }
                                                if (alert.isShowing()) {
                                                    alert.dismiss();
                                                }
                                                break;
                                            case "numero07":
                                                p = 7;
                                                if (isUnlocked(user, p)) {
                                                    alert.dismiss();
                                                    //timeOutAlert.removeCallbacks(runnable);
                                                    slideToMusicFragment();
                                                    helper.AlertDialog(getActivity(), inflater, getString(R.string.procure_a_proxima), getString(R.string.a_musica_ja_foi_desbloqueada, Crud.titleMusics[p]), false);
                                                } else {
                                                    Musicas music = new Musicas("Music 7", true);
                                                    setFirMusic(user, music, p, helper, alert, res, inflater);
                                                }
                                                if (alert.isShowing()) {
                                                    alert.dismiss();
                                                }
                                                break;
                                            case "numero08":
                                                p = 8;
                                                if (isUnlocked(user, p)) {
                                                    alert.dismiss();
                                                   // timeOutAlert.removeCallbacks(runnable);
                                                    slideToMusicFragment();
                                                    helper.AlertDialog(getActivity(), inflater, getString(R.string.procure_a_proxima), getString(R.string.a_musica_ja_foi_desbloqueada, Crud.titleMusics[p]), false);
                                                } else {
                                                    Musicas music = new Musicas("Music 8", true);
                                                    setFirMusic(user, music, p, helper, alert, res, inflater);
                                                }
                                                if (alert.isShowing()) {
                                                    alert.dismiss();
                                                }
                                                break;
                                            case "numero09":
                                                p = 9;
                                                if (isUnlocked(user, p)) {
                                                    alert.dismiss();
                                                    //timeOutAlert.removeCallbacks(runnable);
                                                    slideToMusicFragment();
                                                    helper.AlertDialog(getActivity(), inflater, getString(R.string.procure_a_proxima), getString(R.string.a_musica_ja_foi_desbloqueada, Crud.titleMusics[p]), false);
                                                } else {
                                                    Musicas music = new Musicas("Music 9", true);
                                                    setFirMusic(user, music, p, helper, alert, res, inflater);
                                                }
                                                if (alert.isShowing()) {
                                                    alert.dismiss();
                                                }
                                                break;
                                            default:
                                                if (alert.isShowing()) {
                                                    alert.dismiss();
                                                }
                                                slideToMusicFragment();
                                                Toast.makeText(getActivity(), R.string.esta_musica_nao_esta_registrada_na_base_de_dados, Toast.LENGTH_SHORT).show();
                                        }
                                        if (alert.isShowing()) {
                                            alert.dismiss();
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }

        // }

        // if (cameraSource == null) {
        cameraSource = new CameraSource
                .Builder(getActivity(), barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)
                .build();

        //  }

    }

    /**
     * Start Camera
     */
    public void startCamera() {
        if (cameraPreview != null && surfaceHolderCallback != null) {
            cameraPreview.getHolder().addCallback(surfaceHolderCallback);
        }
    }

    /**
     * Função para detectar o start da camera e permissão no celular
     */
    private void startCameraView(Context context, CameraSource cameraSource,
                                 SurfaceView surfaceView) {
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, RequestCameraPermissionID);
                //onRequestPermissionsResult(RequestCameraPermissionID, new String[]{Manifest.permission.CAMERA},new int[]{PackageManager.PERMISSION_GRANTED});
            } else if (!cameraRunning && cameraSource != null && surfaceView != null) {
                cameraSource.start(surfaceView.getHolder());
                //Log.e("start", "started camera");
                cameraRunning = true;
                progressBarCamera.setVisibility(View.GONE);
            }
        } catch (IOException ie) {
            //Log.e(TAG, ie.getMessage());
            ie.printStackTrace();
        }
    }

    /**
     * Stop Camera
     */
    public void stopCamera() {
        try {
            if (cameraPreview != null && surfaceHolderCallback != null) {
                cameraPreview.getHolder().removeCallback(surfaceHolderCallback);
            }
            if (cameraRunning && cameraSource != null) {
                cameraSource.stop();
                cameraRunning = false;
            }
            //Log.e("rem", "removed callback");
        } catch (Exception ie) {
            //Log.e(TAG, ie.getMessage());
            ie.printStackTrace();
        }
    }


    /**
     * Release and cleanup Camera.
     */
    public void releaseAndCleanup() {
        if (cameraRunning) {
            stopCamera();
            if (cameraSource != null) {
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    public boolean isUnlocked(Usuarios user, int position) {
        boolean value = ((Boolean) user.getMusic(position).getLocked()).booleanValue();
        if (value) {
            return true;
        } else {
            return false;
        }
    }

    public void setFirMusic(final Usuarios user, final Musicas music,
                            final int position, final HelperAux helper, final AlertDialog alert,
                            final Resources res, final LayoutInflater inflater) {
        if (helper.checkConnection(getActivity())){
            return;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        hashMap.put(music.getNome(), music.getLocked());
        db.collection("musics").document(user.getId()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Log.d("CRUD", "++++++++++++++++++++++++++++++++saved++++++++++++++++++++++++++++++++++++++++++++++++++");
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               // Log.d("CRUD", "+++++++++++++++++++++++++++++++++++++++++++not saved+++++++++++++++++++++++++++++++++++", e);
            }

        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                user.setMusic(music, position);
                updateUserMusics(helper, alert, position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                alert.dismiss();
                helper.AlertDialog(getActivity(), inflater, res.getString(R.string.error), res.getString(R.string.msg_erro_atualizar_musica_no_firebase), HelperAux.Message.msgError, false);
                //fazer excecoes com toast
            }
        });
    }

    public void updateUserMusics(HelperAux helper, AlertDialog alert, int position) {
        //Atualizar a listagem de itens do fragmento MusicFragment
        alert.dismiss();
        helper.AlertDialog(getActivity(), inflater, getString(R.string.parabens), getString(R.string.voce_desbloqueou_a_musica, Crud.titleMusics[position]), false);
        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewPagerMain);
        TabsAdapter newAdapter = (TabsAdapter) viewPager.getAdapter();
        MusicFragment newMusicFragment = (MusicFragment) newAdapter.getFragment(0);
        newMusicFragment.getMusicsByUserLogged(newMusicFragment.getLayoutInflater());
        slideToMusicFragment();
    }

    public void slideToMusicFragment() {
        Button btnMusicas = (Button) getActivity().findViewById(R.id.btnMusicas);
        btnMusicas.callOnClick();
    }
}
