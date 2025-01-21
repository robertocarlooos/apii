package com.example.medimate_java;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.medimate_java.api.APIService;
import com.example.medimate_java.api.RetrofitClient;
import com.example.medimate_java.models.Categoria;
import com.example.medimate_java.models.Medicamento;
import com.example.medimate_java.models.MedicamentoResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_home_screen extends Fragment {

    private static final String TAG = "FragmentHomeScreen";
    private LinearLayout containerMedicamentos;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        ImageView iconLogout = view.findViewById(R.id.iconLogout);
        iconLogout.setOnClickListener(v -> realizarLogout());
        // Inicializar o conteiner do medicamentos
        containerMedicamentos = view.findViewById(R.id.container_medicamentos);
        EditText searchBox = view.findViewById(R.id.searchBox);

        // Configurar evento para o texto em tempo rial
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    fetchMedicamentos(); // Mostrar todos si não ha texto
                } else {
                    filtrarMedicamentos(query); // Chamar a API para filtrar medicamentos
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        // Carregar categorías e medicamentos
        fetchMedicamentos();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchMedicamentos();
    }

    private void fetchMedicamentos() {
        int usuarioId = obtenerUsuarioId();
        if (getContext() == null || containerMedicamentos == null) {
            Log.e(TAG, "Contexto o contenedor de medicamentos no inicializado.");
            return;
        }
        APIService apiService = RetrofitClient.getRetrofitInstance(getContext()).create(APIService.class);
        Call<MedicamentoResponse> call = apiService.getMedicamentos(usuarioId);

        call.enqueue(new Callback<MedicamentoResponse>() {
            @Override
            public void onResponse(Call<MedicamentoResponse> call, Response<MedicamentoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Medicamento> medicamentos = response.body().getData();
                    Log.d(TAG, "Medicamentos carregados: " + medicamentos.size());

                    // Mostrar os medicamentos no ScrollView
                    mostrarMedicamentos(medicamentos);
                } else {
                    Log.e(TAG, "Erro ao carregar medicamentos: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MedicamentoResponse> call, Throwable t) {
                Log.e(TAG, "Erro de rede: " + t.getMessage());
            }
        });
    }

    private void mostrarMedicamentos(List<Medicamento> medicamentos) {
        containerMedicamentos.removeAllViews(); // Limpar vistas previas

        for (Medicamento medicamento : medicamentos) {

            View medicamentoItem = LayoutInflater.from(getContext()).inflate(R.layout.medicamento_item, containerMedicamentos, false);

            // Obter as vistas dentro do layout
            TextView tvMedicamento = medicamentoItem.findViewById(R.id.Medicamento);
            TextView tvNome = medicamentoItem.findViewById(R.id.medicamento_name);
            TextView tvDose = medicamentoItem.findViewById(R.id.medicamento_dose);
            TextView tvTipo = medicamentoItem.findViewById(R.id.mediamento_tipo);
            TextView tvQuantidade = medicamentoItem.findViewById(R.id.medicameto_quantidade);
            TextView tvCategoria = medicamentoItem.findViewById(R.id.medicamento_categoria);

            // Configurar os dados do medicamento
            tvMedicamento.setText("Medicamento");
            tvNome.setText("Nome: " + medicamento.getNome());
            tvDose.setText("Dose: " + medicamento.getDose());
            tvTipo.setText("Tipo: " + medicamento.getTipo());
            tvQuantidade.setText("Quantidade: " + medicamento.getQuantidadeAtual());

            // Configurar a categoría do medicamento
            String categoriaNome = medicamento.getCategoria() != null ? medicamento.getCategoria().getNome() : "Desconhecida";
            tvCategoria.setText("Categoria: " + categoriaNome);

            // Configurar eventos para os íconos
            medicamentoItem.findViewById(R.id.edit_icon).setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("medicamento", medicamento); // Pasar o medicamento selecionado
                EditarMedicamento editarFragment = new EditarMedicamento();
                editarFragment.setArguments(bundle);

                // Navegar ao fragmento de edição
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, editarFragment) // Utiliza o ID del conteiner do fragmento
                        .addToBackStack(null)
                        .commit();
            });

            medicamentoItem.findViewById(R.id.delete_icon).setOnClickListener(v -> {
                eliminarMedicamento(medicamento);
            });
            medicamentoItem.findViewById(R.id.done_icon).setOnClickListener(v -> {
                // Chamar a função para reducir a quantidade e registar no historico
                marcarYReducir(medicamento);


                Toast.makeText(getContext(), "O medicamento foi registado como tomado.", Toast.LENGTH_SHORT).show();
            });



            // Adicionar o desenho do medicamento ao conteiner
            containerMedicamentos.addView(medicamentoItem);
        }
    }
    private void marcarYReducir(Medicamento medicamento) {
        int usuarioId = obtenerUsuarioId();
        APIService apiService = RetrofitClient.getRetrofitInstance(getContext()).create(APIService.class);

        Call<Void> call = apiService.marcarYReducir(medicamento.getId(), usuarioId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Reducir a quantidade na interface
                    medicamento.setQuantidadeAtual(medicamento.getQuantidadeAtual() - 1);

                    // Atualizar a lista visivel de medicamentos
                    fetchMedicamentos();

                    // Mensagem de éxito
                    Toast.makeText(getContext(), "O medicamento foi registrado como tomado.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("API_ERROR", "Erro ao marcar como tomado: " + response.message());
                    Toast.makeText(getContext(), "Erro  ao registar o medicamento.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API_ERROR", "Erro de rede ao marcar como tomado: " + t.getMessage());
                Toast.makeText(getContext(), "Erro de rede.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarMedicamento(Medicamento medicamento) {
        int usuarioId = obtenerUsuarioId();
        APIService apiService = RetrofitClient.getRetrofitInstance(getContext()).create(APIService.class);
        Call<Void> call = apiService.deleteMedicamento(medicamento.getId(),usuarioId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Medicamento apagado: " + medicamento.getNome());

                    fetchMedicamentos();
                } else {
                    Log.e(TAG, "Erro ao eliminar medicamento: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Erro de rede ao eliminar medicamento: " + t.getMessage());
            }
        });
    }
    private void agregarHistorico(Medicamento medicamento) {
        LinearLayout linearLayoutHistorico = getView().findViewById(R.id.linearLayoutHistorico);

        // Criar um novo elemento do histórico
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.historico_item, linearLayoutHistorico, false);

        // Configurar os dados
        TextView tvMedicamento = itemView.findViewById(R.id.medicamento_name);
        TextView tvDataToma = itemView.findViewById(R.id.data_toma);
        TextView tvHoraToma = itemView.findViewById(R.id.hora_toma);
        TextView tvEstado = itemView.findViewById(R.id.estado);
        tvMedicamento.setText("Medicamento: " + medicamento.getNome());
        tvDataToma.setText("Data de toma: " + getCurrentDate());
        tvHoraToma.setText("Hora: " + getCurrentTime());
        tvEstado.setText("Estado: Tomado");

        // Adicionar o elemento ao conteiner do histórico
        linearLayoutHistorico.addView(itemView, 0);
    }


    private void filtrarMedicamentos(String query) {
        int usuarioId = obtenerUsuarioId();
        APIService apiService = RetrofitClient.getRetrofitInstance(getContext()).create(APIService.class);

        Call<MedicamentoResponse> call = apiService.getMedicamentos(usuarioId);
        call.enqueue(new Callback<MedicamentoResponse>() {
            @Override
            public void onResponse(Call<MedicamentoResponse> call, Response<MedicamentoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Medicamento> medicamentos = response.body().getData();
                    List<Medicamento> medicamentosFiltrados = new ArrayList<>();

                    // Filtrar medicamentos por nome
                    for (Medicamento medicamento : medicamentos) {
                        if (medicamento.getNome().toLowerCase().contains(query.toLowerCase())) {
                            medicamentosFiltrados.add(medicamento);
                        }
                    }

                    // Mostrar medicamentos filtrados
                    mostrarMedicamentos(medicamentosFiltrados);
                } else {
                    Toast.makeText(getContext(), "Erro ao filtrar medicamentos.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MedicamentoResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Erro de rede ao filtrar medicamentos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return timeFormat.format(new Date());
    }
    private int obtenerUsuarioId() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("usuario_id", -1);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            realizarLogout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void realizarLogout() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();


        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

}