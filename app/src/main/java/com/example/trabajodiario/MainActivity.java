package com.example.trabajodiario;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {

                // 1. Verificamos ubicación (Obligatoria para la práctica)
                boolean fineLocation = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                boolean coarseLocation = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                // 2. Verificamos acceso al sistema/música (Opcional)
                boolean storageGranted;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    storageGranted = result.getOrDefault(Manifest.permission.READ_MEDIA_AUDIO, false);
                } else {
                    storageGranted = result.getOrDefault(Manifest.permission.READ_EXTERNAL_STORAGE, false);
                }

                // Lógica de decisión
                if (fineLocation || coarseLocation) {
                    // Si aceptó ubicación pero NO el acceso al sistema
                    if (!storageGranted) {
                        Toast.makeText(this, "Ubicación lista. No podrás elegir música personalizada, pero puedes usar la app.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Todos los permisos concedidos.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Si denegó la ubicación (crítico), cerramos
                    Toast.makeText(this, "Permiso de ubicación denegado. La app se cerrará.", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        checkPermissions();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        // Dentro de tu MainActivity.java

        Button btnEntradas = findViewById(R.id.btnEntradas);
        btnEntradas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
        /*Button btnEntradas = findViewById(R.id.btnEntradas);
        btnEntradas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                startActivity(intent);
            }
        });*/
    }
    private void checkPermissions() {
        ArrayList<String> permissionsList = new ArrayList<>();

        // Añadimos ambos niveles de ubicación para que el sistema permita elegir
        permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        // Gestión de audio según versión (requisito multimedia de la práctica)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            permissionsList.add(Manifest.permission.READ_MEDIA_AUDIO);
        } else {
            permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        }

        String[] permissions = permissionsList.toArray(new String[0]);
        requestPermissionLauncher.launch(permissions);
    }

}