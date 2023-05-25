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
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_FAVORITOS = "favoritos";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ID_CONTENIDO = "id_contenido";
    private static final String COLUMN_ID_EXPERIENCIA = "id_experiencia";
    private static final String COLUMN_CO_TITULO = "co_titulo";
    private static final String COLUMN_IS_FAVORITE = "is_favorite";

    private static final String CREATE_TABLE_FAVORITOS = "CREATE TABLE " + TABLE_FAVORITOS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_ID_EXPERIENCIA+ " INTEGER," +
            COLUMN_ID_CONTENIDO + " INTEGER," +
            COLUMN_CO_TITULO + " TEXT," +
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

    public void guardarExperienciaFavorita(int idExperiencia, int idContenido, String coTitulo) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_EXPERIENCIA, idExperiencia);
        values.put(COLUMN_ID_CONTENIDO, idContenido);
        values.put(COLUMN_CO_TITULO, coTitulo);
        values.put(COLUMN_IS_FAVORITE, true ? 1 : 0);

        db.insert(TABLE_FAVORITOS, null, values);
        db.close();
    }

    public void eliminarExperienciaFavorita(int idContenido) {
        SQLiteDatabase db = getWritableDatabase();

        String selection = COLUMN_ID_CONTENIDO + " = ?";
        String[] selectionArgs = {String.valueOf(idContenido)};

        db.delete(TABLE_FAVORITOS, selection, selectionArgs);
        db.close();
    }

    public boolean isExperienciaFavorita(int idContenido) {
        SQLiteDatabase db = getReadableDatabase();

        String selection = COLUMN_ID_CONTENIDO + " = ?";
        String[] selectionArgs = {String.valueOf(idContenido)};

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
                int idContenido = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_CONTENIDO));
                String coTitulo = cursor.getString(cursor.getColumnIndex(COLUMN_CO_TITULO));
                boolean isFavorite = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FAVORITE)) == 1;

                Favorito favorito = new Favorito(idExperiencia, idContenido, coTitulo, isFavorite);
                favoritos.add(favorito);
            } while (cursor.moveToNext());
        }

            cursor.close();
            db.close();

            return favoritos;
        }
    }