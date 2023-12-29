package com.example.projetoweb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class Menu extends AppCompatActivity {

    // Nome do arquivo de preferências compartilhadas
    private static final String PREFS_NAME = "PrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    // Retroceder
    public void retroceder(View view){
        finish();
    }

    // Ir para a lista de carros
    public void abrirListaCarros(View view){
        Intent intent = new Intent(this, ListaCarros.class);
        startActivity(intent);
    }


    // Ir para Gestao

    public void abrirGestao(View view){
        Intent intent = new Intent(Menu.this, Gestao.class);
        startActivity(intent);
    }

    // Ir para SobreNos

    public void abrirSobreNos(View view){
        Intent intent = new Intent(this, SobreNos.class);
        startActivity(intent);
    }

    // Logout e retorno para a tela de login
    public void logout(View view){
        // Limpa as SharedPreferences para remover as credenciais de login
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear(); // Limpa todas as preferências
        editor.apply();

        // Retornar para a tela de login
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpa a pilha de atividades
        startActivity(intent);
        finish(); // Fecha todas as outras atividades
    }

    // Altere o nome do método para refletir a ação de logout
    public void fecharApp(View view){
        logout(view);
    }

}
