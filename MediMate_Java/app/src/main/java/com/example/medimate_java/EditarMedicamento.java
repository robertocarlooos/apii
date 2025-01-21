package com.example.medimate_java;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.medimate_java.api.APIService;
import com.example.medimate_java.api.RetrofitClient;
import com.example.medimate_java.models.Categoria;
import com.example.medimate_java.models.Medicamento;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarMedicamento extends Fragment {


    private TextInputEditText etNome, etDose, etTipo, etQuantidade;
    private AutoCompleteTextView etCategoria;
    private Button btnGuardar;
    private Medicamento medicamento;
    private List<Categoria> categorias;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_medicamento, container, false);

        // Inicializar vistas
        etNome = view.findViewById(R.id.etNome);
        etDose = view.findViewById(R.id.etDose);
        etTipo = view.findViewById(R.id.etTipo);
        etQuantidade = view.findViewById(R.id.etQuantidade);
        etCategoria = view.findViewById(R.id.etCategoria);
        btnGuardar = view.findViewById(R.id.btnGuardar);

        // Obter os dados do medicamento
        medicamento = (Medicamento) getArguments().getSerializable("medicamento");
        cargarDatos();

        // Carregar as categorías
        loadCategorias();


        btnGuardar.setOnClickListener(v -> guardarCambios());

        return view;
    }

    private void cargarDatos() {
        if (medicamento != null) {
            etNome.setText(medicamento.getNome());
            etDose.setText(medicamento.getDose());
            etTipo.setText(medicamento.getTipo());
            etQuantidade.setText(String.valueOf(medicamento.getQuantidadeAtual()));
            etCategoria.setText(medicamento.getCategoria().getNome());
        }
    }

    private void loadCategorias() {
        int usuarioId = obtenerUsuarioId();
        APIService apiService = RetrofitClient.getRetrofitInstance(getContext()).create(APIService.class);

        Call<List<Categoria>> call = apiService.getCategorias(usuarioId);
        call.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categorias = response.body();
                    Log.d("API_RESPONSE", "Categorías carregadas: " + categorias.size());

                    // Criar adaptador para o AutoCompleteTextView
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            categorias.stream().map(Categoria::getNome).toArray(String[]::new)
                    );
                    etCategoria.setAdapter(adapter);

                    // Configurar a categoría atual do medicamento
                    if (medicamento != null && medicamento.getCategoria() != null) {
                        etCategoria.setText(medicamento.getCategoria().getNome(), false);
                    }

                    // Configurar o evento de clic para mostrar o menú dropdown
                    etCategoria.setOnClickListener(v -> etCategoria.showDropDown());
                } else {
                    Log.e("API_ERROR", "Erro ao carregar categorías: " + response.message());
                    Toast.makeText(getContext(), "Não conseguimos caregar as categorías", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                Log.e("API_ERROR", "Erro de rede ao carregar categorías: " + t.getMessage());
                Toast.makeText(getContext(), "Erro de rede ao carregar categorías", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarCambios() {
        int usuarioId = obtenerUsuarioId();
        String nome = etNome.getText().toString();
        String dose = etDose.getText().toString();
        String tipo = etTipo.getText().toString();
        int quantidade = Integer.parseInt(etQuantidade.getText().toString());
        String categoriaNome = etCategoria.getText().toString();

        int categoriaId = -1;
        for (Categoria cat : categorias) {
            if (cat.getNome().equals(categoriaNome)) {
                categoriaId = cat.getId();
                break;
            }
        }

        if (categoriaId == -1) {
            Toast.makeText(getContext(), "Selecione uma categoría válida", Toast.LENGTH_SHORT).show();
            return;
        }

        medicamento.setNome(nome);
        medicamento.setDose(dose);
        medicamento.setTipo(tipo);
        medicamento.setQuantidadeAtual(quantidade);
        medicamento.setCategoriaId(categoriaId);

        APIService apiService = RetrofitClient.getRetrofitInstance(getContext()).create(APIService.class);
        Call<Void> call = apiService.updateMedicamento(medicamento.getId(), medicamento,usuarioId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Medicamento atualizado com éxito", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                } else {
                    Log.e("API_ERROR", "Erro ao atualizar medicamento: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API_ERROR", "Erro de rede ao atualizar medicamento: " + t.getMessage());
            }
        });
    }
    private int obtenerUsuarioId() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("usuario_id", -1);
    }
}