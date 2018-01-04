package com.digitalflow.belchior.app_belchior.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.digitalflow.belchior.app_belchior.R;

public class MainActivity extends AppCompatActivity {
    private Button AbrirTelaInicial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        AbrirTelaInicial = (Button)findViewById(R.id.btnConectar);

        findViewById(R.id.btnConectar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAbrirTelaInicial = new Intent(MainActivity.this, Inicial.class);
                startActivity(intentAbrirTelaInicial);
            }
        });
    }
}