package com.example.projetoweb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

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

        imageView3lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });
    }

    private void checkLogin() {
        // Verifica se o usuário está logado e se sim, inicia a ListaCarros activity
        if (preferences.contains("email") && preferences.contains("password")) {
            Intent intent = new Intent(Login.this, ListaCarros.class);
            startActivity(intent);
            finish();
        }
    }

    private void doLogin() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, Config.UrlLogin, response -> {
            if ("OK".equals(response)) {
                boolean isAdmin = response.contains("isAdmin: true"); // Ajuste conforme a sua resposta do servidor
                SharedPreferences.Editor editor = preferences.edit();

                // Salva as credenciais e o status de administrador se o CheckBox está marcado
                if (save.isChecked()) {
                    editor.putString("email", email.getText().toString());
                    editor.putString("password", pass.getText().toString());
                    editor.putBoolean("isAdmin", isAdmin);
                    editor.apply();
                }

                // Inicia a ListaCarros activity
                Intent intent = new Intent(this, ListaCarros.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email.getText().toString());
                params.put("password", pass.getText().toString());
                return params;
            }
        };

        requestQueue.add(postRequest);
    }

    public void goToRegister(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}
