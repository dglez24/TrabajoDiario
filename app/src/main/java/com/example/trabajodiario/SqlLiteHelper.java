package com.example.trabajodiario;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqlLiteHelper extends SQLiteOpenHelper {
    // Nombre de la base de datos y versión
    public static final String DATABASE_NAME = "diario_recuerdos.db";
    public static final int DATABASE_VERSION = 1;

    // Estructura de la tabla 'eventos' según el enunciado [cite: 39]
    public static final String TABLA_EVENTOS = "eventos";
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_TITULO = "titulo"; // Obligatorio [cite: 30]
    public static final String CAMPO_DESCRIPCION = "descripcion"; // Obligatorio [cite: 31]
    public static final String CAMPO_FECHA = "fecha"; // Automática
    public static final String CAMPO_LATITUD = "latitud"; // Coordenadas
    public static final String CAMPO_LONGITUD = "longitud"; // Coordenadas
    public static final String CAMPO_AUDIO_URI = "audio_uri"; // Opcional [cite: 33]

    // Sentencia SQL para crear la tabla [cite: 39]
    private static final String CREAR_TABLA_EVENTOS = "CREATE TABLE " + TABLA_EVENTOS + " (" +
            CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAMPO_TITULO + " TEXT NOT NULL, " +
            CAMPO_DESCRIPCION + " TEXT NOT NULL, " +
            CAMPO_FECHA + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            CAMPO_LATITUD + " REAL, " +
            CAMPO_LONGITUD + " REAL, " +
            CAMPO_AUDIO_URI + " TEXT)";
    public SqlLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Ejecuta la creación de la tabla [cite: 38]
        db.execSQL(CREAR_TABLA_EVENTOS);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // En caso de actualizar la app, borramos la versión anterior
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_EVENTOS);
        onCreate(db);
    }
}
