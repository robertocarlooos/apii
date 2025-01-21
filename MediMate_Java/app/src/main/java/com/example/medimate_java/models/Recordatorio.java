package com.example.medimate_java.models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Recordatorio  implements Serializable {
    private int id;
    @SerializedName("medicamento_id")
    private int medicamentoId;

    @SerializedName("medicamento")
    private Medicamento medicamento;

    private String horario;
    private int frequencia;
    private int ativo;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMedicamentoId() {
        return medicamentoId;
    }

    public void setMedicamentoId(int medicamentoId) {
        this.medicamentoId = medicamentoId;
    }
    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }




    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public int getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(int frequencia) {
        this.frequencia = frequencia;
    }

    public int getAtivo() {
        return ativo;
    }

    public void setAtivo(int ativo) {
        this.ativo = ativo;
    }
}
