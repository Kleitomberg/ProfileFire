package br.dev.android.profilefire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView telaCadastro;

    private EditText inputEmail, inputSenha;
    private Button logarBtn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getSupportActionBar().hide();

        this.telaCadastro = findViewById(R.id.new_conta);
        this.inputEmail = findViewById(R.id.input_email);
        this.inputSenha = findViewById(R.id.input_senha);
        this.progressBar = findViewById(R.id.progrressBar);

        this.logarBtn = findViewById(R.id.btn_login);

        this.telaCadastro.setOnClickListener(this);
        this.logarBtn.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        telaprincipal();
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.new_conta){
            Intent intent = new Intent(this, CadastroActivity.class);
            startActivity(intent);
        }


        else if(view.getId() == R.id.btn_login){
           // Log.d("click","clicou no botão!");
            String email = this.inputEmail.getText().toString();
            String senha = this.inputSenha.getText().toString();

            if (email.isEmpty() || senha.isEmpty()){
                //Log.d("debug","campos vazios!");
                Snackbar snackbar1 = Snackbar.make(view, "Preencha todos os campos!", Snackbar.LENGTH_SHORT);
                snackbar1.setBackgroundTint(Color.WHITE);
                snackbar1.setTextColor(Color.BLACK);
                snackbar1.show();
            }else{
                Log.d("debug","chamou a função autheticar!!");
                AuthenticarUser(email, senha, view);
            }

        }
    }

    private void AuthenticarUser(String email, String senha, View view){

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            telaprincipal();
                        }
                    },3000);
                } else{
                    String errors;
                    try{
                        throw task.getException();
                    }
                    catch (FirebaseAuthInvalidCredentialsException e){
                        errors = "E-mail ou Senha incorretos!";

                    }
                    catch (Exception e){
                        errors = "Erro ao tentar logar o usuario!";
                    }

                    Snackbar snackbar = Snackbar.make(view, errors, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();


                }
            }
        });

    }

    private void telaprincipal(){
        FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();

        if (usuarioLogado != null){
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            startActivity(intent);
        }

    }

}
