package com.example.medimate_java;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Recurdatorios extends Fragment {

    private LinearLayout linearLayoutRecordatorios;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recurdatorios, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = view.findViewById(R.id.floating_icon_recordatorios);
        fab.setOnClickListener(v -> {
            Fragment addRecordatorioFragment = new AdicionarRecordatorio();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, addRecordatorioFragment)
                    .addToBackStack(null)
                    .commit();
        });

        linearLayoutRecordatorios = view.findViewById(R.id.linearLayoutRecordatorios);

        fetchRecordatorios();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchRecordatorios();
    }

    private void fetchRecordatorios() {
        int usuarioId = obtenerUsuarioId();
        APIService apiService = RetrofitClient.getRetrofitInstance(requireContext()).create(APIService.class);
        Call<RecordatorioResponse> call = apiService.getRecordatorios(usuarioId);

        call.enqueue(new Callback<RecordatorioResponse>() {
            @Override
            public void onResponse(Call<RecordatorioResponse> call, Response<RecordatorioResponse> response) {
                if (!isAdded()) {
                    Log.e("Recurdatorios", "Fragmento não está adjunto a uma atividade.");
                    return;
                }

                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_RESPONSE", "Recordatorios carregados corretamente");
                    displayRecordatorios(response.body().getData());
                } else {
                    Log.e("API_ERROR", "Erro ao obter os recordatorios: " + response.code());
                    Toast.makeText(requireContext(), "Erro ao obter os recordatorios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecordatorioResponse> call, Throwable t) {
                if (!isAdded()) {
                    Log.e("Recurdatorios", "Fragmento não está adjunto a uma atividade.");
                    return;
                }

                Log.e("API_ERROR", "Erro de rede: " + t.getMessage());
                Toast.makeText(requireContext(), "Erro de rede ao carregar recordatorios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayRecordatorios(List<Recordatorio> recordatorios) {
        if (!isAdded()) {
            Log.e("Recurdatorios", "Tentando atualizar a interface de un fragmento não adjunto.");
            return;
        }

        linearLayoutRecordatorios.removeAllViews();

        for (Recordatorio recordatorio : recordatorios) {
            View itemView = LayoutInflater.from(requireContext()).inflate(R.layout.recordatorio_item, linearLayoutRecordatorios, false);

            TextView tvName = itemView.findViewById(R.id.recordatorio_name);
            TextView tvHorario = itemView.findViewById(R.id.recordatorio_horario);
            TextView tvFrecuencia = itemView.findViewById(R.id.recordatorio_frecuencia);
            TextView tvActivo = itemView.findViewById(R.id.recordatorio_activo);
            ImageView editIcon = itemView.findViewById(R.id.edit_icon);
            ImageView deleteIcon = itemView.findViewById(R.id.delete_icon);

            String medicamentoNome = recordatorio.getMedicamento() != null ? recordatorio.getMedicamento().getNome() : "Desconocido";
            tvName.setText("Medicamento: " + medicamentoNome);

            tvHorario.setText("Horario: " + recordatorio.getHorario());
            tvFrecuencia.setText("Frquência: " + recordatorio.getFrequencia());
            tvActivo.setText("Ativo: " + (recordatorio.getAtivo() == 1 ? "Sim" : "Não"));

            editIcon.setOnClickListener(v -> {
                Fragment editarFragment = new EditarRecordatorio();
                Bundle bundle = new Bundle();
                bundle.putSerializable("recordatorio", recordatorio);
                editarFragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, editarFragment)
                        .addToBackStack(null)
                        .commit();
            });

            deleteIcon.setOnClickListener(v -> eliminarRecordatorio(recordatorio.getId(), itemView));

            linearLayoutRecordatorios.addView(itemView);
        }
    }

    private void eliminarRecordatorio(int id, View itemView) {
        int usuarioId = obtenerUsuarioId();
        APIService apiService = RetrofitClient.getRetrofitInstance(requireContext()).create(APIService.class);

        Call<Void> call = apiService.deleteRecordatorio(id, usuarioId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!isAdded()) {
                    Log.e("Recurdatorios", "Fragmento não está adjunto a uma atividade.");
                    return;
                }

                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Recordatorio eliminado correctamente", Toast.LENGTH_SHORT).show();
                    cancelarAlarma(id);
                    linearLayoutRecordatorios.removeView(itemView);
                } else {
                    Toast.makeText(requireContext(), "Erro ao eliminar recordatorio", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (!isAdded()) {
                    Log.e("Recurdatorios", "Fragmento não está adjunto a uma atividade.");
                    return;
                }

                Toast.makeText(requireContext(), "Erro de rede ao eliminar recordatorio", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void cancelarAlarma(int medicamentoID) {
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);


        Intent intent = new Intent(requireContext(), NotificationReceiver.class);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                medicamentoID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );


        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Log.d("CANCEL_ALARM", "Alarma cancelada para o ID: " + medicamentoID);
        } else {
            Log.e("CANCEL_ALARM", "AlarmManager não disponivel.");
        }


        pendingIntent.cancel();
    }

    private int obtenerUsuarioId() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("usuario_id", -1);
    }
}