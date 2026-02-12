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

                // Comprobamos qué eligió el usuario
                boolean fineLocation = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                boolean coarseLocation = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                if (fineLocation) {
                    // El usuario eligió "Precisa"
                    Toast.makeText(this, "Ubicación EXACTA concedida", Toast.LENGTH_SHORT).show();
                } else if (coarseLocation) {
                    // El usuario eligió "Aproximada"
                    Toast.makeText(this, "Ubicación APROXIMADA concedida", Toast.LENGTH_SHORT).show();
                } else {
                    // No eligió ninguna de las dos: Cerramos la app como pediste
                    Toast.makeText(this, "No se puede usar la app sin ubicación. Cerrando...", Toast.LENGTH_LONG).show();
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