package com.example.medimate_java.models;

import com.google.gson.annotations.SerializedName;

public class HistorialToma {
    private int id;
    private int medicamento_id;
    private String data_toma;
    private String hora_toma;
    private String estado;

    @SerializedName("medicamento")
    private Medicamento medicamento;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMedicamentoId() {
        return medicamento_id;
    }

    public void setMedicamentoId(int medicamento_id) {
        this.medicamento_id = medicamento_id;
    }

    public String getDataToma() {
        return data_toma;
    }

    public void setDataToma(String data_toma) {
        this.data_toma = data_toma;
    }

    public String getHoraToma() {
        return hora_toma;
    }

    public void setHoraToma(String hora_toma) {
        this.hora_toma = hora_toma;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public String getMedicamentoNome() {
        return medicamento != null ? medicamento.getNome() : "Desconocido";
    }
}