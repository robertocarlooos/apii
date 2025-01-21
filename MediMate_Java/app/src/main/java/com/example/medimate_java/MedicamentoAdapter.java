package com.example.medimate_java;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medimate_java.api.APIService;
import com.example.medimate_java.api.RetrofitClient;
import com.example.medimate_java.models.Medicamento;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicamentoAdapter extends RecyclerView.Adapter<MedicamentoAdapter.MedicamentoViewHolder> {

    private final List<Medicamento> medicamentos;
    private final Context context;

    // Construtor para passar a lista de medicamentos e o contexto
    public MedicamentoAdapter(List<Medicamento> medicamentos, Context context) {
        this.medicamentos = medicamentos;
        this.context = context;
    }

    @NonNull
    @Override
    public MedicamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicamento_item, parent, false);
        return new MedicamentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicamentoViewHolder holder, int position) {
        Medicamento medicamento = medicamentos.get(position);

        // Configurar os dados nas visualizações do ViewHolder
        holder.medicamentoTitle.setText("Medicamento: " + medicamento.getNome());
        holder.medicamentoName.setText("Nome: " + medicamento.getNome());
        holder.medicamentoDose.setText("Dose: " + medicamento.getDose());
        holder.medicamentoTipo.setText("Tipo: " + medicamento.getTipo());
        holder.medicamentoQuantidade.setText("Quantidade: " + medicamento.getQuantidadeAtual());
        holder.medicamentoCategoria.setText("Categoria: " + medicamento.getCategoria());

        // Configurar a funcionalidade do ícone de exclusão
        holder.deleteIcon.setOnClickListener(v -> deleteMedicamento(medicamento.getId(), position));
    }

    private void deleteMedicamento(int id, int position) {
        int usuarioId = obtenerUsuarioId();
        if (usuarioId == -1) {
            Log.e("MedicamentoAdapter", "Usuário ID não encontrado no SharedPreferences.");
            return;
        }

        APIService apiService = RetrofitClient.getRetrofitInstance(context).create(APIService.class);
        Call<Void> call = apiService.deleteMedicamento(id, usuarioId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Remover o medicamento da lista e notificar o adaptador
                    medicamentos.remove(position);
                    notifyItemRemoved(position);
                    Log.d("MedicamentoAdapter", "Medicamento excluído com sucesso.");
                } else {
                    Log.e("MedicamentoAdapter", "Erro ao excluir medicamento: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("MedicamentoAdapter", "Erro de rede ao excluir medicamento: " + t.getMessage());
            }
        });
    }

    private int obtenerUsuarioId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("usuario_id", -1); // Retorna -1 se não encontrado
    }

    @Override
    public int getItemCount() {
        return medicamentos.size();
    }

    // Classe interna do ViewHolder
    static class MedicamentoViewHolder extends RecyclerView.ViewHolder {
        TextView medicamentoTitle, medicamentoName, medicamentoDose, medicamentoTipo, medicamentoQuantidade, medicamentoCategoria;
        ImageView deleteIcon;

        MedicamentoViewHolder(View itemView) {
            super(itemView);
            medicamentoTitle = itemView.findViewById(R.id.Medicamento);
            medicamentoName = itemView.findViewById(R.id.medicamento_name);
            medicamentoDose = itemView.findViewById(R.id.medicamento_dose);
            medicamentoTipo = itemView.findViewById(R.id.mediamento_tipo);
            medicamentoQuantidade = itemView.findViewById(R.id.medicameto_quantidade);
            medicamentoCategoria = itemView.findViewById(R.id.medicamento_categoria);
            deleteIcon = itemView.findViewById(R.id.delete_icon);
        }
    }
}