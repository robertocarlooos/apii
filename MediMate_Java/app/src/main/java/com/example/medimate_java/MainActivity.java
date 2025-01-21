package com.example.medimate_java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Certifique-se de que essa chamada está configurada corretamente no seu projeto.
        setContentView(R.layout.activity_main);

        configureButton();
    }

    private void configureButton() {
        Button getStartedButton = findViewById(R.id.get_started_button);
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar para a SecondActivity
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }
}
