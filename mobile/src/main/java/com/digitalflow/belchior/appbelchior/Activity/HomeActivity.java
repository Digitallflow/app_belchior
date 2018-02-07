package com.digitalflow.belchior.appbelchior.Activity;

import android.graphics.Bitmap;
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

public class HomeActivity extends AppCompatActivity {

    ImageView imageView;

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
                Toast.makeText(getApplicationContext(), user.getPass(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);
    }

}
