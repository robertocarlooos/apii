package com.example.medimate_java;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.medimate_java.api.APIService;
import com.example.medimate_java.api.RetrofitClient;
import com.example.medimate_java.models.HistorialToma;
import com.example.medimate_java.models.HistorialTomaResponse;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Historico extends Fragment {

    private LinearLayout linearLayoutHistorico;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historico, container, false);


        linearLayoutHistorico = view.findViewById(R.id.linearLayoutHistorico);


        fetchHistorico();

        return view;
    }
    private void fetchHistorico() {
        int usuarioId = obtenerUsuarioId();
        APIService apiService = RetrofitClient.getRetrofitInstance(getContext()).create(APIService.class);

        Call<HistorialTomaResponse> call = apiService.getHistorico(usuarioId);
        call.enqueue(new Callback<HistorialTomaResponse>() {
            @Override
            public void onResponse(Call<HistorialTomaResponse> call, Response<HistorialTomaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<HistorialToma> historicoList = response.body().getData();


                    for (HistorialToma item : historicoList) {
                        Log.d("DEBUG", "Medicamento: " + (item.getMedicamento() != null ? item.getMedicamento().getNome() : "null"));
                    }

                    displayHistorico(historicoList);
                } else {
                    Toast.makeText(getContext(), "Erro ao  carregar histórico", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HistorialTomaResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayHistorico(List<HistorialToma> historicoList) {
        linearLayoutHistorico.removeAllViews();

        for (HistorialToma item : historicoList) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.historico_item, linearLayoutHistorico, false);

            TextView tvMedicamentoNome = itemView.findViewById(R.id.medicamento_name);
            TextView tvDataToma = itemView.findViewById(R.id.data_toma);
            TextView tvHoraToma = itemView.findViewById(R.id.hora_toma);
            TextView tvEstado = itemView.findViewById(R.id.estado);
            View deleteIcon = itemView.findViewById(R.id.delete_icon);

            tvMedicamentoNome.setText("Medicamento: " + item.getMedicamentoNome());
            tvDataToma.setText("Data de toma: " + (item.getDataToma() != null ? item.getDataToma() : "Desconhecido"));
            tvHoraToma.setText("Hora: " + (item.getHoraToma() != null ? item.getHoraToma() : "Desconhecido"));
            tvEstado.setText("Estado: " + (item.getEstado() != null ? item.getEstado() : "Desconhecido"));



            linearLayoutHistorico.addView(itemView);
        }
    }

    private int obtenerUsuarioId() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("usuario_id", -1);
    }

}