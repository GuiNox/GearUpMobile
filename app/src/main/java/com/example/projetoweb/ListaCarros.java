package com.example.projetoweb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetoweb.adapter.CarrosAdapter;
import com.example.projetoweb.model.Carro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListaCarros extends AppCompatActivity implements CarrosAdapter.OnCarroClickListener {

    private ArrayList<Carro> carros;
    private RecyclerView recyclerView;
    private CarrosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listacarros);

        ImageView btnSeta = findViewById(R.id.btnSeta);
        btnSeta.setOnClickListener(view -> {
            SharedPreferences prefs = getSharedPreferences("PrefsFile", MODE_PRIVATE);
            boolean isAdmin = prefs.getBoolean("isAdmin", false);

            Intent intent = isAdmin ? new Intent(ListaCarros.this, Menu.class) : new Intent(ListaCarros.this, MenuUser.class);
            startActivity(intent);
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        carros = new ArrayList<>();
        adapter = new CarrosAdapter(carros, this);
        recyclerView.setAdapter(adapter);
        carregarCarros();
    }

    private void carregarCarros() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g16/Projeto/api/veiculos.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            Carro carro = new Carro();
                            carro.setMarca(c.getString("marca"));
                            carro.setModelo(c.getString("modelo"));
                            carro.setAno(c.getInt("ano"));
                            carro.setDisponibilidade(c.getBoolean("disponibilidade"));
                            carro.setImgURL(c.getString("img"));
                            carros.add(carro);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(ListaCarros.this, "Erro ao carregar dados: " + error.toString(), Toast.LENGTH_LONG).show());

        queue.add(stringRequest);
    }

    @Override
    public void onCarroClick(Carro carro) {
        Intent intent = new Intent(this, DetalheCarro.class);
        intent.putExtra("CARRO_EXTRA", carro);
        startActivity(intent);
    }
}
