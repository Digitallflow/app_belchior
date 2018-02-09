package com.digitalflow.belchior.appbelchior.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.view.WindowManager;


import com.digitalflow.belchior.appbelchior.DAO.ConfiguracaoFirebase;
import com.digitalflow.belchior.appbelchior.Entidades.Usuarios;
import com.digitalflow.belchior.appbelchior.Helper.Base64Custom;
import com.digitalflow.belchior.appbelchior.Helper.Preferencias;
import com.digitalflow.belchior.appbelchior.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

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
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
                    //usuarios = new Usuarios();
                    usuarios.setFirstName(edtCadNome.getText().toString());
                    usuarios.setEmail(edtCadEmail.getText().toString());
                    usuarios.setLastName(edtCadSobrenome.getText().toString());
                    usuarios.setPass(edtCadSenha.getText().toString());
                    usuarios.setBirth(edtCadNasc.getText().toString());

                    if(rbFeminino.isChecked()){
                        usuarios.setSex("Feminino");
                    }else{
                        usuarios.setSex("Masculino");
                    }
                    cadastrarUsuario();
                }
                else{
                    Toast.makeText(CadastroActivity.this, "As senhas não são correspondentes", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        autenticacao.createUserWithEmailAndPassword(
                usuarios.getEmail(),
                usuarios.getPass()
        ).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();

                    String identificadorUsuario = Base64Custom.codificarBase64(usuarios.getEmail());

                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuarios.setId(identificadorUsuario);
                    usuarios.salvar();

                    Preferencias preferencias = new Preferencias(CadastroActivity.this);
                    preferencias.salvarUsuarioPreferencias(identificadorUsuario, usuarios.getFirstName());

                    abrirLoginUsuario();

                } else{
                    String erroExcecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        erroExcecao = getString(R.string.msg_erro_senha_minimo);
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = getString(R.string.msg_erro_email_valido);
                    } catch (FirebaseAuthUserCollisionException e){
                        erroExcecao = getString(R.string.msg_erro_cadastro_existente);
                    } catch (Exception e){
                        erroExcecao = getString(R.string.msg_erro_cadastro);
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    public void abrirLoginUsuario(){
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }
}
