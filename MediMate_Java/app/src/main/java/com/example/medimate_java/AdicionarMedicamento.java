package com.example.medimate_java;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.medimate_java.api.APIService;
import com.example.medimate_java.api.RetrofitClient;
import com.example.medimate_java.models.Categoria;
import com.example.medimate_java.models.Medicamento;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdicionarMedicamento extends Fragment {

    private List<Categoria> categorias;
    private TextInputEditText etNome, etDose, etTipo, etQuantidade;
    private AutoCompleteTextView etCategoria;
    private TextView tvDescripcion;
    private Button btnAdicionar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adicionar_medicamento, container, false);


        etNome = view.findViewById(R.id.etNome);
        etDose = view.findViewById(R.id.etDose);
        etTipo = view.findViewById(R.id.etTipo);
        etQuantidade = view.findViewById(R.id.etQuantidade);
        etCategoria = view.findViewById(R.id.etCategoria);
        tvDescripcion = view.findViewById(R.id.tvDescripcion);
        btnAdicionar = view.findViewById(R.id.btnAdicionar);

        // Carregar categorías e configurar descrição
        loadCategorias();

        // Configurar button  para adicionar medicamento
        btnAdicionar.setOnClickListener(v -> adicionarMedicamento());

        return view;
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
                    List<String> categoriasNombres = new ArrayList<>();
                    for (Categoria categoria : categorias) {
                        categoriasNombres.add(categoria.getNome());
                    }


                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            categoriasNombres
                    );
                    etCategoria.setAdapter(adapter);


                    etCategoria.setOnClickListener(v -> {
                        if (!categoriasNombres.isEmpty()) {
                            etCategoria.showDropDown();
                        }
                    });


                    etCategoria.setOnItemClickListener((parent, view, position, id) -> {
                        Categoria categoriaSeleccionada = categorias.get(position);
                        tvDescripcion.setText(categoriaSeleccionada.getDescricao());
                        tvDescripcion.setVisibility(View.VISIBLE);
                    });
                } else {
                    Toast.makeText(getContext(), "Não conseguimos carregar as categorias", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                Toast.makeText(getContext(), "Erro de rede ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void adicionarMedicamento() {
        int usuarioId = obtenerUsuarioId();
        String nome = etNome.getText().toString().trim();
        String dose = etDose.getText().toString().trim();
        String tipo = etTipo.getText().toString().trim();
        String quantidade = etQuantidade.getText().toString().trim();
        String categoriaSeleccionada = etCategoria.getText().toString().trim();

        if (nome.isEmpty() || dose.isEmpty() || tipo.isEmpty() || quantidade.isEmpty() || categoriaSeleccionada.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, complete todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantidadeInt;
        try {
            quantidadeInt = Integer.parseInt(quantidade);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Por favor, adicione um número válido para a quantidade", Toast.LENGTH_SHORT).show();
            return;
        }

        // Procurar  ID da categoría selecionada
        int categoriaId = -1;
        for (Categoria cat : categorias) {
            if (cat.getNome().equals(categoriaSeleccionada)) {
                categoriaId = cat.getId();
                break;
            }
        }

        if (categoriaId == -1) {
            Toast.makeText(getContext(), "Categoría inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        // Criar objeto Medicamento com o ID da categoría selecionada
        Medicamento medicamento = new Medicamento(nome, dose, tipo, quantidadeInt, categoriaId);

        // Enviar medicamento à API
        APIService apiService = RetrofitClient.getRetrofitInstance(getContext()).create(APIService.class);
        Call<Void> call = apiService.createMedicamento(medicamento,usuarioId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Medicamento adicionado corretamente", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Erro ao adicionar medicamento", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Erro de rede ao adicionar medicamento", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private int obtenerUsuarioId() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("usuario_id", -1);
    }
}