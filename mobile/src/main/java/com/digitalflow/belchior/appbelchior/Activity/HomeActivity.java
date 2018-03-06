package com.digitalflow.belchior.appbelchior.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.usb.UsbInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalflow.belchior.appbelchior.DAO.Crud;
import com.digitalflow.belchior.appbelchior.Entidades.Musicas;
import com.digitalflow.belchior.appbelchior.Entidades.Usuarios;
import com.digitalflow.belchior.appbelchior.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.Result;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.commons.lang3.ArrayUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class HomeActivity extends AppCompatActivity{

    ImageView imageView;
    SurfaceView cameraPreview;
    TextView txtResult;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    private ImageButton btnMusicas;


    final int RequestCameraPermissionID = 1001;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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


    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_home);

        cameraPreview = (SurfaceView) findViewById(R.id.cameraPreview);
        txtResult = (TextView) findViewById(R.id.txtResult);
        btnMusicas = (ImageButton)findViewById(R.id.btnMusicas);

        btnMusicas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MusicActivity.class);
                startActivity(intent);
            }
        });

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource
               .Builder(this, barcodeDetector)
               .setRequestedPreviewSize(640, 480)
               .build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CAMERA}, RequestCameraPermissionID);
                    return;
                }
                try{
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e){
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

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if(qrcodes.size() != 0 ){
                    txtResult.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            txtResult.setText(qrcodes.valueAt(0).displayValue);

                            Usuarios user = Usuarios.getInstance();
                            Musicas[] musicas_user = user.getMusic();


                            String tituloMusica = txtResult.getText().toString();

                            Toast.makeText(HomeActivity.this, "oie ", Toast.LENGTH_SHORT).show();

                            if(tituloMusica != null){

                                switch (tituloMusica){
                                    case "numero00":
                                        Musicas music0 = new Musicas("Music 0", true);
                                        user.setMusic(music0,0);
                                        Crud.setFirMusic(user,music0);
                                        break;
                                    case "numero01":
                                        Musicas music1 = new Musicas("Music 1", true);
                                        user.setMusic(music1,1);
                                        Crud.setFirMusic(user,music1);
                                        break;
                                    case "numero02":
                                        Musicas music2 = new Musicas("Music 2", true);
                                        user.setMusic(music2,2);
                                        Crud.setFirMusic(user,music2);
                                        break;
                                    case "numero03":
                                        Musicas music3 = new Musicas("Music 3", true);
                                        user.setMusic(music3,3);
                                        Crud.setFirMusic(user,music3);
                                        break;
                                    case "numero04":
                                        Musicas music4 = new Musicas("Music 4", true);
                                        user.setMusic(music4,4);
                                        Crud.setFirMusic(user,music4);
                                        break;
                                    case "numero05":
                                        Musicas music5 = new Musicas("Music 5", true);
                                        user.setMusic(music5,5);
                                        Crud.setFirMusic(user,music5);
                                        break;
                                    case "numero06":
                                        Musicas music6 = new Musicas("Music 6", true);
                                        user.setMusic(music6,6);
                                        Crud.setFirMusic(user,music6);
                                        break;
                                    case "numero07":
                                        Musicas music7 = new Musicas("Music 7", true);
                                        user.setMusic(music7,7);
                                        Crud.setFirMusic(user,music7);
                                        break;
                                    case "numero08":
                                        Musicas music8 = new Musicas("Music 8", true);
                                        user.setMusic(music8,8);
                                        Crud.setFirMusic(user,music8);
                                        break;
                                    case "numero09":
                                        Musicas music9 = new Musicas("Music 9", true);
                                        user.setMusic(music9,9);
                                        Crud.setFirMusic(user,music9);
                                        break;
                                }
                            }

                        }
                        });
                    }
                }
                });
            }
        }

