package com.digitalflow.belchior.app_belchior;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.digitalflow.belchior.app_belchior.Entidades.Usuarios;

public class CadastroActivity extends AppCompatActivity {

    private EditText edtCadEmail;
    private EditText edtCadSenha;
    private EditText edtCadConfirmarSenha;
    private EditText edtCadNome;
    private EditText edtCadSobrenome;
    private EditText edtCadNasc;
    private RadioButton rbMasculino;
    private RadioButton rbFeminino;
    private Button btnCadastrar;

    private Usuarios usuarios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        edtCadEmail = (EditText)findViewById(R.id.edtCadEmail);
        edtCadNome = (EditText)findViewById(R.id.edtCadNome);
        edtCadSobrenome = (EditText)findViewById(R.id.edtCadSobrenome);
        edtCadSenha = (EditText)findViewById(R.id.edtCadSenha);
        edtCadConfirmarSenha = (EditText)findViewById(R.id.edtCadConfirmarSenha);
        edtCadNasc = (EditText)findViewById(R.id.edtCadNasc);
        rbMasculino = (RadioButton)findViewById(R.id.rbMasculino);
        rbFeminino = (RadioButton)findViewById(R.id.rbFeminino);
        btnCadastrar = (Button)findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtCadSenha.getText().toString().equals(edtCadConfirmarSenha.getText().toString())){
                    usuarios = new Usuarios();
                    usuarios.setNome(edtCadNome.getText().toString());
                    usuarios.setEmail(edtCadEmail.getText().toString());
                    usuarios.setSobrenome(edtCadSobrenome.getText().toString());
                    usuarios.setSenha(edtCadSenha.getText().toString());
                    usuarios.setNasc(edtCadNasc.getText().toString());

                    if(rbFeminino.isChecked()){
                        usuarios.setSexo("Feminino");
                    }else{
                        usuarios.setSexo("Masculino");
                    }
                }
                else{
                    Toast.makeText(CadastroActivity.this, "As senhas não são correspondentes", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
