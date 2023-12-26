package com.example.projetoweb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText txtNome, txtEmail, txtPassword, txtConfirmPassword;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        ImageView imageView3 = findViewById(R.id.imageView3lg);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirecionar para o login
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        // Inicializar as views
        txtNome = findViewById(R.id.txtnome);
        txtEmail = findViewById(R.id.txtemail);
        txtPassword = findViewById(R.id.txtpass);
        txtConfirmPassword = findViewById(R.id.txtregisterconfirmpass);
        btnDone = findViewById(R.id.btnDone);

        // Definir um clique para o botão Done
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chamar o método de registo
                registerUser();
            }
        });
    }

    private void registerUser() {
        // Obter os valores inseridos pelo user
        String nome = txtNome.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String confirmPassword = txtConfirmPassword.getText().toString().trim();

        // Verificar se as senhas coincidem
        if (password.equals(confirmPassword)) {
            // Implementar a lógica de registo aqui
            sendRegistrationRequest(nome, email, password);

//            Toast.makeText(this, "Registro bem-sucedido!", Toast.LENGTH_SHORT).show();
        } else {
            // Senhas não coincidem, exibir mensagem de erro
            Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendRegistrationRequest(String nome, String email, String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, Config.UrlRegister, response -> {
            try {
                // Converter a resposta JSON em um objeto JSONObject
                JSONObject jsonResponse = new JSONObject(response);

                // Verificar se o registo foi bem-sucedido
                if (jsonResponse.getBoolean("success")) {
                    Toast.makeText(this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();

                    // Redirecionar para a atividade de login
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                    finish(); // Opcional: encerrar a atividade de registo após o sucesso
                } else {
                    // Exibir mensagem de erro
                    Toast.makeText(this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nome", nome);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        requestQueue.add(postRequest);
    }

}
