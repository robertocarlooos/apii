package com.example.medimate_java;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.Manifest;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EcraPrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frangment_main_screen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }



        View floatingIcon = findViewById(R.id.floating_icon);

        // Configuraçõa do fragmento inicial
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new Fragment_home_screen())
                .commit();


        floatingIcon.setOnClickListener(v -> {
            Fragment addMedicamentoFragment = new AdicionarMedicamento();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, addMedicamentoFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Configuração da navegação  do BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.nav_medicamento) {
                selectedFragment = new Fragment_home_screen();
                floatingIcon.setVisibility(View.VISIBLE); // Mostrar botón flotante
            } else if (item.getItemId() == R.id.nav_recordatorio) {
                selectedFragment = new Recurdatorios();
                floatingIcon.setVisibility(View.GONE);
            } else if (item.getItemId() == R.id.nav_historico) {
                selectedFragment = new Historico();
                floatingIcon.setVisibility(View.GONE);
            } else if (item.getItemId() == R.id.nav_notificacoes) {
                selectedFragment = new Notificacoes();
                floatingIcon.setVisibility(View.GONE);
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        });

        // Listener para o BackStack - Mostrar ou ocultar o button
        fragmentManager.addOnBackStackChangedListener(() -> {
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
            if (currentFragment instanceof Fragment_home_screen || currentFragment instanceof Recurdatorios) {
                floatingIcon.setVisibility(View.VISIBLE);
            } else {
                floatingIcon.setVisibility(View.GONE);
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permisão de notificações concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permição de notificações denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
