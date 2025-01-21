package com.example.medimate_java;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medimate_java.api.APIService;
import com.example.medimate_java.api.RetrofitClient;
import com.example.medimate_java.api.TokenManager;
import com.example.medimate_java.models.LoginRequest;
import com.example.medimate_java.models.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        emailField = findViewById(R.id.Email_field);
        passwordField = findViewById(R.id.password_field);
        loginButton = findViewById(R.id.login_button_second_page);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Por favor, completa todos os campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginUser(email, password);
            }
        });
    }

    private void loginUser(String email, String password) {
        APIService apiService = RetrofitClient.getRetrofitInstance(this).create(APIService.class);

        LoginRequest loginRequest = new LoginRequest(email, password);

        Call<LoginResponse> call = apiService.login(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();


                    TokenManager tokenManager = new TokenManager(LoginActivity.this);
                    tokenManager.saveToken(loginResponse.getToken());

                    Log.d("Login", "Token: " + loginResponse.getToken());

                    // Redirigir para a actividade principal
                    startActivity(new Intent(LoginActivity.this, EcraPrincipalActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Email ou password incorretas", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("Login", "Error: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }
}