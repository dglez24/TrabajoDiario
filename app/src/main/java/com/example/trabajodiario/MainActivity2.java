package com.example.trabajodiario;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity2 extends AppCompatActivity {

    private ListView listView;
    private SqlLiteHelper dbHelper;
    private ListAdapter adapter;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        // Inicializar Base de Datos y Ubicación
        dbHelper = new SqlLiteHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Referencia al ListView del layout
        listView = findViewById(R.id.ListView); // Asegúrate de que este ID esté en activity_main2.xml

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        configurarLista();

        Button btnVolver = findViewById(R.id.BtnVolver);
        if (btnVolver != null) {
            btnVolver.setOnClickListener(v -> finish());
        }

        Button btnNuevo = findViewById(R.id.BtnNuevo);
        if (btnNuevo != null) {
            btnNuevo.setOnClickListener(v -> mostrarFormularioProgramatico());
        }
    }

    private void configurarLista() {
        // Obtenemos los datos. Importante: id AS _id para el CursorAdapter
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id AS _id, " + SqlLiteHelper.CAMPO_TITULO + ", " +
                SqlLiteHelper.CAMPO_FECHA + ", " + SqlLiteHelper.CAMPO_LATITUD + ", " +
                SqlLiteHelper.CAMPO_LONGITUD + ", " + SqlLiteHelper.CAMPO_AUDIO_URI +
                " FROM " + SqlLiteHelper.TABLA_EVENTOS + " ORDER BY id DESC", null);

        adapter = new ListAdapter(this, cursor, 0);
        listView.setAdapter(adapter);
    }

    // Método para refrescar la lista después de guardar
    private void actualizarLista() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor nuevoCursor = db.rawQuery("SELECT id AS _id, titulo, fecha, latitud, longitud, audio_uri FROM eventos ORDER BY id DESC", null);
        adapter.changeCursor(nuevoCursor);
    }

    private void obtenerUbicacionYGuardar(String titulo, String descripcion, String musica) {
        // Verificamos permisos antes de pedir ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                double lat = (location != null) ? location.getLatitude() : 0.0;
                double lon = (location != null) ? location.getLongitude() : 0.0;

                guardarEnDB(titulo, descripcion, lat, lon, musica);
            });
        } else {
            // Si no hay permisos, guardamos con 0,0 para que no falle la app
            guardarEnDB(titulo, descripcion, 0.0, 0.0, musica);
        }
    }

    private void guardarEnDB(String t, String d, double lat, double lon, String musica) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlLiteHelper.CAMPO_TITULO, t);
        values.put(SqlLiteHelper.CAMPO_DESCRIPCION, d);
        values.put(SqlLiteHelper.CAMPO_LATITUD, lat);
        values.put(SqlLiteHelper.CAMPO_LONGITUD, lon);
        values.put(SqlLiteHelper.CAMPO_AUDIO_URI, musica);

        long id = db.insert(SqlLiteHelper.TABLA_EVENTOS, null, values);
        if (id != -1) {
            Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
            actualizarLista(); // Refrescamos el ListView automáticamente
        }
        db.close();
    }

    void mostrarFormularioProgramatico() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 40);

        // 1. Campos de texto
        EditText etTitulo = new EditText(this);
        etTitulo.setHint("Título (Obligatorio)");

        EditText etDescripcion = new EditText(this);
        etDescripcion.setHint("Descripción (Obligatorio)");
        etDescripcion.setMinLines(3);

        // 2. Título para la selección de música
        TextView tvMusica = new TextView(this);
        tvMusica.setText("\nSelecciona una música (Opcional):");
        tvMusica.setPadding(0, 20, 0, 10);

        // 3. Crear el RadioGroup para los botones de opción
        RadioGroup rgMusica = new RadioGroup(this);

        RadioButton rb1 = new RadioButton(this);
        rb1.setText("Cancion 1");
        rb1.setId(View.generateViewId()); // Generar ID para identificarlo

        RadioButton rb2 = new RadioButton(this);
        rb2.setText("Cancion 2");
        rb2.setId(View.generateViewId());

        RadioButton rb3 = new RadioButton(this);
        rb3.setText("Cancion 3");
        rb3.setId(View.generateViewId());

        // Añadir RadioButtons al grupo
        rgMusica.addView(rb1);
        rgMusica.addView(rb2);
        rgMusica.addView(rb3);

        // 4. Añadir todo al layout principal
        layout.addView(etTitulo);
        layout.addView(etDescripcion);
        layout.addView(tvMusica);
        layout.addView(rgMusica);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Nuevo Recuerdo")
                .setView(layout)
                .setPositiveButton("Guardar", null)
                .setNegativeButton("Cancelar", (d, w) -> d.dismiss())
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                String tit = etTitulo.getText().toString().trim();
                String desc = etDescripcion.getText().toString().trim();

                if (tit.isEmpty() || desc.isEmpty()) {
                    Toast.makeText(this, "Título y Descripción son obligatorios", Toast.LENGTH_SHORT).show();
                } else {
                    // Lógica para saber qué música se seleccionó
                    String musicaElegida = "";
                    int selectedId = rgMusica.getCheckedRadioButtonId();

                    if (selectedId == rb1.getId()) {
                        musicaElegida = "pista1";
                    } else if (selectedId == rb2.getId()) {
                        musicaElegida = "pista2";
                    } else if (selectedId == rb3.getId()) {
                        musicaElegida = "pista3";
                    }

                    // Guardamos con la música elegida (o vacía si no marcó nada)
                    obtenerUbicacionYGuardar(tit, desc, musicaElegida);
                    dialog.dismiss();
                }
            });
        });

        dialog.show();
    }

}
