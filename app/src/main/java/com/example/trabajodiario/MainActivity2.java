package com.example.trabajodiario;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    private String rutaAudioSeleccionado = ""; // Variable para guardar la ruta del audio

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnVolver = findViewById(R.id.BtnVolver);
        if (btnVolver != null) {
            btnVolver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Volvemos a la actividad anterior (que suele ser MainActivity)
                    finish();
                }
            });
        }
    }

    // El método debe estar fuera de onCreate para evitar el error ';' expected
    void mostrarFormularioProgramatico() {
        // 1. Crear el contenedor principal (LinearLayout)
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 40);

        // 2. Crear los campos de texto
        EditText etTitulo = new EditText(this);
        etTitulo.setHint("Título (Obligatorio)");

        EditText etDescripcion = new EditText(this);
        etDescripcion.setHint("Descripción (Obligatorio)");
        etDescripcion.setMinLines(3);

        // 3. Botón para el audio (Opcional)
        Button btnAudio = new Button(this);
        btnAudio.setText("Seleccionar Música");

        TextView tvAudioEstado = new TextView(this);
        tvAudioEstado.setText("Sin música seleccionada");

        // 4. Añadir las vistas al layout
        layout.addView(etTitulo);
        layout.addView(etDescripcion);
        layout.addView(btnAudio);
        layout.addView(tvAudioEstado);

        // 5. Crear el AlertDialog que contendrá el layout
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Nuevo Recuerdo")
                .setView(layout) 
                .setPositiveButton("Guardar", null) 
                .setNegativeButton("Cancelar", (d, w) -> d.dismiss())
                .create();

        // 6. Lógica del botón Guardar
        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                String tit = etTitulo.getText().toString().trim();
                String desc = etDescripcion.getText().toString().trim();

                if (tit.isEmpty() || desc.isEmpty()) {
                    Toast.makeText(this, "Título y Descripción son obligatorios", Toast.LENGTH_SHORT).show();
                } else {
                    obtenerUbicacionYGuardar(tit, desc, rutaAudioSeleccionado);
                    dialog.dismiss();
                }
            });
        });

        dialog.show();
    }

    private void obtenerUbicacionYGuardar(String titulo, String descripcion, String rutaAudio) {
        // Implementación pendiente
        Toast.makeText(this, "Guardando: " + titulo, Toast.LENGTH_SHORT).show();
    }
}