package br.dev.android.profilefire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView telaCadastro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.telaCadastro = findViewById(R.id.new_conta);
        this.telaCadastro.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.new_conta){
            Intent intent = new Intent(this, CadastroActivity.class);
            startActivity(intent);
        }

    }
}
