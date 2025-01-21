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
import com.example.medimate_java.models.Recordatorio;
import com.example.medimate_java.models.RecordatorioResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notificacoes extends Fragment {

    private LinearLayout linearLayoutNotificacoes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_notificacoes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar o conteiner de notificações
        linearLayoutNotificacoes = view.findViewById(R.id.linearLayoutNotificacoes);

        // Carregar as notificações ativas desde a API
        fetchNotificacoes();
    }

    private void fetchNotificacoes() {
        int usuarioId = obtenerUsuarioId();
        APIService apiService = RetrofitClient.getRetrofitInstance(requireContext()).create(APIService.class);
        Call<RecordatorioResponse> call = apiService.getRecordatorios(usuarioId);

        call.enqueue(new Callback<RecordatorioResponse>() {
            @Override
            public void onResponse(Call<RecordatorioResponse> call, Response<RecordatorioResponse> response) {
                // Verificar se o fragmento está adjunto antes de interatuar com o contexto ou a interface
                if (!isAdded()) {
                    Log.e("Notificacoes", "Fragmento não está adjunto a uma atividade.");
                    return;
                }

                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_RESPONSE", "Recordatorios carregados corretamente");
                    displayNotificacoes(response.body().getData());
                } else {
                    Log.e("API_ERROR", "Erro ao obter os recordatorios: " + response.code());
                    Toast.makeText(requireContext(), "Erro ao obter as notificações", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecordatorioResponse> call, Throwable t) {
                // Verificar se o fragmento está adjunto antes de interatuar com o contexto ou a interface
                if (!isAdded()) {
                    Log.e("Notificacoes", "Fragmento não está adjunto a uma atividade.");
                    return;
                }

                Log.e("API_ERROR", "Erro de rede: " + t.getMessage());
                Toast.makeText(requireContext(), "Erro de rede ao carregar notificações", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayNotificacoes(List<Recordatorio> recordatorios) {
        // Verificar se o fragmento está adjunto antes de modificar a interface
        if (!isAdded()) {
            Log.e("Notificacoes", "Tentando atualizar a interface de um fragmento não adjunto.");
            return;
        }

        linearLayoutNotificacoes.removeAllViews();

        for (Recordatorio recordatorio : recordatorios) {
            // Mostrar so os recordatorios ativos
            if (recordatorio.getAtivo() == 1) {
                View itemView = LayoutInflater.from(requireContext()).inflate(R.layout.notificacao_item, linearLayoutNotificacoes, false);

                // Configurar dados no item
                TextView tvMedicamentoName = itemView.findViewById(R.id.medicamento_name);
                TextView tvHorario = itemView.findViewById(R.id.horario);
                TextView tvFrequencia = itemView.findViewById(R.id.frequencia);

                String medicamentoNome = recordatorio.getMedicamento() != null ? recordatorio.getMedicamento().getNome() : "Desconocido";
                tvMedicamentoName.setText("Medicamento: " + medicamentoNome);
                tvHorario.setText("Hora: " + recordatorio.getHorario());
                tvFrequencia.setText("Frequência (min): " + recordatorio.getFrequencia());

                // Adicionar o item ao conteiner
                linearLayoutNotificacoes.addView(itemView);
            }
        }
    }

    private int obtenerUsuarioId() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("usuario_id", -1);
    }

}