package com.digitalflow.belchior.appbelchior.Fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalflow.belchior.appbelchior.Activity.HomeActivity;
import com.digitalflow.belchior.appbelchior.Adapter.TabsAdapter;
import com.digitalflow.belchior.appbelchior.DAO.Crud;
import com.digitalflow.belchior.appbelchior.Entidades.Musicas;
import com.digitalflow.belchior.appbelchior.Entidades.Usuarios;
import com.digitalflow.belchior.appbelchior.Helper.HelperAux;
import com.digitalflow.belchior.appbelchior.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment {

    final int RequestCameraPermissionID = 1001;
    protected boolean onCreateViewCalled = false;
    LayoutInflater inflater;
    SurfaceView cameraPreview;
    TextView txtResult;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    private AlertDialog alert;


    public CameraFragment() {
        // Required empty public constructor
    }

    public boolean onCreateViewBeenCalled() {
        return onCreateViewCalled;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }

    // Função para detectar se o fragment da câmera está visível para o usuário
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        /* Se o Fragment esteja visível para o usuario, setar a funcao do barcode
            para detectar o qrcode. */
        if (isVisibleToUser && isResumed()) {
            barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<Barcode> detections) {
                    final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                    if (qrcodes.size() != 0) {
                        txtResult.post(new Runnable() {
                            @Override
                            public void run() {
                                Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(1000);
                                txtResult.setText(qrcodes.valueAt(0).displayValue);

                                Usuarios user = Usuarios.getInstance();


                                String tituloMusica = txtResult.getText().toString();
                                HelperAux helper = new HelperAux();
                                int p = 0;

                                if (tituloMusica != null) {
                                    alert = helper.AlertDialog(getActivity(), inflater ,"essamusica","identificando musica..", true);
                                    switch (tituloMusica) {
                                        case "numero00":
                                            p = 0;
                                            if (isUnlocked(user, p)) {
                                                alert.dismiss();
                                                helper.AlertDialog(getActivity(), inflater ,getString(R.string.procure_a_proxima),getString(R.string.a_musica_ja_foi_desbloqueada,Crud.titleMusics[p]), false);
                                            } else {
                                                Musicas music = new Musicas("Music 0", true);
                                                if (Crud.setFirMusic(user, music)){
                                                    user.setMusic(music, p);
                                                    updateUserMusics(helper, alert,p);
                                                } else {
                                                    alert.dismiss();
                                                    helper.AlertDialog(getActivity(), inflater ,"essamusica","erro ao da update na musica em firebase", HelperAux.Message.msgError, false);
                                                }

                                            }
                                            break;
                                        case "numero01":
                                            p = 1;
                                            if (isUnlocked(user, p)) {
                                                alert.dismiss();
                                                helper.AlertDialog(getActivity(), inflater ,getString(R.string.procure_a_proxima),getString(R.string.a_musica_ja_foi_desbloqueada,Crud.titleMusics[p]), false);
                                            } else {

                                                Musicas music = new Musicas("Music 1", true);
                                                if (Crud.setFirMusic(user, music)){
                                                    user.setMusic(music, p);
                                                    updateUserMusics(helper, alert,p);
                                                } else {
                                                    alert.dismiss();
                                                    helper.AlertDialog(getActivity(), inflater ,"essamusica","erro ao da update na musica em firebase", HelperAux.Message.msgError, false);
                                                }

                                            }
                                            break;
                                        case "numero02":
                                            p = 2;
                                            if (isUnlocked(user, p)) {
                                                alert.dismiss();
                                                helper.AlertDialog(getActivity(), inflater ,getString(R.string.procure_a_proxima),getString(R.string.a_musica_ja_foi_desbloqueada,Crud.titleMusics[p]), false);
                                            } else {

                                                Musicas music = new Musicas("Music 2", true);
                                                if (Crud.setFirMusic(user, music)){
                                                    user.setMusic(music, p);
                                                    updateUserMusics(helper, alert,p);
                                                } else {
                                                    alert.dismiss();
                                                    helper.AlertDialog(getActivity(), inflater ,"essamusica","erro ao da update na musica em firebase", HelperAux.Message.msgError, false);
                                                }

                                            }
                                            break;
                                        case "numero03":
                                            p = 3;
                                            if (isUnlocked(user, p)) {
                                                alert.dismiss();
                                                helper.AlertDialog(getActivity(), inflater ,getString(R.string.procure_a_proxima),getString(R.string.a_musica_ja_foi_desbloqueada,Crud.titleMusics[p]), false);
                                            } else {

                                                Musicas music = new Musicas("Music 3", true);
                                                if (Crud.setFirMusic(user, music)){
                                                    user.setMusic(music, p);
                                                    updateUserMusics(helper, alert,p);
                                                } else {
                                                    alert.dismiss();
                                                    helper.AlertDialog(getActivity(), inflater ,"essamusica","erro ao da update na musica em firebase", HelperAux.Message.msgError, false);
                                                }

                                            }
                                            break;
                                        case "numero04":
                                            p = 4;
                                            if (isUnlocked(user, p)) {
                                                alert.dismiss();
                                                helper.AlertDialog(getActivity(), inflater ,getString(R.string.procure_a_proxima),getString(R.string.a_musica_ja_foi_desbloqueada,Crud.titleMusics[p]), false);
                                            } else {

                                                Musicas music = new Musicas("Music 4", true);
                                                if (Crud.setFirMusic(user, music)){
                                                    user.setMusic(music, p);
                                                    updateUserMusics(helper, alert,p);
                                                } else {
                                                    alert.dismiss();
                                                    helper.AlertDialog(getActivity(), inflater ,"essamusica","erro ao da update na musica em firebase", HelperAux.Message.msgError, false);
                                                }

                                            }
                                            break;
                                        case "numero05":
                                            p = 5;
                                            if (isUnlocked(user, p)) {
                                                alert.dismiss();
                                                helper.AlertDialog(getActivity(), inflater ,getString(R.string.procure_a_proxima),getString(R.string.a_musica_ja_foi_desbloqueada,Crud.titleMusics[p]), false);
                                            } else {

                                                Musicas music = new Musicas("Music 5", true);
                                                if (Crud.setFirMusic(user, music)){
                                                    user.setMusic(music, p);
                                                    updateUserMusics(helper, alert,p);
                                                } else {
                                                    alert.dismiss();
                                                    helper.AlertDialog(getActivity(), inflater ,"essamusica","erro ao da update na musica em firebase", HelperAux.Message.msgError, false);
                                                }

                                            }
                                            break;
                                        case "numero06":
                                            p = 6;
                                            if (isUnlocked(user, p)) {
                                                alert.dismiss();
                                                helper.AlertDialog(getActivity(), inflater ,getString(R.string.procure_a_proxima),getString(R.string.a_musica_ja_foi_desbloqueada,Crud.titleMusics[p]), false);
                                            } else {

                                                Musicas music = new Musicas("Music 6", true);
                                                if (Crud.setFirMusic(user, music)){
                                                    user.setMusic(music, p);
                                                    updateUserMusics(helper, alert,p);
                                                } else {
                                                    alert.dismiss();
                                                    helper.AlertDialog(getActivity(), inflater ,"essamusica","erro ao da update na musica em firebase", HelperAux.Message.msgError, false);
                                                }

                                            }
                                            break;
                                        case "numero07":
                                            p = 7;
                                            if (isUnlocked(user, p)) {
                                                alert.dismiss();
                                                helper.AlertDialog(getActivity(), inflater ,getString(R.string.procure_a_proxima),getString(R.string.a_musica_ja_foi_desbloqueada,Crud.titleMusics[p]), false);
                                            } else {

                                                Musicas music = new Musicas("Music 7", true);
                                                if (Crud.setFirMusic(user, music)){
                                                    user.setMusic(music, p);
                                                    updateUserMusics(helper, alert,p);
                                                } else {
                                                    alert.dismiss();
                                                    helper.AlertDialog(getActivity(), inflater ,"essamusica","erro ao da update na musica em firebase", HelperAux.Message.msgError, false);
                                                }

                                            }
                                            break;
                                        case "numero08":
                                            p = 8;
                                            if (isUnlocked(user, p)) {
                                                alert.dismiss();
                                                helper.AlertDialog(getActivity(), inflater ,getString(R.string.procure_a_proxima),getString(R.string.a_musica_ja_foi_desbloqueada,Crud.titleMusics[p]), false);
                                            } else {

                                                Musicas music = new Musicas("Music 8", true);
                                                if (Crud.setFirMusic(user, music)){
                                                    user.setMusic(music, p);
                                                    updateUserMusics(helper, alert,p);
                                                } else {
                                                    alert.dismiss();
                                                    helper.AlertDialog(getActivity(), inflater ,"essamusica","erro ao da update na musica em firebase", HelperAux.Message.msgError, false);
                                                }

                                            }
                                            break;
                                        case "numero09":
                                            p = 9;
                                            if (isUnlocked(user, p)) {
                                                alert.dismiss();
                                                helper.AlertDialog(getActivity(), inflater ,getString(R.string.procure_a_proxima),getString(R.string.a_musica_ja_foi_desbloqueada,Crud.titleMusics[p]), false);
                                            } else {

                                                Musicas music = new Musicas("Music 9", true);
                                                if (Crud.setFirMusic(user, music)){
                                                    user.setMusic(music, p);
                                                    updateUserMusics(helper, alert,p);
                                                } else {
                                                    alert.dismiss();
                                                    helper.AlertDialog(getActivity(), inflater ,"essamusica","erro ao da update na musica em firebase", HelperAux.Message.msgError, false);
                                                }

                                            }
                                            break;
                                        default:
                                            Toast.makeText(getActivity(), "Código inválido", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                }
            });
            onResume();
        } else {
            /* Se o Fragment estiver invisível e o método createView ja foi chamado, então sobreescrever o barcode para não
            /  detectar o qrcode com o Fragment invisível */
            if (onCreateViewBeenCalled()) {
                barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                    @Override
                    public void release() {

                    }

                    @Override
                    public void receiveDetections(Detector.Detections<Barcode> detections) {

                    }
                });
            }
        }
    }

    //Função auxiliar da função acima para saber se a view não está pausada.
    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        onCreateViewCalled = true; // importante

        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        this.inflater = inflater;

        cameraPreview = (SurfaceView) view.findViewById(R.id.cameraPreview);
        txtResult = (TextView) view.findViewById(R.id.txtResult);

        barcodeDetector = new BarcodeDetector.Builder(getActivity())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource
                .Builder(getActivity(), barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, RequestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        return view;
    }

    public void updateUserMusics(HelperAux helper, AlertDialog alert, int position) {
        //Atualizar a listagem de itens do fragmento MusicFragment
        alert.dismiss();
        helper.AlertDialog(getActivity(), inflater ,getString(R.string.parabens), getString(R.string.voce_desbloqueou_a_musica, Crud.titleMusics[position]), false);
        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewPagerMain);
        Button btnMusicas = (Button) getActivity().findViewById(R.id.btnMusicas);
        TabsAdapter newAdapter = (TabsAdapter) viewPager.getAdapter();
        MusicFragment newMusicFragment = (MusicFragment) newAdapter.getFragment(0);
        newMusicFragment.getMusicsByUserLogged();
        btnMusicas.callOnClick();
    }

    public boolean isUnlocked(Usuarios user, int position) {
        boolean value = ((Boolean) user.getMusic(position).getLocked()).booleanValue();
        if (value) {
            return true;
        } else {
            return false;
        }
    }

}
