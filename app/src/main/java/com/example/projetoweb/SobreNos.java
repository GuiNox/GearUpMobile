package com.example.projetoweb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SobreNos extends AppCompatActivity {

    private EditText etNome, etAssunto, etCorpo;
    private Button btnEnviar;
    private SharedPreferences prefs;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre_nos);

        ImageView imageView3lg = findViewById(R.id.imageView3lg);
        imageView3lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();  // Fecha a atividade atual e retorna à anterior.
            }
        });


        etNome = findViewById(R.id.etNome);
        etAssunto = findViewById(R.id.etAssunto);
        etCorpo = findViewById(R.id.etCorpo);
        btnEnviar = findViewById(R.id.btnEnviar);

        prefs = getSharedPreferences("PrefsFile", MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(this);

        preencherEmailDoUsuario(); // Método para preencher o campo de email.

        btnEnviar.setOnClickListener(view -> enviarMensagem());
    }

    private void preencherEmailDoUsuario() {
        // Recupera o email do usuário das SharedPreferences.
        String emailDoUsuario = prefs.getString("email", "");
        etNome.setText(emailDoUsuario); // Define o email recuperado no campo de texto.
    }

    private void enviarMensagem() {

        String email = etNome.getText().toString();
        String assunto = etAssunto.getText().toString();
        String corpo = etCorpo.getText().toString();

        if (etAssunto.getText().toString().isEmpty() || etCorpo.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }
        StringRequest postRequest = new StringRequest(Request.Method.POST, "https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g16/Projeto/api/processa_formulariomobile.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.optString("mensagem", "Resposta não contém a chave 'mensagem'.");
                            Toast.makeText(SobreNos.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SobreNos.this, "Erro ao processar resposta JSON.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SobreNos.this, "Erro ao enviar mensagem!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", etNome.getText().toString());
                params.put("assunto", etAssunto.getText().toString());
                params.put("mensagem", etCorpo.getText().toString());
                return params;
            }
        };
        requestQueue.add(postRequest);
    }
}
