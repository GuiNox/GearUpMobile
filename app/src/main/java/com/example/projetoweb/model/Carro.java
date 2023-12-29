package com.example.projetoweb.model;

import java.io.Serializable;

public class Carro implements Serializable {

    private int id;
    private String marca;
    private String modelo;
    private int ano;

    private int tipoveiculo;
    private boolean disponibilidade;

    private String descricao;
    private double valor;
    private String imgURL;

    // Construtores, se necessário

    // Getters

    public int getId(){
        return id;
    }
    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAno() {
        return ano;
    }

    public int getTipoveiculo() {return tipoveiculo; }

    public boolean isDisponibilidade() {
        return disponibilidade;
    }

    public String getDescricao() { return descricao; }

    public double getValor() {
        return valor;
    }

    public String getImgURL() {
        return imgURL;
    }

    // Setters

    public void setId(int id){
        this.id = id;
    }
    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public void setTipoveiculo(int tipoveiculo) {this.tipoveiculo = tipoveiculo; }

    public void setDisponibilidade(boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public void setDescricao(String descricao){this.descricao = descricao; }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
