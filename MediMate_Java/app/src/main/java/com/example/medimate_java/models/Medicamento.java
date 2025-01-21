package com.example.medimate_java.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Medicamento  implements Serializable {
    private int id;
    private String nome;
    private String dose;
    private String tipo;
    @SerializedName("quantidade_atual")
    private int quantidadeAtual;

    @SerializedName("categoria_id")
    private int categoriaId;
    private Categoria categoria;

    public Medicamento(String nome, String dose, String tipo, int quantidadeAtual,  int categoriaId) {
        this.nome = nome;
        this.dose = dose;
        this.tipo = tipo;
        this.quantidadeAtual = quantidadeAtual;
        this.categoriaId= categoriaId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters y setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(int quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}