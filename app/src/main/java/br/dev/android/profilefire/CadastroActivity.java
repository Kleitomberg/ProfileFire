package br.dev.android.profilefire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CadastroActivity extends AppCompatActivity  {

    private TextView toLogin;
    String userID;
    private EditText inputNome, inputEmail, inputSenha;
    private Button cadastrarButton;
    String[] toastMessages ={
        "Preencha todos os campos",
        "O campo E-mail é obrigatorio",
        "O campo Senha é obrigatorio",
        "O campo Nome pe obrigatorio",
        "Cadastro realizado com sucesso@"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();

        setContentView(R.layout.activity_cadastro);

        this.startComponents();

        //event clicks

        //click to loginPage
        this.toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //click in btn cadastrar

        this.cadastrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = inputNome.getText().toString();
                String email = inputEmail.getText().toString();
                String senha = inputSenha.getText().toString();

                if (nome.equals("") || email.equals("") || senha.equals("")){
                    //mensagem preencha os campos
                    Snackbar snackbar = Snackbar.make(view, toastMessages[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                }else{
                    cadastrarUser(nome,email,senha, view);
                }


            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        redirectLogin();
    }

    private void cadastrarUser(String nome, String email, String senha, View v){

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    salvarUser(nome);
                    Snackbar snackbar = Snackbar.make(v, toastMessages[4], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.GREEN);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();

                    redirectLogin();


                }else{
                    String errors;
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        errors = "Digite uma senha com no minimo 6 caracteres";

                    }
                    catch (FirebaseAuthUserCollisionException e){
                        errors = "Já existe uma conta com esse E-mail";

                    }

                    catch (FirebaseAuthInvalidCredentialsException e){
                        errors = "Informe um E-mail válido!";

                    }
                    catch (Exception e){
                        errors = "Erro ao tentar cadastrar o usuario!";
                    }

                    Snackbar snackbar = Snackbar.make(v, errors, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();

                }
            }
        });

    }

    private void salvarUser(String nome){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("nome",nome);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(userID);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db", "Dados salvos com sucesso!");
            }
        })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("db+error", "Error ao tentar salvar os dados" + e.toString());

                }
            });



    }

    private void redirectLogin(){
        FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();

        if (usuarioLogado != null){
            Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void startComponents(){
        this.toLogin = findViewById(R.id.new_login);
        this.inputEmail = findViewById(R.id.input_email);
        this.inputNome = findViewById(R.id.input_username);
        this.inputSenha = findViewById(R.id.input_senha);
        this.cadastrarButton = findViewById(R.id.btn_cadastro);
    }
}
