package com.example.projetoweb.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoweb.Config;
import com.example.projetoweb.R;
import com.example.projetoweb.model.Carro;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CarrosAdapter extends RecyclerView.Adapter<CarrosAdapter.ViewHolder> {

    private final OnCarroClickListener listener;
    private ArrayList<Carro> carros;
    private OnCarroClickListener onCarroClickListener;

    public interface OnCarroClickListener {
        void onCarroClick(Carro carro);
    }

    public CarrosAdapter(ArrayList<Carro> carros, OnCarroClickListener listener) {
        this.carros = carros;
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_marca, txt_modelo, txt_ano, txt_disponibilidade, txt_valor;
        ImageView img_carro;

        ViewHolder(View itemView, final OnCarroClickListener onCarroClickListener, final ArrayList<Carro> carros) {
            super(itemView);

            txt_marca = itemView.findViewById(R.id.txt_marca);
            txt_modelo = itemView.findViewById(R.id.txt_modelo);
            txt_ano = itemView.findViewById(R.id.txt_ano);
            txt_disponibilidade = itemView.findViewById(R.id.txt_disponibilidade);
            txt_valor = itemView.findViewById(R.id.txt_valor);
            img_carro = itemView.findViewById(R.id.img_carro);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCarroClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onCarroClickListener.onCarroClick(carros.get(position));
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carro, parent, false);
        return new ViewHolder(itemView, onCarroClickListener, carros);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Carro item = carros.get(position);
        holder.txt_marca.setText(item.getMarca());
        holder.txt_modelo.setText(item.getModelo());
        holder.txt_ano.setText(String.valueOf(item.getAno()));
        holder.txt_disponibilidade.setText(item.isDisponibilidade() ? "Disponível" : "Indisponível");
        holder.txt_valor.setText(String.format("%s €", item.getValor()));

        // Construa a URL completa da imagem aqui
        String imageUrl = Config.projetpath + "uploads/" + item.getImgURL();

        // Defina o onClickListener para o itemView
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCarroClick(item);
            }
        });

        // Carregue a imagem com o Picasso
        Picasso.get()
                .load(imageUrl) // Carrega a imagem a partir da URL construída
                .placeholder(R.drawable.placeholder_image) // Mostra uma imagem de placeholder enquanto carrega
                .error(R.drawable.error_image) // Mostra uma imagem de erro se não conseguir carregar
                .into(holder.img_carro); // Define a imagem no ImageView do ViewHolder
    }

    @Override
    public int getItemCount() {
        return carros.size();
    }
}
