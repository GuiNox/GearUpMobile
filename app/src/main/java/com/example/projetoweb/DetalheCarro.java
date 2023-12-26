package com.example.projetoweb;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.projetoweb.model.Carro;
import com.squareup.picasso.Picasso;

public class DetalheCarro extends AppCompatActivity {

    private static final String CHANNEL_ID = "favoritos_channel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhe_carro);

        // Solicitação de permissão para notificações no Android 13 (API nível 33) e superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivity(intent);
            }
        }

        Carro carro = (Carro) getIntent().getSerializableExtra("CARRO_EXTRA");
        SharedPreferences sharedPreferences = getSharedPreferences("Favoritos", MODE_PRIVATE);

        ImageView ivCarroDetalhe = findViewById(R.id.ivCarroDetalhe);
        TextView tvMarcaModelo = findViewById(R.id.tvMarcaModelo);
        TextView tvAno = findViewById(R.id.tvAno);
        TextView tvDisponibilidade = findViewById(R.id.tvDisponibilidade);
        TextView tvValor = findViewById(R.id.tvValor);
        Button btnFavorito = findViewById(R.id.btnFavorito);

        Picasso.get().load(carro.getImgURL()).into(ivCarroDetalhe);
        tvMarcaModelo.setText(String.format("%s %s", carro.getMarca(), carro.getModelo()));
        tvAno.setText(String.format("Ano: %d", carro.getAno()));
        tvDisponibilidade.setText(carro.isDisponibilidade() ? "Disponível" : "Indisponível");
        tvValor.setText(String.format("Valor: %.2f €", carro.getValor()));

        btnFavorito.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("Favorito_" + carro.getId(), true);
            editor.apply();

            Toast.makeText(DetalheCarro.this, "Carro adicionado aos favoritos!", Toast.LENGTH_SHORT).show();

            // Agendar uma notificação para daqui a 1 minuto
            new Handler().postDelayed(() -> sendNotification(carro.getMarca() + " " + carro.getModelo()), 60000);
        });

        createNotificationChannel();
    }

    private void sendNotification(String carroInfo) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Carro Favoritado")
                .setContentText("Você adicionou o " + carroInfo + " aos favoritos.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
