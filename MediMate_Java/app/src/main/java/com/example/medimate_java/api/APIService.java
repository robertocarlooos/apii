package com.example.medimate_java.api;

import com.example.medimate_java.models.Categoria;
import com.example.medimate_java.models.HistorialTomaResponse;
import com.example.medimate_java.models.LoginRequest;
import com.example.medimate_java.models.LoginResponse;
import com.example.medimate_java.models.Medicamento;
import com.example.medimate_java.models.MedicamentoResponse;
import com.example.medimate_java.models.Recordatorio;
import com.example.medimate_java.models.RecordatorioResponse;
import com.example.medimate_java.models.RegisterRequest;
import com.example.medimate_java.models.RegisterResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @Headers("Content-Type: application/json")
    @POST("register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @Headers("Content-Type: application/json")
    @GET("medicamentos")
    Call<MedicamentoResponse> getMedicamentos(@Query("usuario_id") int usuarioId);

    @Headers("Content-Type: application/json")
    @DELETE("medicamentos/{id}")
    Call<Void> deleteMedicamento(@Path("id") int id, @Query("usuario_id") int usuarioId);

    @Headers("Content-Type: application/json")
    @GET("categorias")
    Call<List<Categoria>> getCategorias(@Query("usuario_id") int usuarioId);

    @Headers("Content-Type: application/json")
    @POST("medicamentos")
    Call<Void> createMedicamento(@Body Medicamento medicamento, @Query("usuario_id") int usuarioId);

    @Headers("Content-Type: application/json")
    @PUT("medicamentos/{id}")
    Call<Void> updateMedicamento(@Path("id") int id, @Body Medicamento medicamento, @Query("usuario_id") int usuarioId);

    @Headers("Content-Type: application/json")
    @GET("historico")
    Call<HistorialTomaResponse> getHistorico(@Query("usuario_id") int usuarioId);

    @Headers("Content-Type: application/json")
    @POST("medicamentos/{id}/marcar-y-reducir")
    Call<Void> marcarYReducir(@Path("id") int id, @Query("usuario_id") int usuarioId);

    @Headers("Content-Type: application/json")
    @DELETE("historial_tomas/{id}")
    Call<Void> deleteHistorial(@Path("id") int id, @Query("usuario_id") int usuarioId);

    @Headers("Content-Type: application/json")
    @GET("recordatorios")
    Call<RecordatorioResponse> getRecordatorios(@Query("usuario_id") int usuarioId);

    @Headers("Content-Type: application/json")
    @DELETE("recordatorios/{id}")
    Call<Void> deleteRecordatorio(@Path("id") int id, @Query("usuario_id") int usuarioId);

    @Headers("Content-Type: application/json")
    @POST("recordatorios")
    Call<Void> createRecordatorio(@Body Recordatorio recordatorio, @Query("usuario_id") int usuarioId);

    @Headers("Content-Type: application/json")
    @PUT("recordatorios/{id}")
    Call<Void> updateRecordatorio(@Path("id") int id, @Body Recordatorio recordatorio, @Query("usuario_id") int usuarioId);

    @Headers("Content-Type: application/json")
    @GET("medicamentos/filtrar")
    Call<MedicamentoResponse> filtrarMedicamentos(
            @Query("usuario_id") int usuarioId,
            @Query("nombre") String nombre
    );
}
