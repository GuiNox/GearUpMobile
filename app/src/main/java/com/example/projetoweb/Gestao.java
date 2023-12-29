package com.example.projetoweb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetoweb.adapter.CarrosAdapter;
import com.example.projetoweb.model.Carro;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Gestao extends AppCompatActivity implements CarrosAdapter.OnCarroClickListener {

    private Carro carroSelecionado;
    private ArrayList<Carro> carros = new ArrayList<>();  // Lista de carros
    private RecyclerView recyclerView;  // RecyclerView

    ImageView imageView3lg;

    private Button btnAddCar, btnEditCar, btnRemoveCar;
    private RequestQueue requestQueue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestao);

        // Inicializando RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.rvCars);
        CarrosAdapter adapter = new CarrosAdapter(carros, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializando botões
        btnAddCar = findViewById(R.id.btnAddCar);
        btnEditCar = findViewById(R.id.btnEditCar);
        btnRemoveCar = findViewById(R.id.btnRemoveCar);
        imageView3lg = findViewById(R.id.imageView3lg);

        imageView3lg.setOnClickListener(view -> finish());

        // Adicionando Listeners para os botões
        btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogoAdicionarCarro();
            }
        });

        btnEditCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implementar lógica de edição
                editarCarro();
            }
        });

        btnRemoveCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implementar lógica de remoção
