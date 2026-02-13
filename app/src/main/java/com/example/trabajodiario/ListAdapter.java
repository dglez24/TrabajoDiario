package com.example.trabajodiario;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ListAdapter extends CursorAdapter {

    public ListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    // Crea la vista por primera vez
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_view, parent, false);
    }

    // Une los datos del cursor con la vista
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvTitulo = view.findViewById(R.id.Titulo);
        TextView tvDescripcion = view.findViewById(R.id.Descripcion);


        // Extraer datos del cursor usando los nombres de las columnas de tu SqlLiteHelper
        String titulo = cursor.getString(cursor.getColumnIndexOrThrow(SqlLiteHelper.CAMPO_TITULO));
        String fecha = cursor.getString(cursor.getColumnIndexOrThrow(SqlLiteHelper.CAMPO_FECHA));
        double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(SqlLiteHelper.CAMPO_LATITUD));
        double lon = cursor.getDouble(cursor.getColumnIndexOrThrow(SqlLiteHelper.CAMPO_LONGITUD));

        // Asignar a los elementos visuales
        tvTitulo.setText(titulo);
        tvDescripcion.setText("Descripcion: " + fecha);

    }
}
