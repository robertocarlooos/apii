package com.example.medimate_java;

import android.app.TimePickerDialog;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarRecordatorio extends Fragment {

    private EditText etHorario, etFrequencia;
    private CheckBox cbAtivo;
    private Button btnActualizar;
    private Recordatorio recordatorioExistente;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_recordatorio, container, false);

        etHorario = view.findViewById(R.id.etHorario);
        etFrequencia = view.findViewById(R.id.etFrequencia);
        cbAtivo = view.findViewById(R.id.cbAtivo);
        btnActualizar = view.findViewById(R.id.btnSalvar);
        btnActualizar.setText("Atualizar Recordatorio");

        // Configurar TimePickerDialog
        etHorario.setOnClickListener(v -> showTimePickerDialog());


        if (getArguments() != null) {
            recordatorioExistente = (Recordatorio) getArguments().getSerializable("recordatorio");
            if (recordatorioExistente != null) {
                populateFields();
            }
        }

        // Atualizar recordatorio
        btnActualizar.setOnClickListener(v -> updateRecordatorio());

        return view;
    }

    private void populateFields() {
        etHorario.setText(recordatorioExistente.getHorario());
        etFrequencia.setText(String.valueOf(recordatorioExistente.getFrequencia()));
        cbAtivo.setChecked(recordatorioExistente.getAtivo() == 1);
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

    private void updateRecordatorio() {
        int usuarioId = obtenerUsuarioId();
        String horario = etHorario.getText().toString().trim();
        String frequencia = etFrequencia.getText().toString().trim();
        boolean ativo = cbAtivo.isChecked();

        if (horario.isEmpty() || frequencia.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, complete todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int frequenciaInt;
        try {
            frequenciaInt = Integer.parseInt(frequencia);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Frequência inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        recordatorioExistente.setHorario(horario);
        recordatorioExistente.setFrequencia(frequenciaInt);
        recordatorioExistente.setAtivo(ativo ? 1 : 0);

        APIService apiService = RetrofitClient.getRetrofitInstance(requireContext()).create(APIService.class);
        Call<Void> call = apiService.updateRecordatorio(recordatorioExistente.getId(), recordatorioExistente, usuarioId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Recordatorio atualizado", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(requireContext(), "Erro ao atualizar o recordatorio", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(requireContext(), "Erro de rede ao atualizar o recordatorio", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private int obtenerUsuarioId() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("usuario_id", -1);
    }
}