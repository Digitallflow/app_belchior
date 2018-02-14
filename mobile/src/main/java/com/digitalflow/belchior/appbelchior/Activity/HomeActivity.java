package com.digitalflow.belchior.appbelchior.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.usb.UsbInterface;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.digitalflow.belchior.appbelchior.Entidades.Usuarios;
import com.digitalflow.belchior.appbelchior.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class HomeActivity extends AppCompatActivity {

    ImageView imageView;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        ImageButton btnCamera = (ImageButton)findViewById(R.id.btnCamera);
        imageView = (ImageView)findViewById(R.id.imageView);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent,0);
                Usuarios user = Usuarios.getInstance();
//                Toast.makeText(getApplicationContext(), user.getId(), Toast.LENGTH_SHORT).show();
                abrirCamera();
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
//        imageView.setImageBitmap(bitmap);
//    }

    public void abrirCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        InputStream stream = null;
        if(requestCode == 0 && resultCode == RESULT_OK){
            try{
                if(bitmap != null){
                    bitmap.recycle();
                }
                stream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                imageView.setImageBitmap(resizeImage(this, bitmap, 700, 600));
            } catch (FileNotFoundException e){
                e.printStackTrace();
            } finally {
                if(stream != null){
                    try{
                        stream.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static Bitmap resizeImage(Context context, Bitmap bmpOriginal, float newWidth, float newHeight){
        Bitmap novoBmp = null;
        int w = bmpOriginal.getWidth();
        int h = bmpOriginal.getHeight();

        float densityFactor = context.getResources().getDisplayMetrics().density;
        float novoW = newWidth * densityFactor;
        float novoH = newHeight * densityFactor;

        float scalaW = novoW / w;
        float scalaH = novoH / h;

        Matrix matrix = new Matrix();

        matrix.postScale(scalaW, scalaH);

        novoBmp = Bitmap.createBitmap(bmpOriginal, 0, 0, w, h, matrix, true);

        return novoBmp;

    }

}

