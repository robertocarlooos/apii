package com.example.medimate_java;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.medimate_java.api.APIService;
import com.example.medimate_java.api.RetrofitClient;
import com.example.medimate_java.models.Medicamento;
import com.example.medimate_java.models.MedicamentoResponse;
import com.example.medimate_java.models.Recordatorio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdicionarRecordatorio extends Fragment {

    private EditText etHorario, etFrequencia;
    private AutoCompleteTextView etMedicamento;
    private CheckBox cbAtivo;
    private Button btnSalvar;
    private List<Medicamento> medicamentos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adicionar_recordatorio, container, false);

        etHorario = view.findViewById(R.id.etHorario);
        etFrequencia = view.findViewById(R.id.etFrequencia);
        etMedicamento = view.findViewById(R.id.etMedicamento);
        cbAtivo = view.findViewById(R.id.cbAtivo);
        btnSalvar = view.findViewById(R.id.btnSalvar);

        // Configurar TimePickerDialog
        etHorario.setOnClickListener(v -> showTimePickerDialog());

        // Carregar medicamentos
        loadMedicamentos();

        // Guardar recordatorio
        btnSalvar.setOnClickListener(v -> saveRecordatorio());

        return view;
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, selectedHour, selectedMinute) -> {
                    String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                    etHorario.setText(time);
                },
                hour,
                minute,
                DateFormat.is24HourFormat(requireContext())
        );
        timePickerDialog.show();
    }

    private int medicamentoId = -1;

    private void loadMedicamentos() {
        int usuarioId = obtenerUsuarioId();
        APIService apiService = RetrofitClient.getRetrofitInstance(requireContext()).create(APIService.class);

        Call<MedicamentoResponse> call = apiService.getMedicamentos(usuarioId);

        call.enqueue(new Callback<MedicamentoResponse>() {
            @Override
            public void onResponse(Call<MedicamentoResponse> call, Response<MedicamentoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData() != null && !response.body().getData().isEmpty()) {
                        medicamentos = response.body().getData();


                        List<String> medicamentoNombres = new ArrayList<>();
                        for (Medicamento medicamento : medicamentos) {
                            medicamentoNombres.add(medicamento.getNome());
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                requireContext(),
                                android.R.layout.simple_dropdown_item_1line,
                                medicamentoNombres
                        );
                        etMedicamento.setAdapter(adapter);

                        // Mostrar o dropdown ao fazer click
                        etMedicamento.setOnClickListener(v -> {
                            etMedicamento.requestFocus();
                            etMedicamento.showDropDown();
                        });


                        etMedicamento.setOnItemClickListener((parent, view, position, id) -> {
                            Medicamento medicamentoSeleccionado = medicamentos.get(position);
                            medicamentoId = medicamentoSeleccionado.getId(); // Guarda o ID do medicamento
                            Log.d("MEDICAMENTO_SELECT", "medicamento_id selecionado: " + medicamentoId);
                            Toast.makeText(requireContext(), "Selecionado: " + medicamentoSeleccionado.getNome(), Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        Toast.makeText(requireContext(), "Não ha medicamentos ", Toast.LENGTH_SHORT).show();
                        Log.e("API_RESPONSE", "A lista de medicamentos está vacía.");
                    }
                } else {
                    Toast.makeText(requireContext(), "Erro ao carregar medicamentos: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Código de resposta: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MedicamentoResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Erro de rede ao carregar medicamentos ", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Erro de rede: " + t.getMessage());
            }
        });
    }
    private void saveRecordatorio() {
        int usuarioId = obtenerUsuarioId();
        String horario = etHorario.getText().toString().trim();
        String frequencia = etFrequencia.getText().toString().trim();
        String medicamentoSeleccionado = etMedicamento.getText().toString().trim();
        boolean ativo = cbAtivo.isChecked();

        if (horario.isEmpty() || frequencia.isEmpty() || medicamentoSeleccionado.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, adicione todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int frequenciaInt;
        try {
            frequenciaInt = Integer.parseInt(frequencia);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Frequencia inválida", Toast.LENGTH_SHORT).show();
            return;
        }


        Log.d("MEDICAMENTOS_LISTA", "Lista de medicamentos disponiveis:");
        for (Medicamento medicamento : medicamentos) {
            Log.d("MEDICAMENTO", "ID: " + medicamento.getId() + ", Nome: " + medicamento.getNome());
        }


        int medicamentoId = -1;
        for (Medicamento medicamento : medicamentos) {
            if (medicamento.getNome().equals(medicamentoSeleccionado)) {
                medicamentoId = medicamento.getId();
                break;
            }
        }


        if (medicamentoId == -1) {
            Toast.makeText(requireContext(), "Medicamento inválido ou não encontrado", Toast.LENGTH_SHORT).show();
            Log.e("SAVE_RECORDATORIO", "O medicamento selecionado não tem um ID válido.");
            return;
        }

        Recordatorio recordatorio = new Recordatorio();
        recordatorio.setMedicamentoId(medicamentoId);
        recordatorio.setHorario(horario);
        recordatorio.setFrequencia(frequenciaInt);
        recordatorio.setAtivo(ativo ? 1 : 0);


        Log.d("SAVE_RECORDATORIO", "Dados enviados: " +
                "medicamento_id=" + medicamentoId +
                ", horario=" + horario +
                ", frequencia=" + frequenciaInt +
                ", ativo=" + (ativo ? 1 : 0));

        APIService apiService = RetrofitClient.getRetrofitInstance(requireContext()).create(APIService.class);
        Call<Void> call = apiService.createRecordatorio(recordatorio,usuarioId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("API_RESPONSE", "Recordatorio criado corretamente: " + response.code());
                    Toast.makeText(requireContext(), "Recordatorio guardado corretamente", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack(); // Voltar ao fragmento anterior
                } else {
                    Log.e("API_ERROR", "Erro ao guardar o recordatorio: " + response.code() + " " + response.message());
                    try {
                        Log.e("API_ERROR", "Corpo do erro: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(requireContext(), "Erro ao guardar  recordatorio", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API_ERROR", "Erro de rede ao guardar  recordatorio: " + t.getMessage());
                Toast.makeText(requireContext(), "Erro de rede ao guardar recordatorio", Toast.LENGTH_SHORT).show();
            }
        });
        scheduleNotification(medicamentoSeleccionado, horario, frequenciaInt);
    }
    private void scheduleNotification(String medicamento, String horario, int frequenciaMinutos) {

        String[] parts = horario.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {

            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }


        Intent intent = new Intent(requireContext(), NotificationReceiver.class);
        intent.putExtra("medicamento", medicamento);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                medicamentoId, // ID único por medicamento
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );


        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        long interval = frequenciaMinutos * 60 * 1000;
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                interval,
                pendingIntent
        );


        Log.d("ALARM_MANAGER", "Notifição programada para " + horario + " cada " + frequenciaMinutos + " minutos.");
    }
    private int obtenerUsuarioId() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("usuario_id", -1);
    }

}