//                Carro carroSelecionado = getCarroSelecionado();

                if (carroSelecionado != null) {
                    int vehicleId = carroSelecionado.getId();
                    String imageUrl = carroSelecionado.getImgURL();

                    // Chama removerCarro() com os argumentos corretos
                    removerCarro(vehicleId, imageUrl);
                } else {
                    // Se nenhum carro está selecionado, mostre uma mensagem ao usuário
                    Toast.makeText(Gestao.this, "Por favor, selecione um carro para remover.", Toast.LENGTH_SHORT).show();
                }
            }

            private Carro getCarroSelecionado() {
                return carroSelecionado;
            }
        });
    }

    @Override
    public void onCarroClick(Carro carro) {
        // Atualize o carroSelecionado com o carro que foi clicado
        carroSelecionado = carro;
        Toast.makeText(this, "Carro selecionado: " + carro.getMarca(), Toast.LENGTH_SHORT).show();
        // Agora carroSelecionado contém o carro que foi clicado
    }

    private void mostrarDialogoAdicionarCarro() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicionar Novo Carro");

        // Inflar e configurar o layout para o diálogo
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_car, null);
        final EditText etMarca = dialogView.findViewById(R.id.etMarca);
        final EditText etModelo = dialogView.findViewById(R.id.etModelo);
        final EditText etAno = dialogView.findViewById(R.id.etAnoCarro);
        final EditText etTipo = dialogView.findViewById(R.id.etTipoVeiculo);
        final EditText etDisponibilidade = dialogView.findViewById(R.id.etDisponibilidade);
        final EditText etDescricao = dialogView.findViewById(R.id.etDescricao);
        final EditText etImage = dialogView.findViewById(R.id.etCarImage);

        builder.setView(dialogView);

        builder.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Implementar lógica para adicionar carro aqui
                String marca = etMarca.getText().toString();
                String modelo = etModelo.getText().toString();
                String ano = etAno.getText().toString();
                String tipo = etTipo.getText().toString();
                String disponibilidade = etDisponibilidade.getText().toString();
                String descricao = etDescricao.getText().toString();
                String image = etImage.getText().toString();

                enviarDadosCarro(marca, modelo, ano, tipo, disponibilidade, descricao, image);
            }
        });

        builder.setNegativeButton("Cancelar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void enviarDadosCarro(String marca, String modelo, String ano, String tipo, String disponibilidade, String descricao, String filePath) {
        // Headers (se precisares de headers específicos, senão pode ser um mapa vazio)
        Map<String, String> headers = new HashMap<>();

        // Parâmetros de texto
        Map<String, String> params = new HashMap<>();
        params.put("marca", marca);
        params.put("modelo", modelo);
        params.put("ano", ano);
        params.put("tipo", tipo);
        params.put("disponibilidade", disponibilidade);
        params.put("descricao", descricao);

        // Byte data para a imagem
        Map<String, DataPart> byteData = new HashMap<>();
        byteData.put("imagem", new DataPart("car_image.jpg", AppHelper.getFileDataFromPath(getApplicationContext(), filePath), "image/jpeg"));

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(
                Request.Method.POST,
                "https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g16/Projeto/api/addveiculo.php",
                headers, // Passa o mapa de headers
                params, // Passa o mapa de parâmetros de texto
                byteData, // Passa o mapa de DataParts (byte data)
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Toast.makeText(Gestao.this, "Resposta do servidor: " + new String(response.data), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Gestao.this, "Erro ao enviar dados: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(multipartRequest);
    }



    private void editarCarro() {
        // Crie um AlertDialog para obter as novas informações do carro do usuário
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Carro");

        // Inflar e configurar o layout para o diálogo
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_car, null);
        final EditText etMarca = dialogView.findViewById(R.id.etMarca);
        final EditText etModelo = dialogView.findViewById(R.id.etModelo);
        final EditText etAno = dialogView.findViewById(R.id.etAnoCarro);
        final EditText etTipo = dialogView.findViewById(R.id.etTipoVeiculo);
        final EditText etDisponibilidade = dialogView.findViewById(R.id.etDisponibilidade);
        final EditText etDescricao = dialogView.findViewById(R.id.etDescricao);
        final EditText etImage = dialogView.findViewById(R.id.etCarImage);

        // Preencher os campos com os dados atuais do carro
        etMarca.setText(carroSelecionado.getMarca());
        etModelo.setText(carroSelecionado.getModelo());
        etAno.setText(String.valueOf(carroSelecionado.getAno())); // Converte int para String
        etTipo.setText(carroSelecionado.getTipoveiculo());
        etDisponibilidade.setText(carroSelecionado.isDisponibilidade() ? "Sim" : "Não");
        etDescricao.setText(carroSelecionado.getDescricao());
        etImage.setText(carroSelecionado.getImgURL());

        builder.setView(dialogView);

        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Recupere os valores editados do AlertDialog
                String marca = etMarca.getText().toString();
                String modelo = etModelo.getText().toString();
                String ano = etAno.getText().toString();
                String tipo = etTipo.getText().toString();
                String disponibilidade = etDisponibilidade.getText().toString();
                String descricao = etDescricao.getText().toString();
                String image = etImage.getText().toString();

                // Aqui você chama a função que atualiza os dados do carro no servidor
                atualizarDadosCarro(marca, modelo, ano, tipo, disponibilidade, descricao, image);
            }
        });

        builder.setNegativeButton("Cancelar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void atualizarDadosCarro(String marca, String modelo, String ano, String tipo, String disponibilidade, String descricao, String image) {
        // Implemente a lógica para atualizar os dados do carro no servidor aqui

        String url = "https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g16/Projeto/api/editar_veiculo.php";
        // Isso geralmente envolverá enviar uma solicitação HTTP PUT para o seu servidor web com os dados atualizados do carro

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("id", carroSelecionado.getId());
            jsonParams.put("marca", marca);
            jsonParams.put("modelo", modelo);
            jsonParams.put("ano", ano);
            jsonParams.put("tipo", tipo);
            jsonParams.put("disponibilidade", disponibilidade);
            jsonParams.put("descricao", descricao);
            jsonParams.put("image", image);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, // ou POST, dependendo do que seu servidor espera
                url,
                jsonParams,
                response -> {
                    // Trate a resposta aqui. Por exemplo, atualize a interface do usuário mostrando que os dados foram atualizados com sucesso.
                    try {
                        if (response.getBoolean("success")) {
                            Toast.makeText(Gestao.this, "Carro atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            // O servidor respondeu que a atualização falhou.
                            Toast.makeText(Gestao.this, "Falha ao atualizar: " + response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Gestao.this, "Erro na resposta do servidor", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Trate os erros de rede aqui
                    Toast.makeText(Gestao.this, "Erro ao atualizar o carro: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                // Adicione quaisquer cabeçalhos necessários aqui, como tokens de autenticação
                return headers;
            }
        };

        // Adiciona a solicitação à fila
        requestQueue.add(jsonObjectRequest);
    }




    private void removerCarro(int vehicleId, String imageUrl) {
        String url = "https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g16/Projeto/api/remover_veiculo.php"; // Substitua pela URL do seu endpoint PHP

        // Adicionar parâmetros ao URL, como o ID do veículo e a localização da imagem
        url += "?id=" + vehicleId + "&image=" + Uri.encode(imageUrl);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    // Trate a resposta aqui. Por exemplo, atualize a interface do usuário mostrando que o carro foi removido com sucesso.
                    Toast.makeText(Gestao.this, "Carro removido com sucesso!", Toast.LENGTH_SHORT).show();
                    // Aqui você pode querer atualizar a lista de carros ou realizar outras ações baseadas na remoção bem-sucedida.
                },
                error -> {
                    // Trate os erros de rede aqui
                    Toast.makeText(Gestao.this, "Erro ao remover o carro: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        // Adicione a solicitação à fila
        requestQueue.add(stringRequest);
    }

}
