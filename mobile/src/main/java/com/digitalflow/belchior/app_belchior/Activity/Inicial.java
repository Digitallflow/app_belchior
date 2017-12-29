package com.digitalflow.belchior.app_belchior.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.WindowManager;


import com.digitalflow.belchior.app_belchior.R;

public class Inicial extends AppCompatActivity {
    private Button btnAbrirTelaLogin;
    private Button btnAbrirTelaCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_inicial);

        btnAbrirTelaLogin = (Button)findViewById(R.id.btnLogar);
        btnAbrirTelaCadastro = (Button)findViewById(R.id.btnCadastrar);

        btnAbrirTelaLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intentAbrirTelaLogin = new Intent(Inicial.this, LoginActivity.class);
                startActivity(intentAbrirTelaLogin);
            }
        });

        btnAbrirTelaCadastro.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intentAbrirTelaCadastro = new Intent(Inicial.this, CadastroActivity.class);
                startActivity(intentAbrirTelaCadastro);
            }
        });
    }
}
