package com.example.medimate_java.models;

import java.util.List;

public class RecordatorioResponse {
    private List<Recordatorio> data;

    public List<Recordatorio> getData() {
        return data;
    }

    public void setData(List<Recordatorio> data) {
        this.data = data;
    }
}
