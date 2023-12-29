package com.example.projetoweb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login2 extends AppCompatActivity {

    EditText email, pass;
    Button login;
    CheckBox save;
    ImageView imageView3lg;

    // Adiciona constantes para SharedPreferences
    private static final String PREFS_NAME = "PrefsFile";
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        // Inicialização
        email = findViewById(R.id.txtemail);
        pass = findViewById(R.id.txtpass);
        login = findViewById(R.id.btnlogin);
        save = findViewById(R.id.salvar);
        imageView3lg = findViewById(R.id.imageView3lg);
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Verifica se existe um usuário previamente logado
        checkLogin();

        imageView3lg.setOnClickListener(view -> finish());

        login.setOnClickListener(view -> doLogin());
    }

    private void checkLogin() {
        // Verifica se o usuário está logado e se sim, inicia a ListaCarros activity
        if (preferences.contains("email") && preferences.contains("password")) {
            Intent intent = new Intent(Login2.this, ListaCarros.class);
            startActivity(intent);
            finish();
        }
    }

    private void doLogin() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("email", email.getText().toString());
            jsonParams.put("password", pass.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, Config.UrlLogin, jsonParams, response -> {
            try {
                if (response.getBoolean("success")) {
                    // Login bem-sucedido
                    boolean isAdmin = response.getBoolean("isAdmin");
                    String nomeUser = response.getString("nomeUtilizador");
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("email", email.getText().toString());
                    editor.putString("password", pass.getText().toString());
                    editor.putString("nome_utilizador", nomeUser);
                    editor.putBoolean("isAdmin", isAdmin);  // Salvar o status do admin
                    editor.apply();

                    // Iniciar a próxima atividade (ListaCarros)
                    Intent intent = new Intent(Login2.this, ListaCarros.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Login falhou
                    Toast.makeText(Login2.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(Login2.this, "Erro na rede: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public void goToRegister(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}
