package com.acevedo.rutaexperienciauc.util.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.acevedo.rutaexperienciauc.clases.Favorito;

import java.util.ArrayList;
import java.util.List;

public class FavoritosDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favoritos.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_FAVORITOS = "favoritos";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ID_EXPERIENCIA = "id_experiencia";
    private static final String COLUMN_NOMBRE_EXPERIENCIA = "nombre_experiencia";
    private static final String COLUMN_IS_FAVORITE = "is_favorite";

    private static final String CREATE_TABLE_FAVORITOS = "CREATE TABLE " + TABLE_FAVORITOS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_ID_EXPERIENCIA + " INTEGER," +
            COLUMN_NOMBRE_EXPERIENCIA + " TEXT," +
            COLUMN_IS_FAVORITE + " INTEGER)";

    public FavoritosDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FAVORITOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Manejo de actualizaciones futuras de la base de datos
    }

    public void guardarExperienciaFavorita(int idExperiencia, String nombreExperiencia) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_EXPERIENCIA, idExperiencia);
        values.put(COLUMN_NOMBRE_EXPERIENCIA, nombreExperiencia);
        values.put(COLUMN_IS_FAVORITE, 1); // Marcamos como favorito

        db.insert(TABLE_FAVORITOS, null, values);
        db.close();
    }

    public void eliminarExperienciaFavorita(int idExperiencia) {
        SQLiteDatabase db = getWritableDatabase();

        String selection = COLUMN_ID_EXPERIENCIA + " = ?";
        String[] selectionArgs = {String.valueOf(idExperiencia)};

        db.delete(TABLE_FAVORITOS, selection, selectionArgs);
        db.close();
    }

    public boolean isExperienciaFavorita(int idExperiencia) {
        SQLiteDatabase db = getReadableDatabase();

        String selection = COLUMN_ID_EXPERIENCIA + " = ?";
        String[] selectionArgs = {String.valueOf(idExperiencia)};

        Cursor cursor = db.query(TABLE_FAVORITOS, null, selection, selectionArgs, null, null, null);
        boolean isFavorite = cursor.moveToFirst();
        cursor.close();
        db.close();

        return isFavorite;
    }

    public List<Favorito> getExperienciasFavoritas() {
        List<Favorito> favoritos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVORITOS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int idExperiencia = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_EXPERIENCIA));
                String nombreExperiencia = cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE_EXPERIENCIA));
                boolean isFavorite = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FAVORITE)) == 1;

                Favorito favorito = new Favorito(idExperiencia, nombreExperiencia, isFavorite);
                favoritos.add(favorito);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return favoritos;
    }
}